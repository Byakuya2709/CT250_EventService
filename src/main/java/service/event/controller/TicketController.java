/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.event.exceptions.EventNotFoundException;
import service.event.model.Event;
import service.event.model.EventTicket;
import service.event.request.RatingRequest;
import service.event.response.OneEventResponse;
import service.event.response.TicketResponse;
import service.event.services.EventService;
import service.event.services.TicketService;
import service.event.utils.ResponseHandler;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    TicketService eventTicketService;
    @Autowired 
    EventService eventService;
    

    @GetMapping("/buy/{userId}")
    public ResponseEntity<?> getTicketByEventId(@PathVariable String userId,
            @RequestParam(defaultValue = "0") int page, // Đổi mặc định về 0 để tránh lỗi
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<EventTicket> tickets = eventTicketService.getAllTicketByUserId(userId, pageable);

            if (tickets == null || tickets.isEmpty()) {
                return ResponseHandler.resBuilder("Không tìm thấy sự kiện hoặc chưa có vé nào được đặt", HttpStatus.OK, null);
            }

            // Chuyển đổi Page<EventTicket> sang Page<TicketResponse>
            Page<TicketResponse> ticketResponses = tickets.map(TicketResponse::new);

            return ResponseHandler.resBuilder("Lấy thông tin tất cả vé thành công", HttpStatus.OK, ticketResponses);

        } catch (EventNotFoundException e) {
            return ResponseHandler.resBuilder("Sự kiện không tồn tại: " + e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            String errorMessage = e.getMessage() != null && e.getMessage().length() > 100
                    ? e.getMessage().substring(0, 100)
                    : e.getMessage();
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy vé đã đặt: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PatchMapping("/rating/{ticketId}")
    public ResponseEntity<?>  ratingTicket(@PathVariable Long ticketId,@RequestBody RatingRequest req){
        try {
            EventTicket  res = eventService.ratingEvent(ticketId, req);
            return ResponseHandler.resBuilder("Đánh giá sự kiện thành công", HttpStatus.OK,new TicketResponse(res) );
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi:"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
    @PatchMapping("/feedback/{ticketId}")
    public ResponseEntity<?>  dimissrRatingEvent(@PathVariable Long ticketId,@RequestBody RatingRequest req){
        try {
            EventTicket  res = eventService.dismissRatingEvent(ticketId, req);
            return ResponseHandler.resBuilder("Hủy đánh giá sự kiện thành công", HttpStatus.OK, res);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi:"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
    
    @PatchMapping("/{ticketId}")
    public ResponseEntity<?> ratingEvent(@PathVariable Long ticketId){
        try {
            TicketResponse  res = eventTicketService.requestCancelledTicket(ticketId);
            return ResponseHandler.resBuilder("Hủy vé thành công", HttpStatus.OK, res);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi khi hủy vé: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    @DeleteMapping("/{ticketId}")
    public ResponseEntity<?> cancelledTicket(@PathVariable Long ticketId) {
        try {
            eventTicketService.cancelledTicket(ticketId);
            return ResponseHandler.resBuilder("Hủy vé thành công", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi khi hủy vé: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/{eventID}/all")
    public ResponseEntity<?> getAllTicketByEventId(@PathVariable Long eventID) {
        try {
            List<EventTicket> res = eventTicketService.findAllTicketByEvent(eventID);

            if (res == null || res.isEmpty()) {
                return ResponseHandler.resBuilder("Không tìm thấy sự kiện hoặc chưa có vé nào được đặt", HttpStatus.OK, null);
            }

            return ResponseHandler.resBuilder("Lấy thông tin tất cả vé thành công", HttpStatus.OK, res);

        } catch (EventNotFoundException e) {
            return ResponseHandler.resBuilder("Sự kiện không tồn tại: " + e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            String errorMessage = e.getMessage().length() > 100 ? e.getMessage().substring(0, 100) : e.getMessage();
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy vé đã đặt: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/{eventID}")
    public ResponseEntity<?> getTicketByEventId(@PathVariable Long eventID,
            @RequestParam(name = "day", required = false) Integer day) {
        try {
            List<EventTicket> res;

            if (day != null) {
                res = eventTicketService.getAllTicketByEventAndDay(eventID, day);
            } else {
                res = eventTicketService.getAllTicketByEvent(eventID);
            }

            if (res == null || res.isEmpty()) {
                return ResponseHandler.resBuilder("Không tìm thấy sự kiện hoặc chưa có vé nào được đặt", HttpStatus.OK, null);
            }

            return ResponseHandler.resBuilder("Lấy thông tin tất cả vé thành công", HttpStatus.OK, res);

        } catch (EventNotFoundException e) {
            return ResponseHandler.resBuilder("Sự kiện không tồn tại: " + e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            String errorMessage = e.getMessage().length() > 100 ? e.getMessage().substring(0, 100) : e.getMessage();
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy vé đã đặt: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
    
  
}
