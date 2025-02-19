/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.event.dto.SubmissionDTO;
import service.event.model.Submission;
import service.event.services.EventService;
import service.event.services.SubmissionService;
import service.event.utils.ResponseHandler;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/submissions")
public class SubmissionController {

    @Autowired
    EventService eventService;
    
    @Autowired
    SubmissionService submissionService;

    @PostMapping("/{eventId}/create")

    public ResponseEntity<?> createSubmission(
            @PathVariable("eventId") long eventId,
            @RequestBody SubmissionDTO submissionDTO) {
        try {
            submissionDTO.setEventId(eventId); // Gán eventId vào SubmissionDTO
            Submission createdSubmission = submissionService.createSubmission(submissionDTO);
            return ResponseHandler.resBuilder("Tạo submission thành công", HttpStatus.CREATED, createdSubmission);
        } catch (IllegalArgumentException e) {
            return ResponseHandler.resBuilder("Lỗi:" + e.getMessage().substring(0, 20), HttpStatus.INTERNAL_SERVER_ERROR, null);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Lỗi:" + e.getMessage().substring(0, 20), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getSubmissionsByEvent(@PathVariable("eventId") long eventId) {
        try {
            // Lấy danh sách submissions từ service
            Submission submissions = submissionService.getSubmissionByEventId(eventId);

            if (submissions==null) {
                return ResponseHandler.resBuilder("Không tìm thấy submissions cho event này", HttpStatus.NOT_FOUND, null);
            }

            // Trả về danh sách submissions
            return ResponseHandler.resBuilder("Danh sách submissions", HttpStatus.OK, submissions);

        } catch (Exception e) {
            // Trả về phản hồi lỗi chung
            return ResponseHandler.resBuilder("Lỗi: " + e.getMessage().substring(0, 20), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // API xóa một Submission theo submissionId
    @DeleteMapping("/{submissionId}/delete")
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

}
