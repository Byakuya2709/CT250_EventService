/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.event.dto.EventTicketSummaryDTO;
import service.event.repository.EventRepository;
import service.event.services.EventService;
import service.event.utils.ResponseHandler;

/**
 * @author admin
 */
@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    EventService eventService;

    @GetMapping("/overview")
    public ResponseEntity<?> reportForOverview() {
        try {
            return ResponseHandler.resBuilder("Lấy báo cáo sự kiện thành công", HttpStatus.OK, eventService.reportEvent());
        } catch (Exception ex) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra trong hệ thống", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
//    @GetMapping("/chart")
//    public ResponseEntity<?> reportForChart(
//            @RequestParam int year,
//            @RequestParam int month,
//            @RequestParam(defaultValue = "PAID") String status
//    ) {
//        try {
//            return ResponseHandler.resBuilder("Lấy báo cáo sự kiện thành công", HttpStatus.OK,eventService.getTotalPriceWithStatus(year,month,status) );
//        } catch (Exception ex) {
//            return ResponseHandler.resBuilder("Có lỗi xảy ra trong hệ thống", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
//        }
//    }

    @GetMapping("/charts")
    public ResponseEntity<?> reportForChartYear(
            @RequestParam int year
    ) {
        try {
            return ResponseHandler.resBuilder("Lấy báo cáo sự kiện thành công", HttpStatus.OK, eventService.getMonthlyTotalRevenueByStatus(year));
        } catch (Exception ex) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra trong hệ thống", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<?> getEventTicketSummary(@PathVariable String companyId) {

        try {
            return ResponseHandler.resBuilder("Lấy báo cáo sự kiện thành công", HttpStatus.OK, eventService.getEventTicketSummaryByCompanyId(companyId));
        } catch (Exception ex) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra trong hệ thống", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }


}
