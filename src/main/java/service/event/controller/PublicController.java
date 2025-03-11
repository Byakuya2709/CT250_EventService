package service.event.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.event.dto.EventProjection;
import service.event.exceptions.EventNotFoundException;
import service.event.model.Event;
import service.event.response.OneEventResponse;
import service.event.services.EventService;
import service.event.utils.ResponseHandler;
import service.event.utils.TextUtils;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    EventService eventService;

    @GetMapping("/search-text")
    public ResponseEntity<?> searchEventByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) { // Thêm size và giá trị mặc định
        try {
            String keywordNoAccent = TextUtils.removeAccents(keyword);
            System.out.println(keywordNoAccent);
            Page<Event> res = eventService.searchEvents(keywordNoAccent, page, size);

            return ResponseHandler.resBuilder("Lấy danh sách sự kiện thành công", HttpStatus.OK, res);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy event: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }



    @GetMapping("/tag")
    public ResponseEntity<?> getEventByTag(@RequestParam String tag, @RequestParam(defaultValue = "UP_COMMING") String status) {
        try {
            List<Event> res = eventService.get5ByTag(tag,status);
            if (res.isEmpty()) {
                return ResponseHandler.resBuilder("Không tồn tạisự kiện có tag này", HttpStatus.OK, null);
            }

            return ResponseHandler.resBuilder("Lấy danh sách sự kiện thành công", HttpStatus.OK, res);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy event" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
    
   

    @GetMapping("/top-rated")
    public ResponseEntity<List<Event>> getTopRatedEvents(@RequestParam(defaultValue = "5") int limit) {
        try{
            return ResponseEntity.ok(eventService.getTopRatedEvents(limit));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
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
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy sự kiện: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/current-month")
    public ResponseEntity<?> getEventsThisMonth() {
        try{
            List<EventProjection> events = eventService.getEventsInCurrentMonth();
            return ResponseHandler.resBuilder("Lấy thông tin sự kiện thành công", HttpStatus.OK,events);
        }catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy sự kiện: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> searchEvents(
            @RequestParam(required = false) String companyId,
            @RequestParam(required = false) String eventStatus,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        try{
            companyId = null;
            Pageable pageable = PageRequest.of(page, size);
            Page<?> result = eventService.searchEvents(companyId, eventStatus, month, year, pageable);
            return ResponseHandler.resBuilder("Lấy danh sách sự kiện thành công", HttpStatus.OK, result);
        }catch (Exception e ){
            return ResponseHandler.resBuilder("Lỗi xảy ra trong quá trình lấy event" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }


    }
}
