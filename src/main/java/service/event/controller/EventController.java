/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.event.dto.EventDTO;
import service.event.dto.EventStatsDTO;
import service.event.dto.SubmissionDTO;
import service.event.exceptions.EventNotFoundException;
import service.event.exceptions.FailedUpdateEventEx;
import service.event.model.Event;
import service.event.model.EventSummary;
import service.event.model.EventTicket;
import service.event.model.EventTicketZone;
import service.event.model.Submission;
import service.event.request.UpdatedZoneRequest;
import service.event.response.OneEventResponse;
import service.event.services.EventService;
import service.event.services.SubmissionService;
import service.event.services.TicketService;
import service.event.utils.ResponseHandler;

/**
 * @author ADMIN
 */
@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    EventService eventService;
    @Autowired
    TicketService eventTicketService;

    @Autowired
    SubmissionService submissionService;

//    @PostMapping("")
//    public ResponseEntity<?> saveEvent(@RequestBody EventDTO eventDTO) {
//        try {
//            // Gọi service để lưu sự kiện
//            Event savedEvent = eventService.saveEvent(eventDTO);
//
//            // Trả về response thành công với sự kiện đã lưu
//            return ResponseHandler.resBuilder("Tạo event thành công", HttpStatus.CREATED, savedEvent);
//        } catch (Exception e) {
//            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình tạo event" + e.getMessage().substring(0, 100), HttpStatus.CREATED, null);
//        }
//        // Xử lý lỗi khi không thể parse ngày tháng
//
//    }

    @PostMapping("")
    public ResponseEntity<?> testEvent(@RequestBody EventDTO eventDTO) {
        try {
            // Gọi service để lưu sự kiện
            Event savedEvent = eventService.saveEvent(eventDTO);
            if (savedEvent != null) {

                SubmissionDTO submissionDTO = new SubmissionDTO();

                submissionDTO.setSubSubject("Tường trình đề nghị phê duyệt sự kiện");

                Date now = new Date();
                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                isoFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

                String formattedDate = isoFormat.format(now);

                submissionDTO.setSubCreateDate(formattedDate);
                submissionDTO.setSubFinishDate(null);
                submissionDTO.setSubStatus("PENDING");
                // Cộng thêm 2 ngày
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                calendar.add(Calendar.DAY_OF_MONTH, 5);
                Date deadlineDate = calendar.getTime();

                // Chuyển deadline thành String
                String formattedDeadline = isoFormat.format(deadlineDate);

                submissionDTO.setSubDeadline(formattedDeadline);
                submissionDTO.setSubContent("Chúng tôi kính trình lên ban quản trị đề nghị phê duyệt sự kiện theo kế hoạch đã đề xuất. Sự kiện này bao gồm bài thuyết trình chi tiết, các công tác chuẩn bị cần thiết và các yêu cầu hỗ trợ từ ban tổ chức. Kính mong nhận được sự xem xét và phê duyệt từ quý ban.");
                submissionDTO.setEventId(savedEvent.getEventId());
                submissionDTO.setSubFormdata("0");
                submissionDTO.setSubCompanyId(savedEvent.getEventCompanyId());
                submissionDTO.setSubCompanyName(eventDTO.getEventCompanyName());

                submissionService.createOrUpdateSubmission(submissionDTO);
            }

            // Trả về response thành công với sự kiện đã lưu
            return ResponseHandler.resBuilder("Tạo event thành công", HttpStatus.CREATED, savedEvent);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình tạo event" + e.getMessage(), HttpStatus.CREATED, null);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllEvent(
            @RequestParam(defaultValue = "1") int page,
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
                return ResponseHandler.resBuilder("Lấy thông tin sự kiện thành công", HttpStatus.OK, OneEventResponse.toEventResponse(event));
            } else {
                return ResponseHandler.resBuilder("Không tìm thấy sự kiện", HttpStatus.NOT_FOUND, null);
            }
        } catch (EventNotFoundException e) {
            return ResponseHandler.resBuilder("Sự kiện không tồn tại: " + e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy sự kiện: " + e.getMessage().substring(0, 100), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> getEventByCompanyId(@PathVariable String companyId) {
        try {
            List<EventSummary> eventList = eventService.getAllEventsByCompanyId(companyId);

            if (eventList.isEmpty()) {
                return ResponseHandler.resBuilder("Không tìm thấy sự kiện", HttpStatus.NOT_FOUND, null);
            }

            return ResponseHandler.resBuilder("Lấy thông tin sự kiện thành công", HttpStatus.OK, eventList);
        } catch (Exception e) {
            return ResponseHandler.resBuilder(
                    "Lỗi xảy ra trong quá trình lấy sự kiện: " + e.getMessage().substring(0, 100),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }

    @GetMapping("/status/{eventStatus}")
    public ResponseEntity<?> getAllEventByCompanyId(
            @PathVariable String eventStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (size > 100) {
                size = 100; // Cap size at 100 to prevent large queries
            }
            Pageable pageable = PageRequest.of(page, size);

            Page<EventSummary> eventList = eventService.getAllEventSummary(eventStatus, pageable);

            if (eventList.isEmpty()) {
                return ResponseHandler.resBuilder("Không tìm thấy sự kiện", HttpStatus.NOT_FOUND, null);
            }

            return ResponseHandler.resBuilder("Lấy thông tin sự kiện thành công", HttpStatus.OK, eventList);
        } catch (EventNotFoundException e) {
            return ResponseHandler.resBuilder("Sự kiện không tồn tại: " + e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            return ResponseHandler.resBuilder(
                    "Lỗi xảy ra trong quá trình lấy sự kiện: " + e.getMessage().substring(0, 100),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
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

    @PatchMapping("/zones/{eventID}")
    public ResponseEntity<?> updateEventZone(@PathVariable Long eventID, @RequestBody List<UpdatedZoneRequest> req) {
        try {
            List<EventTicketZone> res = eventService.updateZone(eventID, req);
            return ResponseHandler.resBuilder("Cập nhật zone sự kiện thành công", HttpStatus.OK, res);
        } catch (EventNotFoundException e) {
            return ResponseHandler.resBuilder("Lỗi: " + e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (FailedUpdateEventEx ex) {
            return ResponseHandler.resBuilder("Lỗi cập nhật: " + ex.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (DataAccessException ex) {
            return ResponseHandler.resBuilder("Lỗi database: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi hệ thống: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

//    @GetMapping("/tickets/{eventID}")
//    public ResponseEntity<?> getTicketByEventId(@PathVariable Long eventID) {
//        try {
//            List<EventTicket> res = eventTicketService.getAllTicketByEvent(eventID);
//
//            if (res == null || res.isEmpty()) {
//                return ResponseHandler.resBuilder("Không tìm thấy sự kiện hoặc chưa có vé nào được đặt", HttpStatus.OK, null);
//            }
//
//            return ResponseHandler.resBuilder("Lấy thông tin tất cả vé thành công", HttpStatus.OK, res);
//
//        } catch (Exception e) {
//            String errorMessage = e.getMessage().length() > 100 ? e.getMessage().substring(0, 100) : e.getMessage();
//            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy vé đã đặt: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, null);
//        }
//    }
    @GetMapping("/tickets/{eventID}")
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

    @GetMapping("/tickets/{eventID}/all")
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

    @GetMapping("/tickets/{eventID}/count-quantity")
    public ResponseEntity<?> countTicketsByEvent(@PathVariable Long eventID) {
        try {
            Long res = eventTicketService.countTicketsByEvent(eventID);
            return ResponseHandler.resBuilder("Lấy thông tin tổng số vé của sự kiện thành công", HttpStatus.OK, res);

        } catch (EventNotFoundException e) {
            return ResponseHandler.resBuilder("Sự kiện không tồn tại: " + e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            String errorMessage = e.getMessage().length() > 100 ? e.getMessage().substring(0, 100) : e.getMessage();
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy  thông tin: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/tickets/{eventID}/count-sumprice")
    public ResponseEntity<?> getTotalTicketPriceByEvent(@PathVariable Long eventID) {
        try {
            Double res = eventTicketService.getTotalTicketPriceByEvent(eventID);
            return ResponseHandler.resBuilder("Lấy thông tin tổng doanh thu vé của sự kiện thành công", HttpStatus.OK, res);

        } catch (EventNotFoundException e) {
            return ResponseHandler.resBuilder("Sự kiện không tồn tại: " + e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            String errorMessage = e.getMessage().length() > 100 ? e.getMessage().substring(0, 100) : e.getMessage();
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy thông tin: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
//getAllZone

    @GetMapping("/zones/{eventID}")
    public ResponseEntity<?> getAllZoneByEvent(@PathVariable Long eventID) {
        try {
            List<EventTicketZone> res = eventService.getAllZoneByEvent(eventID);
            return ResponseHandler.resBuilder("Lấy thông tin zone sự kiện thành công", HttpStatus.OK, res);

        } catch (EventNotFoundException e) {
            return ResponseHandler.resBuilder("Sự kiện không tồn tại: " + e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            String errorMessage = e.getMessage().length() > 100 ? e.getMessage().substring(0, 100) : e.getMessage();
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy thông tin: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/{CompanyId}/count-event")
    public ResponseEntity<?> countByEventCompanyId(@PathVariable String CompanyId) {
        try {
            Long res = eventService.countByEventCompanyId(CompanyId);
            return ResponseHandler.resBuilder("Lấy thông tin tổng số sự kiện đã tổ chức của 1 công ty thành công", HttpStatus.OK, res);

        } catch (Exception e) {
            String errorMessage = e.getMessage().length() > 100 ? e.getMessage().substring(0, 100) : e.getMessage();
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy thông tin: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/{CompanyId}/statistic")
    public ResponseEntity<?> getEventTicketStatisticsByCompanyId(@PathVariable String CompanyId) {
        try {
            List<EventStatsDTO> stats = eventService.getEventTicketStatisticsByCompanyId(CompanyId);
            return ResponseHandler.resBuilder("Lấy thống kê sự kiện thành công", HttpStatus.OK, stats);

        } catch (Exception e) {
            String errorMessage = e.getMessage().length() > 100 ? e.getMessage().substring(0, 100) : e.getMessage();
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy thông tin: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

}
