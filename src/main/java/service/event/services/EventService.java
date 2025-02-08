/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.services;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import service.event.dto.EventDTO;
import service.event.dto.ZoneDTO;
import service.event.model.Event;
import service.event.model.EventTicketZone;
import service.event.repository.EventRepository;
import service.event.utils.DateUtils;

/**
 *
 * @author admin
 */
@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;
//
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
        event.setEventStatus(eventDTO.getEventStatus());
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
                new ZoneDTO("VIP", 1.4, vipCapacity),       // 140% giá gốc
                new ZoneDTO("STANDARD", 1.0, standardCapacity), // 100% giá gốc
                new ZoneDTO("ECONOMY", 0.8, economyCapacity)  // 80% giá gốc
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


    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }
    // Lấy sự kiện theo ID
    public Event getEventById(Long eventID) {
        Optional<Event> event = eventRepository.findById(eventID);
        return event.orElse(null); // Trả về null nếu không tìm thấy
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

}
