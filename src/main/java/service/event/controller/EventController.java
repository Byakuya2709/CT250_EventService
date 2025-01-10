/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        }catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình tạo event" + e.getMessage().substring(0, 100), HttpStatus.CREATED,null);
        }
        // Xử lý lỗi khi không thể parse ngày tháng
        
    }
}
