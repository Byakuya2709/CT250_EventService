/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.services;

import com.google.zxing.WriterException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import service.event.exceptions.CapacityExceededException;
import service.event.exceptions.EntityNotFoundExceptions;
import service.event.exceptions.EventNotFoundException;
import service.event.model.Event;
import service.event.model.EventTicket;
import service.event.model.EventTicketZone;
import service.event.model.VNPayTransaction;
import service.event.repository.EventRepository;
import service.event.repository.EventTicketRepository;
import service.event.repository.EventTicketZoneRepository;
import service.event.request.BookingRequest;
import service.event.request.TicketCapacityRequest;
import service.event.response.TicketResponse;
import service.event.utils.DateUtils;
import service.event.utils.QRUtils;

import javax.transaction.Transactional;
import service.event.request.RatingRequest;

/**
 *
 * @author admin
 */
@Service
public class TicketService {
    
    @Autowired
    private EventTicketRepository eventTicketRepository;
    
    @Autowired
    private EventTicketZoneRepository eventTicketZoneRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    public void cancelledTicket(Long ticketId) {
        // Tìm vé theo ID
        EventTicket ticket = eventTicketRepository.findById(ticketId)
                .orElseThrow(() -> new EventNotFoundException("Ticket not found"));

        // Lấy thông tin sự kiện & loại vé
        Event event = ticket.getEvent();
        EventTicket.TicketDay ticketType = ticket.getTicketDuration();
        String ticketZone = ticket.getTicketPosition().split("_")[0]; // Lấy phần "VIP", "STANDARD", "ECONOMY"
        int ticketDay = ticket.getTicketDay(); // Ngày vé có hiệu lực

        if (ticketType == EventTicket.TicketDay.SINGLE_DAY) {
            // Lấy khu vực theo ngày cụ thể
            EventTicketZone zone = eventTicketZoneRepository.findByEventAndDayAndZoneName(event, ticketDay, ticketZone)
                    .orElseThrow(() -> new RuntimeException("Zone not found!"));

            // Cập nhật lại số lượng vé còn lại
            zone.setRemainingCapacity(zone.getRemainingCapacity() + 1);
            eventTicketZoneRepository.save(zone);
            
        } else if (ticketType == EventTicket.TicketDay.ALL_DAYS) {
            // Lấy tất cả ngày của zone tương ứng
            List<EventTicketZone> matchingZones = eventTicketZoneRepository.findByEvent(event).stream()
                    .filter(zone -> extractZoneType(zone.getZoneName()).equals(ticketZone))
                    .collect(Collectors.toList());

            // Cập nhật lại số lượng vé của từng ngày
            for (EventTicketZone zone : matchingZones) {
                zone.setRemainingCapacity(zone.getRemainingCapacity() + 1);
                eventTicketZoneRepository.save(zone);
            }
        }

        // Xóa vé khỏi database
        eventTicketRepository.deleteById(ticketId);
    }

    private String extractZoneType(String zoneName) {
        return zoneName.split("_")[0]; // Tách lấy phần "VIP", "STANDARD", "ECONOMY"
    }
    
    @Transactional
    public TicketResponse requestCancelledTicket(Long ticketId) {
        EventTicket ticket = eventTicketRepository.findById(ticketId)
                .orElseThrow(() -> new EventNotFoundException("Ticket not found"));

        // Kiểm tra nếu vé đã bị hủy trước đó
        if (ticket.getTicketStatus().equals(EventTicket.TicketStatus.CANCELLED.toString())) {
            throw new IllegalStateException("Ticket has already been canceled");
        }
        
        ticket.setTicketStatus(EventTicket.TicketStatus.CANCELLED.toString());
        
        eventTicketRepository.save(ticket);
        
        return new TicketResponse(ticket);
    }
    
    public List<EventTicketZone> findByEventAndDay(TicketCapacityRequest request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        
        return eventTicketZoneRepository.findByEventAndDay(event, request.getDay());
    }
    
    public List<EventTicket> findAllTicketByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        
        return eventTicketRepository.findByEvent(event);
    }
    
    public EventTicket findById(Long ticketId) {
        return eventTicketRepository.findById(ticketId).orElseThrow(() -> new EntityNotFoundExceptions("Ticket not found"));
    }
    
    public EventTicket updatePAIDTicket(VNPayTransaction transaction) throws WriterException, IOException {
        EventTicket ticket = eventTicketRepository.findByTransaction(transaction)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setTicketStatus(EventTicket.TicketStatus.PAID.toString());
        
        byte[] qrCode = QRUtils.generateQRCode(ticket);
        ticket.setQrCode(qrCode);
        return eventTicketRepository.save(ticket);
    }
    
    public List<EventTicketZone> findByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        
        return eventTicketZoneRepository.findByEvent(event);
    }
    
    public List<EventTicket> getAllTicketByEventAndDay(Long eventId, Integer day) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        return eventTicketRepository.findByEventAndTicketDay(event, day);
    }
    
    public Page<EventTicket> getAllTicketByUserId(String userId, Pageable pageable) {
        
        return eventTicketRepository.findByTicketUserId(userId, pageable);
    }
    
    public EventTicket bookTicket(BookingRequest request) throws Exception {
        // Kiểm tra xem sự kiện có tồn tại không
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        // Xác định loại vé (SINGLE_DAY hoặc ALL_DAYS)
        EventTicket.TicketDay ticketDuration = parseTicketDuration(request.getTicketDuration());

        // Tạo vé mới và thiết lập thông tin cơ bản
        EventTicket eventTicket = createBaseTicket(request, event, ticketDuration);
        
        if (ticketDuration == EventTicket.TicketDay.SINGLE_DAY) {
            processSingleDayTicket(request, event, eventTicket);
        } else if (ticketDuration == EventTicket.TicketDay.ALL_DAYS) {
            processAllDaysTicket(request, event, eventTicket);
        }

        // Thiết lập thời gian đặt vé
        eventTicket.setTicketBookingTime(new Date());

        // Lưu vé vào database
        return eventTicketRepository.save(eventTicket);
    }

    /**
     * Phương thức kiểm tra và parse ticket duration
     */
    private EventTicket.TicketDay parseTicketDuration(String ticketDuration) {
        try {
            return EventTicket.TicketDay.valueOf(ticketDuration.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ticket duration type: " + ticketDuration);
        }
    }

    /**
     * Tạo đối tượng EventTicket với thông tin cơ bản
     */
    private EventTicket createBaseTicket(BookingRequest request, Event event, EventTicket.TicketDay ticketDuration) {
        EventTicket eventTicket = new EventTicket();
        eventTicket.setEvent(event);
        eventTicket.setTicketExpiredTime(event.getEventEndDate());
        eventTicket.setTicketUserId(request.getUserId());
        eventTicket.setTicketPosition(request.getTicketPosition());
        eventTicket.setTicketDuration(ticketDuration);
        eventTicket.setTicketStatus(EventTicket.TicketStatus.UNPAID.toString());
        eventTicket.setTicketValidity(EventTicket.TicketValidity.INACTIVE.toString());
        eventTicket.setTicketDay(Integer.parseInt(request.getDay()));
        eventTicket.setTicketUserEmail(request.getUserEmail());
        eventTicket.setTicketRating(false);
        return eventTicket;
    }

    /**
     * Xử lý đặt vé loại SINGLE_DAY
     */
    private void processSingleDayTicket(BookingRequest request, Event event, EventTicket eventTicket) {
        // Kiểm tra và parse ngày
        int dayNumber = parseTicketDay(request.getDay());

        // Lấy khu vực vé
        EventTicketZone zone = eventTicketZoneRepository.findByEventAndDayAndZoneName(event, dayNumber, request.getTicketZone())
                .orElseThrow(() -> new CapacityExceededException("Không tìm thấy khu vực vé phù hợp!"));

        // Kiểm tra sức chứa
        if (zone.getRemainingCapacity() <= 0) {
            throw new CapacityExceededException("No tickets available for the selected zone.");
        }

        // Tính giá vé và làm tròn
        eventTicket.setTicketPrice(
                BigDecimal.valueOf(request.getTicketPrice() * zone.getZoneRate())
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue()
        );

        // Giảm sức chứa
        updateZoneCapacity(zone);

        // Xác định ngày kích hoạt vé
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getEventStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, dayNumber - 1);
        eventTicket.setTicketDayActive(calendar.getTime());
    }

    /**
     * Xử lý đặt vé loại ALL_DAYS
     */
    private void processAllDaysTicket(BookingRequest request, Event event, EventTicket eventTicket) {
        // Lấy tất cả zone của sự kiện
        List<EventTicketZone> allZones = eventTicketZoneRepository.findByEvent(event);
        
        if (allZones.isEmpty()) {
            throw new CapacityExceededException("No remaining capacity for all-day tickets.");
        }
        
        int totalDays = event.getTotalDays(); // Tổng số ngày của sự kiện

        if (totalDays<=0){
            totalDays=1;
        }
        // Lọc danh sách zone đúng với zone khách chọn (VIP, STANDARD, ECONOMY)
        List<EventTicketZone> selectedZones = allZones.stream()
                .filter(zone -> zone.getZoneName().equals(request.getTicketZone()))
                .collect(Collectors.toList());
        
        if (selectedZones.size() < totalDays) {
            throw new CapacityExceededException("Not enough capacity for all-day tickets.");
        }
        
        double zoneRate = 1.0; // Mặc định

        // Kiểm tra tất cả các ngày của zone có còn vé không
        for (EventTicketZone capacityDay : selectedZones) {
            if (capacityDay.getRemainingCapacity() <= 0) {
                throw new CapacityExceededException("No remaining capacity for all-day tickets.");
            }
        }

        // Giảm số lượng vé của **tất cả các ngày của zone đó**
        for (EventTicketZone capacityDay : selectedZones) {
            updateZoneCapacity(capacityDay);
            zoneRate = capacityDay.getZoneRate(); // Lấy zone rate từ ngày đầu tiên
        }

        // Cập nhật giá vé dựa trên số ngày
        eventTicket.setTicketPrice(
                BigDecimal.valueOf(request.getTicketPrice() * zoneRate * totalDays)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue()
        );

        // Ngày active mặc định là ngày bắt đầu sự kiện
        eventTicket.setTicketDayActive(event.getEventStartDate());
    }

    /**
     * Chuyển đổi ngày vé từ String sang Integer, kiểm tra lỗi
     */
    private int parseTicketDay(String day) {
        if (day == null || day.isEmpty()) {
            throw new IllegalArgumentException("Day must be provided for single day tickets.");
        }
        try {
            return Integer.parseInt(day);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid day provided: " + day);
        }
    }

    /**
     * Giảm sức chứa của khu vực vé
     */
    private void updateZoneCapacity(EventTicketZone zone) {
        zone.setRemainingCapacity(zone.getRemainingCapacity() - 1);
        eventTicketZoneRepository.save(zone);
    }
    
    public List<EventTicket> getAllTicketByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        return eventTicketRepository.findByEvent(event);
    }
    
    public long countTicketsByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        return eventTicketRepository.countByEvent(event);
    }
    
    public Double getTotalTicketPriceByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        Double totalPrice = eventTicketRepository.sumTicketPriceByEvent(event);
        return totalPrice != null ? totalPrice : 0.0;
    }
    
    public Page<EventTicket> getTickets(String status, String userEmail, Long eventId, Pageable pageable) {
        if (eventId != null) {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new EventNotFoundException("Event not found"));
            
            if (status != null && userEmail != null) {
                return eventTicketRepository.findByTicketStatusAndTicketUserEmailAndEvent_EventId(status, userEmail, eventId, pageable);
            } else if (status != null) {
                return eventTicketRepository.findByTicketStatusAndEvent(status, event, pageable);
            } else if (userEmail != null) {
                return eventTicketRepository.findByTicketUserEmailAndEvent(userEmail, event, pageable);
            } else {
                return eventTicketRepository.findByEvent(event, pageable);
            }
        } else if (status != null && userEmail != null) {
            return eventTicketRepository.findByTicketStatusAndTicketUserEmail(status, userEmail, pageable);
        } else if (status != null) {
            return eventTicketRepository.findByTicketStatus(status, pageable);
        } else if (userEmail != null) {
            return eventTicketRepository.findByTicketUserEmail(userEmail, pageable);
        } else {
            return eventTicketRepository.findAll(pageable);
        }
    }
    
}
