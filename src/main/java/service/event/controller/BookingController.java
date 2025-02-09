/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.event.exceptions.EventNotFoundException;
import service.event.model.EventTicket;
import service.event.model.EventTicketZone;
import service.event.request.BookingRequest;
import service.event.request.TicketCapacityRequest;
import service.event.services.TicketService;
import service.event.utils.ResponseHandler;

import javax.management.Query;
import java.util.List;

/**
 * @author admin
 */
@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private TicketService bookingService;

    @PostMapping("/ticket")
    public ResponseEntity<?> bookTicket(@RequestBody BookingRequest request) {
        try {
            // Gọi service để đặt vé
            EventTicket eventTicket = bookingService.bookTicket(request);

            // Trả về phản hồi thành công
            return ResponseHandler.resBuilder("Đặt vé thành công", HttpStatus.CREATED, eventTicket);
        } catch (EventNotFoundException e) {
            // Xử lý khi không tìm thấy vé hoặc sự kiện
            return ResponseHandler.resBuilder(e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            return ResponseHandler.resBuilder(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

    @GetMapping("/zone/{eventId}")
    public ResponseEntity<?> getZoneByEventId(
            @PathVariable Long eventId,
            @RequestParam(name = "day", required = false) Integer day) {
        try {
            TicketCapacityRequest req = new TicketCapacityRequest(eventId, day);
            List<EventTicketZone> res = bookingService.findByEventAndDay(req);

            if (res == null || res.isEmpty()) {
                return ResponseHandler.resBuilder(
                        "Không tìm thấy sự kiện hoặc chưa có zone nào",
                        HttpStatus.NOT_FOUND,
                        null
                );
            }

            return ResponseHandler.resBuilder(
                    "Lấy thông tin tất cả zone của sự kiện thành công",
                    HttpStatus.OK,
                    res
            );

        } catch (Exception e) {
            String errorMessage = e.getMessage().length() > 100
                    ? e.getMessage().substring(0, 100)
                    : e.getMessage();
            return ResponseHandler.resBuilder(
                    "Lỗi xảy ra trong quá trình lấy vé đã đặt: " + errorMessage,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }
    @GetMapping("/zone/{eventId}/all")
    public ResponseEntity<?> getAllZoneByEventId(
            @PathVariable Long eventId) {
        try {
            List<EventTicketZone> res = bookingService.findByEvent(eventId);

            if (res == null || res.isEmpty()) {
                return ResponseHandler.resBuilder(
                        "Không tìm thấy sự kiện hoặc chưa có zone nào",
                        HttpStatus.NOT_FOUND,
                        null
                );
            }

            return ResponseHandler.resBuilder(
                    "Lấy thông tin tất cả zone của sự kiện thành công",
                    HttpStatus.OK,
                    res
            );

        } catch (Exception e) {
            String errorMessage = e.getMessage().length() > 100
                    ? e.getMessage().substring(0, 100)
                    : e.getMessage();
            return ResponseHandler.resBuilder(
                    "Lỗi xảy ra trong quá trình lấy vé đã đặt: " + errorMessage,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }

}
