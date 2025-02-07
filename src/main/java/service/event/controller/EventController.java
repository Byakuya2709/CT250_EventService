/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.event.dto.EventDTO;
import service.event.model.Event;
import service.event.model.EventTicket;
import service.event.services.EventService;
import service.event.services.TicketService;
import service.event.utils.ResponseHandler;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    EventService eventService;
    @Autowired
    TicketService eventTicketService;

    @PostMapping("/created")
    public ResponseEntity<?> saveEvent(@RequestBody EventDTO eventDTO) {
        try {
            // Gọi service để lưu sự kiện
            Event savedEvent = eventService.saveEvent(eventDTO);

            // Trả về response thành công với sự kiện đã lưu
            return ResponseHandler.resBuilder("Tạo event thành công", HttpStatus.CREATED, savedEvent);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình tạo event" + e.getMessage().substring(0, 100), HttpStatus.CREATED, null);
        }
        // Xử lý lỗi khi không thể parse ngày tháng

    }

    @PostMapping("/create")
    public ResponseEntity<?> testEvent(@RequestBody EventDTO eventDTO) {
        try {
            // Gọi service để lưu sự kiện
            Event savedEvent = eventService.saveEvent(eventDTO);

            // Trả về response thành công với sự kiện đã lưu
            return ResponseHandler.resBuilder("Tạo event thành công", HttpStatus.CREATED, savedEvent);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình tạo event" + e.getMessage().substring(0, 100), HttpStatus.CREATED, null);
        }
    }
    

    @GetMapping("/getall")
    public ResponseEntity<?> getAllEvent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (size > 100) {
                size = 100; // Cap size at 100 to prevent large queries
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Event> events = eventService.getAllEvents(pageable);
            return ResponseHandler.resBuilder("Lấy danh sách sự kiện thành công", HttpStatus.OK, events);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy event" + e.getMessage().substring(0, 100), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    @GetMapping("/{eventID}")
    public ResponseEntity<?> getEventById(@PathVariable Long eventID) {
        try {
            Event event = eventService.getEventById(eventID);
            if (event != null) {
                return ResponseHandler.resBuilder("Lấy thông tin sự kiện thành công", HttpStatus.OK, event);
            } else {
                return ResponseHandler.resBuilder("Không tìm thấy sự kiện", HttpStatus.NOT_FOUND, null);
            }
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy sự kiện: " + e.getMessage().substring(0, 100), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
//    @PutMapping("/{eventID}")
//    public ResponseEntity<?> updateEvent(@PathVariable Long eventID, @RequestBody EventDTO eventDTO) {
//        try {
//            Event updatedEvent = eventService.updateEvent(eventID, eventDTO);
//            if (updatedEvent != null) {
//                return ResponseHandler.resBuilder("Cập nhật sự kiện thành công", HttpStatus.OK, updatedEvent);
//            } else {
//                return ResponseHandler.resBuilder("Không tìm thấy sự kiện để cập nhật", HttpStatus.NOT_FOUND, null);
//            }
//        } catch (Exception e) {
//            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình cập nhật sự kiện: " + e.getMessage().substring(0, 100), HttpStatus.INTERNAL_SERVER_ERROR, null);
//        }
//    }

    @DeleteMapping("/{eventID}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long eventID) {
        try {
            boolean isDeleted = eventService.deleteEvent(eventID);
            if (isDeleted) {
                return ResponseHandler.resBuilder("Xóa sự kiện thành công", HttpStatus.OK, null);
            } else {
                return ResponseHandler.resBuilder("Không tìm thấy sự kiện để xóa", HttpStatus.NOT_FOUND, null);
            }
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình xóa sự kiện: " + e.getMessage().substring(0, 100), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
    @GetMapping("/tickets/{eventID}")
    public ResponseEntity<?> getTicketByEventId(@PathVariable Long eventID) {
        try {
            List<EventTicket> res = eventTicketService.getAllTicketByEvent(eventID);

            if (res == null || res.isEmpty()) {
                return ResponseHandler.resBuilder("Không tìm thấy sự kiện hoặc chưa có vé nào được đặt", HttpStatus.NOT_FOUND, null);
            }

            return ResponseHandler.resBuilder("Lấy thông tin tất cả vé thành công", HttpStatus.OK, res);

        } catch (Exception e) {
            String errorMessage = e.getMessage().length() > 100 ? e.getMessage().substring(0, 100) : e.getMessage();
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy vé đã đặt: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
