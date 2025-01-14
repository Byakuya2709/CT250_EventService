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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.event.dto.EventDTO;
import service.event.model.Event;
import service.event.services.EventService;
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

    @PostMapping("/create")
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

    @PostMapping("/test")
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
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy event" + e.getMessage().substring(0, 100), HttpStatus.CREATED, null);
        }
    }

}
