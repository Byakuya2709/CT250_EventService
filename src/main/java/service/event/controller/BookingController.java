/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.event.exceptions.EventNotFoundException;
import service.event.model.EventTicket;
import service.event.request.BookingRequest;
import service.event.services.TicketService;
import service.event.utils.ResponseHandler;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private TicketService bookingService;

//    @PostMapping("/ticket")
//    public ResponseEntity<?> bookTicket(@RequestBody BookingRequest request) {
//        try {
//            // Gọi service để đặt vé
//            EventTicket eventTicket = bookingService.bookTicket(request);
//
//            // Trả về phản hồi thành công
//            return ResponseHandler.resBuilder("Đặt vé thành công", HttpStatus.CREATED, eventTicket);
//        } catch (EventNotFoundException e) {
//            // Xử lý khi không tìm thấy vé hoặc sự kiện
//             return ResponseHandler.resBuilder(e.getMessage(), HttpStatus.NOT_FOUND, null);
//        } catch (Exception e) {
//            return ResponseHandler.resBuilder(e.getMessage(), HttpStatus.NOT_FOUND, null);
//        }
//    }

}
