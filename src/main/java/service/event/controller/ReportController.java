/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.event.repository.EventRepository;
import service.event.services.EventService;
import service.event.utils.ResponseHandler;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    EventService eventService;
    
    @GetMapping("/admin")
    public ResponseEntity<?> reportForAccount() {
        try {
            return ResponseHandler.resBuilder("Lấy báo cáo sự kiện thành công", HttpStatus.OK,eventService.reportEvent() );
        } catch (Exception ex) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra trong hệ thống", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
