/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.event.exceptions.EventNotFoundException;
import service.event.model.EventTicket;
import service.event.response.TicketResponse;
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

    @GetMapping("/{userId}")
    public ResponseEntity<?> getTicketByEventId(@PathVariable String userId) {
        try {
            List<EventTicket> tickets = eventTicketService.getAllTicketByUserId(userId);
            if (tickets == null || tickets.isEmpty()) {
                return ResponseHandler.resBuilder("Không tìm thấy sự kiện hoặc chưa có vé nào được đặt", HttpStatus.OK, null);
            }

            // Chuyển đổi List<EventTicket> → List<TicketResponse>
            List<TicketResponse> ticketResponses = tickets.stream()
                    .map(TicketResponse::new)
                    .collect(Collectors.toList());

            return ResponseHandler.resBuilder("Lấy thông tin tất cả vé thành công", HttpStatus.OK, ticketResponses);

        } catch (EventNotFoundException e) {
            return ResponseHandler.resBuilder("Sự kiện không tồn tại: " + e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            String errorMessage = e.getMessage().length() > 100 ? e.getMessage().substring(0, 100) : e.getMessage();
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy vé đã đặt: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

}
