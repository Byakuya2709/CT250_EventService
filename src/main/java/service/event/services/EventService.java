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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import service.event.dto.EventDTO;
import service.event.model.Event;
import service.event.model.EventTicketCapacity;
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

        // Tính số ngày giữa eventStartDate và eventEndDate
        Date startDate = DateUtils.convertStringToDate(eventDTO.getEventStartDate());
        Date endDate = DateUtils.convertStringToDate(eventDTO.getEventEndDate());
        long daysBetween = ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant());

        if (daysBetween < 1) {
            daysBetween = 1;
        }

        List<EventTicketCapacity> ticketCapacities = new ArrayList<>();
        for (int i = 1; i <= daysBetween; i++) {
            EventTicketCapacity ticketCapacity = new EventTicketCapacity();
            ticketCapacity.setDay(i);
            ticketCapacity.setRemainingCapacity(eventDTO.getEventCapacity());  // Giả sử số lượng còn lại là 0
            ticketCapacity.setEvent(event);
            ticketCapacities.add(ticketCapacity);  // Thêm vào danh sách
        }

        event.setTicketCapacities(ticketCapacities);  // Đặt danh sách ticket capacities vào event

        // Lưu sự kiện vào cơ sở dữ liệu
        eventRepository.save(event);

        return event;
    }
    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }
}
