/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.event.exceptions.CapacityExceededException;
import service.event.exceptions.EntityNotFoundExceptions;
import service.event.exceptions.EventNotFoundException;
import service.event.model.Event;
import service.event.model.EventTicket;
import service.event.model.EventTicketZone;
import service.event.repository.EventRepository;
import service.event.repository.EventTicketRepository;
import service.event.repository.EventTicketZoneRepository;
import service.event.request.BookingRequest;
import service.event.request.TicketCapacityRequest;
import service.event.utils.DateUtils;

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
        eventTicket.setTicketUserId(request.getUserId());
        eventTicket.setTicketPosition(request.getTicketPosition());
        eventTicket.setTicketDuration(ticketDuration);
        eventTicket.setTicketStatus(EventTicket.TicketStatus.UNPAID.toString());
        eventTicket.setTicketValidity(EventTicket.TicketValidity.INACTIVE.toString());
        eventTicket.setTicketDay(Integer.parseInt(request.getDay()));
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
        List<EventTicketZone> totalCapacity = eventTicketZoneRepository.findByEvent(event);

        if (totalCapacity.isEmpty()) {
            throw new CapacityExceededException("No remaining capacity for all-day tickets.");
        }

        // Kiểm tra tất cả ngày còn vé không
        for (EventTicketZone capacityDay : totalCapacity) {
            if (capacityDay.getRemainingCapacity() <= 0) {
                throw new CapacityExceededException("No remaining capacity for all-day tickets.");
            }
        }

        int totalDays = event.getTotalDays(); // Giả sử phương thức này tồn tại

        // Tính giá vé
        double zoneRate = 1.0;
        // Giảm số lượng vé
        for (EventTicketZone capacityDay : totalCapacity) {
            updateZoneCapacity(capacityDay);
            if (capacityDay.getZoneName().equals(request.getTicketZone())) {
                zoneRate = capacityDay.getZoneRate();
            }
        }
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
}
