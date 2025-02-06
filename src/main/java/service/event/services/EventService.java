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
import org.springframework.data.domain.PageRequest;
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
        // Giả sử số lượng khách tối đa cho mỗi ngày 
        int dailyCapacity = eventDTO.getEventCapacity();
        
        for (int i = 1; i <= daysBetween; i++) {
            EventTicketCapacity ticketCapacity = new EventTicketCapacity();
            ticketCapacity.setDay(i);
            
            ticketCapacity.setRemainingCapacity(eventDTO.getEventCapacity()); 
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
