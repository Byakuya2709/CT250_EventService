/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.event.dto.SubmissionDTO;
import service.event.model.Event;
import service.event.model.Submission;
import service.event.repository.EventRepository;
import service.event.repository.SubmissionRepository;
import service.event.utils.DateUtils;

/**
 *
 * @author ADMIN
 */
@Service
public class SubmissionService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Transactional
    public Submission createSubmission(SubmissionDTO submissionDTO) {
        if (submissionDTO.getEventId() == null || submissionDTO.getSubSubject() == null
                || submissionDTO.getSubCreateDate() == null || submissionDTO.getSubDeadline() == null) {
            throw new IllegalArgumentException("Missing required fields in SubmissionDTO");
        }
        Event event = eventRepository.findById(submissionDTO.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not existed with ID: " + submissionDTO.getEventId()));

        // Kiểm tra deadline có hợp lệ không
        Date createDate = DateUtils.convertStringToDate(submissionDTO.getSubCreateDate());
        Date deadline = DateUtils.convertStringToDate(submissionDTO.getSubDeadline());
        if (deadline.before(createDate)) {
            throw new IllegalArgumentException("Submission deadline cannot be before the creation date.");
        }

        // Tạo Submission mới
        Submission submission = new Submission();
        submission.setEvent(event);
        submission.setSubSubject(submissionDTO.getSubSubject());
        submission.setSubCreateDate(createDate);
        submission.setSubFinishDate(null); // Mặc định là null khi chưa hoàn thành
        submission.setSubDeadline(deadline);
        submission.setSubCompanyId(submissionDTO.getSubCompanyId());
        submission.setSubStatus(submissionDTO.getSubStatus());
        submission.setSubContent(submissionDTO.getSubContent());
        submission.setSubCompanyName(submissionDTO.getSubCompanyName());

        // Liên kết Submission với Event
        event.setContract(submission);

        // Lưu Submission vào cơ sở dữ liệu
        return submissionRepository.save(submission);
    }

    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public Submission getSubmissionById(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Submission với ID: " + id));
    }

    public Submission updateSubmission(Long id, Submission updatedSubmission) {
        Submission existingSubmission = submissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Submission với ID: " + id));

        // Cập nhật các trường cần thiết
        existingSubmission.setSubSubject(updatedSubmission.getSubSubject());
        existingSubmission.setSubCreateDate(updatedSubmission.getSubCreateDate());
        existingSubmission.setSubFinishDate(updatedSubmission.getSubFinishDate());
        existingSubmission.setSubStatus(updatedSubmission.getSubStatus());
        existingSubmission.setSubDeadline(updatedSubmission.getSubDeadline());
        existingSubmission.setSubContent(updatedSubmission.getSubContent());
        existingSubmission.setSubCompanyId(updatedSubmission.getSubCompanyId());
        existingSubmission.setSubCompanyName(updatedSubmission.getSubCompanyName());

        return submissionRepository.save(existingSubmission);
    }

    public boolean deleteSubmission(long submissionId) {
        Optional<Submission> submission = submissionRepository.findById(submissionId);
        if (submission.isPresent()) {
            submissionRepository.delete(submission.get());
            return true;
        }
        return false;
    }

    public Submission getSubmissionByEventId(Long eventId) {
        return submissionRepository.findByEvent_EventId(eventId)
                .orElseThrow(() -> new RuntimeException("Submission not found for event id: " + eventId));
    }
}
