/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.services;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import service.event.dto.EventDTO;
import service.event.dto.EventStatsDTO;
import service.event.dto.ZoneDTO;
import service.event.exceptions.EventNotFoundException;
import service.event.exceptions.FailedUpdateEventEx;
import service.event.model.Event;
import service.event.model.EventSummary;
import service.event.model.EventTicketZone;
import service.event.repository.EventRepository;
import service.event.repository.EventTicketRepository;
import service.event.repository.EventTicketZoneRepository;
import service.event.request.TicketCapacityRequest;
import service.event.request.UpdatedZoneRequest;
import service.event.utils.DateUtils;

/**
 *
 * @author admin
 */
@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventTicketZoneRepository eventTicketZoneRepository;
//

    @Autowired
    EventTicketRepository eventTicketRepository;
//    @Autowired
//    EventTicketCapacityRepository eventTicketCapacityRepository;

    public Event saveEvent(EventDTO eventDTO) {
        Event event = new Event();

        // Gán các thuộc tính từ DTO vào entity
        event.setEventTitle(eventDTO.getEventTitle());
        event.setEventStartDate(DateUtils.convertStringToDate(eventDTO.getEventStartDate()));
        event.setEventDescription(eventDTO.getEventDescription());
        event.setEventAgeTag(eventDTO.getEventAgeTag());  // Gán chuỗi từ DTO trực tiếp vào entity
        event.setEventEndDate(DateUtils.convertStringToDate(eventDTO.getEventEndDate()));
        event.setEventTags(eventDTO.getEventTags());
        event.setEventDuration(eventDTO.getEventDuration());
        event.setEventAddress(eventDTO.getEventAddress());
        event.setEventCapacity(eventDTO.getEventCapacity());
//        event.setEventStatus(eventDTO.getEventStatus());
        event.setEventStatus("AWAITING_APPROVAL");
        event.setEventCompanyId(eventDTO.getEventCompanyId());
        event.setEventListArtist(eventDTO.getEventListArtist());
        event.setEventPrice(eventDTO.getEventPrice());
        // Khởi tạo eventRatingStart
        Map<Integer, Integer> eventRatingStart = new HashMap<>();
        eventRatingStart.put(1, 0);
        eventRatingStart.put(2, 0);
        eventRatingStart.put(3, 0);
        eventRatingStart.put(4, 0);
        eventRatingStart.put(5, 0);
        event.setEventRatingStart(eventRatingStart);

        event.setEventListImgURL(eventDTO.getEventListImgURL());

        // Tính số ngày sự kiện diễn ra
        Date startDate = DateUtils.convertStringToDate(eventDTO.getEventStartDate());
        Date endDate = DateUtils.convertStringToDate(eventDTO.getEventEndDate());
        long daysBetween = ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant());

        if (daysBetween < 1) {
            daysBetween = 1;
        }

        List<EventTicketZone> ticketZones = new ArrayList<>();

        int totalCapacity = eventDTO.getEventCapacity(); // Tổng số vé

// Tính số vé của từng zone
        int vipCapacity = (int) Math.round(totalCapacity * 0.2);
        int standardCapacity = (int) Math.round(totalCapacity * 0.5);
        int economyCapacity = totalCapacity - (vipCapacity + standardCapacity); // Còn lại cho Economy

        List<ZoneDTO> defaultZones = List.of(
                new ZoneDTO("VIP", 1.4, vipCapacity), // 140% giá gốc
                new ZoneDTO("STANDARD", 1.0, standardCapacity), // 100% giá gốc
                new ZoneDTO("ECONOMY", 0.8, economyCapacity) // 80% giá gốc
        );

        // Duyệt qua từng zone mặc định và mỗi ngày của sự kiện
        for (ZoneDTO zoneDTO : defaultZones) {
            for (int i = 1; i <= daysBetween; i++) {
                EventTicketZone ticketZone = new EventTicketZone();

                ticketZone.setZoneName(zoneDTO.getZoneName());
                ticketZone.setZoneRate(zoneDTO.getZoneRate());
                ticketZone.setZoneCapacity(zoneDTO.getZoneCapacity());
                ticketZone.setDay(i);
                ticketZone.setRemainingCapacity(zoneDTO.getZoneCapacity()); // Ban đầu còn đủ vé
                ticketZones.add(ticketZone);

                ticketZone.setEvent(event);
            }
        }

        event.setTicketZones(ticketZones);

        // Lưu sự kiện và zones
        eventRepository.save(event);

        return event;
    }

    public List<EventTicketZone> updateZone(Long eventId, List<UpdatedZoneRequest> req) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        List<EventTicketZone> listZone = event.getTicketZones();

        for (UpdatedZoneRequest updatedZone : req) {
            for (EventTicketZone zone : listZone) {
                if (zone.getZoneName().equals(updatedZone.getZoneName())) {
                    // Tính toán remainingCapacity mới
                    int capacityChange = updatedZone.getZoneCapacity() - zone.getZoneCapacity();
                    int newRemainingCapacity = zone.getRemainingCapacity() + capacityChange;

                    // Kiểm tra remainingCapacity không được nhỏ hơn 0
                    if (newRemainingCapacity < 0) {
                        throw new IllegalArgumentException("Remaining capacity của zone "
                                + zone.getZoneName() + " không hợp lệ!");
                    }

                    zone.setRemainingCapacity(newRemainingCapacity);
                    zone.setZoneRate(updatedZone.getZoneRate());
                    zone.setZoneCapacity(updatedZone.getZoneCapacity());
                }
            }
        }

        // Tính tổng eventCapacity mới
        Set<String> processedZones = new HashSet<>();
        Integer newEventCapacity = 0;

        for (EventTicketZone zone : listZone) {
            if (!processedZones.contains(zone.getZoneName())) {
                newEventCapacity += zone.getZoneCapacity();
                processedZones.add(zone.getZoneName());
            }
        }

        event.setEventCapacity(newEventCapacity);

        try {
            eventRepository.save(event);
        } catch (Exception e) {  // Bắt lỗi chung nếu save thất bại
            throw new FailedUpdateEventEx("Lỗi khi cập nhật sự kiện: " + e.getMessage());
        }

        return eventTicketZoneRepository.saveAll(listZone);
    }

    public List<Event> get5ByTag(String tag, String status) {
        return eventRepository.findByEventTagAndEventStatus(tag, status);
    }

    public List<Event> getTopRatedEvents(int limit) {
        return eventRepository.findTopRatedEvents(limit);
    }

    public List<EventTicketZone> getAllZoneByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        return eventTicketZoneRepository.findByEvent(event);
    }

    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public List<EventSummary> getAllEventsByCompanyId(String companyId) {
        return eventRepository.findAllByEventCompanyId(companyId);
    }

    public Page<EventSummary> getAllEventSummary(String eventStatus, Pageable pageable) {
        return eventRepository.findByEventStatus(eventStatus, pageable);
    }

    // Lấy sự kiện theo ID
    public Event getEventById(Long eventID) {
        Optional<Event> event = eventRepository.findById(eventID);
        return event.orElse(null); // Trả về null nếu không tìm thấy
    }

    public Long countByEventCompanyId(String companyId) {
        return eventRepository.countByEventCompanyId(companyId);
    }

    public List<EventStatsDTO> getEventTicketStatisticsByCompanyId(String companyId) {
        return eventRepository.getEventTicketStatisticsByCompanyId(companyId);
    }

//    // Cập nhật sự kiện
//    public Event updateEvent(Long eventID, EventDTO eventDTO) {
//        Optional<Event> optionalEvent = eventRepository.findById(eventID);
//        if (optionalEvent.isPresent()) {
//            Event event = optionalEvent.get();
//            // Cập nhật thông tin sự kiện từ EventDTO vào Entity Event
//            event.setName(eventDTO.getName());  // Ví dụ: cập nhật tên
//            event.setDescription(eventDTO.getDescription()); // Ví dụ: cập nhật mô tả
//            event.setStartDate(eventDTO.getStartDate()); // Ví dụ: cập nhật ngày bắt đầu
//            // Lưu lại sự kiện đã cập nhật
//            return eventRepository.save(event);
//        } else {
//            return null; // Trả về null nếu không tìm thấy sự kiện
//        }
//    }
    // Xóa sự kiện
    public boolean deleteEvent(Long eventID) {
        Optional<Event> event = eventRepository.findById(eventID);
        if (event.isPresent()) {
            eventRepository.delete(event.get());
            return true;
        } else {
            return false; // Trả về false nếu không tìm thấy sự kiện
        }
    }

    public Map<String, Object> reportEvent() {
        Map<String, Object> res = new HashMap<>();

        // Đếm số sự kiện theo trạng thái (GROUP BY để tối ưu)
        List<Object[]> eventStatusResults = eventRepository.countEventsByStatus();
        Map<String, Long> eventStatusCount = new HashMap<>();
        eventStatusResults.forEach(result -> eventStatusCount.put((String) result[0], (Long) result[1]));

        // Đếm tổng số sự kiện (có thể lấy từ eventStatusCount nếu mọi trạng thái đều có ít nhất 1 event)
        Long countedEvent = eventStatusCount.values().stream().reduce(0L, Long::sum);

        // Lấy từng trạng thái cụ thể
        Long countedCancelled = eventStatusCount.getOrDefault("CANCELLED", 0L);
        Long countedAwaiting = eventStatusCount.getOrDefault("AWAITING_APPROVAL", 0L);
        Long countedUpcoming = eventStatusCount.getOrDefault("UP_COMING", 0L);

        // Đếm tổng số vé theo trạng thái
        List<Object[]> ticketStatusResults = eventTicketRepository.countTicketsByStatus();
        Map<String, Long> ticketStatusCount = new HashMap<>();
        ticketStatusResults.forEach(result -> ticketStatusCount.put((String) result[0], (Long) result[1]));

        Long countTicketUnpaid = ticketStatusCount.getOrDefault("UNPAID", 0L);
        Long countTicketPaid = ticketStatusCount.getOrDefault("PAID", 0L);
        Long countTicket = countTicketUnpaid + countTicketPaid;

        // Thêm dữ liệu vào kết quả trả về
        res.put("totalEvent", countedEvent);
        res.put("cancelledEvent", countedCancelled);
        res.put("awaitingApprovalEvent", countedAwaiting);
        res.put("upcomingEvent", countedUpcoming);

        res.put("totalTicket", countTicket);
        res.put("unpaidTicket", countTicketUnpaid);
        res.put("paidTicket", countTicketPaid);

        return res;
    }


    public Double getTotalPriceWithStatus(int year, int month, String status) {
        return eventTicketRepository.getTotalPriceForMonthAndStatus(year, month, status);
    }

//    public Map<Integer, Double> getTotalPriceForYear(int year, String status) {
//        List<Object[]> results = eventTicketRepository.getMonthlyTotalPrice(year, status);
//
//        Map<Integer, Double> monthlyRevenue = new HashMap<>();
//
//        // Duyệt qua kết quả và lưu vào Map với key = tháng, value = tổng tiền
//        for (Object[] result : results) {
//            Integer month = (Integer) result[0];
//            Double totalPrice = (Double) result[1];
//            monthlyRevenue.put(month, totalPrice);
//        }
//
//        return monthlyRevenue; // Trả về dữ liệu dạng {1=500.0, 2=800.0, ..., 12=300.0}
//    }

    public List<Map<String, Object>> getMonthlyTotalRevenueByStatus(int year) {
        List<Object[]> results = eventTicketRepository.getMonthlyTotalPriceByStatus(year);

        return results.stream().map(obj -> Map.of(
                "month", obj[0],
                "ticketStatus", obj[1],
                "totalPrice", obj[2]
        )).collect(Collectors.toList());
    }

}
