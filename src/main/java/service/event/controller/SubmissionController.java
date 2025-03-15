/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.event.dto.SubmissionDTO;
import service.event.model.Submission;
import service.event.response.SubmissionResponse;
import service.event.services.EventService;
import service.event.services.SubmissionService;
import service.event.utils.ResponseHandler;

/**
 * @author ADMIN
 */
@RestController
@RequestMapping("/submissions")
public class SubmissionController {

    @Autowired
    EventService eventService;

    @Autowired
    SubmissionService submissionService;

    @PostMapping("/{eventId}")
    public ResponseEntity<?> createSubmission(
            @PathVariable("eventId") long eventId,
            @RequestBody SubmissionDTO submissionDTO) {
        try {
            submissionDTO.setEventId(eventId); // Gán eventId vào SubmissionDTO
            Submission createdSubmission = submissionService.createOrUpdateSubmission(submissionDTO);
            return ResponseHandler.resBuilder("Tạo submission thành công", HttpStatus.CREATED, createdSubmission);
        }
         catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getSubmissionsByEvent(@PathVariable("eventId") long eventId) {
        try {
            // Lấy danh sách submissions từ service
            Submission submissions = submissionService.getSubmissionByEventId(eventId);

            if (submissions == null) {
                return ResponseHandler.resBuilder("Không tìm thấy submissions cho event này", HttpStatus.NOT_FOUND, null);
            }

            // Trả về danh sách submissions
            return ResponseHandler.resBuilder("Danh sách submissions", HttpStatus.OK, submissions);

        } catch (Exception e) {
            // Trả về phản hồi lỗi chung
            return ResponseHandler.resBuilder("Lỗi: " + e.getMessage().substring(0, 20), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllSubmissions(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {

        try {
            if (size > 100) {
                size = 100; // Cap size at 100 to prevent large queries
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Submission> res = submissionService.getAllSubmissionsWithPageAble(pageable);
            Page<SubmissionResponse> responsePage = res.map(SubmissionResponse::new);

            return ResponseHandler.resBuilder("Danh sách đơn xét duyệt", HttpStatus.OK,responsePage);
        } catch (Exception e) {
            // Trả về phản hồi lỗi chung
            return ResponseHandler.resBuilder("Lỗi: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }

    }
    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> getAllSubmissionsByCompany(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @PathVariable String companyId) {

        try {
            if (size > 100) {
                size = 100; // Cap size at 100 to prevent large queries
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Submission> res = submissionService.getAllSubmissionsByComapanyWithPageAble(companyId,pageable);
            Page<SubmissionResponse> responsePage = res.map(SubmissionResponse::new);

            return ResponseHandler.resBuilder("Danh sách đơn xét duyệt", HttpStatus.OK,responsePage);
        } catch (Exception e) {
            // Trả về phản hồi lỗi chung
            return ResponseHandler.resBuilder("Lỗi: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }

    }

    // API xóa một Submission theo submissionId
    @DeleteMapping("/{submissionId}")
    public ResponseEntity<?> deleteSubmission(@PathVariable("submissionId") long submissionId) {
        try {
            // Xóa submission thông qua service
            boolean isDeleted = submissionService.deleteSubmission(submissionId);

            if (!isDeleted) {
                return ResponseHandler.resBuilder("Submission không tồn tại", HttpStatus.NOT_FOUND, null);
            }

            // Trả về phản hồi thành công
            return ResponseHandler.resBuilder("Xóa submission thành công", HttpStatus.NO_CONTENT, null);

        } catch (Exception e) {
            // Trả về phản hồi lỗi chung
            return ResponseHandler.resBuilder("Lỗi: " + e.getMessage().substring(0, 20), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/{submissionId}/confirm/{status}")
    public ResponseEntity<?> approvedSubmission(
            @PathVariable("status") String status,
            @PathVariable("submissionId") long submissionId) {
        try {
            status = status.toUpperCase(); // Chuẩn hóa status về chữ in hoa

            if ("APPROVED".equals(status)) {
                return ResponseHandler.resBuilder(
                        "Đã phê duyệt",
                        HttpStatus.OK,
                        submissionService.approvedSubmission(submissionId) // Sửa lỗi chính tả
                );
            } else if ("REJECTED".equals(status)) {
                return ResponseHandler.resBuilder(
                        "Đã từ chối",
                        HttpStatus.OK,
                        submissionService.declinedSubmission(submissionId)
                );
            } else {
                return ResponseHandler.resBuilder(
                        "Trạng thái không hợp lệ: " + status,
                        HttpStatus.BAD_REQUEST,
                        null
                );
            }
        } catch (Exception e) {
            return ResponseHandler.resBuilder(
                    "Lỗi: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }


}
