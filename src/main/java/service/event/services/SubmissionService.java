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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Submission createOrUpdateSubmission(SubmissionDTO submissionDTO) {
        if (submissionDTO.getEventId() == null || submissionDTO.getSubSubject() == null
                || submissionDTO.getSubCreateDate() == null || submissionDTO.getSubDeadline() == null) {
            throw new IllegalArgumentException("Thiếu thông tin bắt buộc trong SubmissionDTO.");
        }

        Event event = eventRepository.findById(submissionDTO.getEventId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện với ID: " + submissionDTO.getEventId()));


        if (event.getContract() != null) {
            Submission oldSubmission = event.getContract();
            if ("AWAITING_APPROVAL".equals(event.getEventStatus())) {
                throw new IllegalArgumentException("Submission trước đó đang chờ phê duyệt, không thể tạo submission mới.");
            }
            event.setContract(null);
            eventRepository.save(event); // Cập nhật event trước khi xóa submission

            submissionRepository.delete(oldSubmission);
            submissionRepository.flush(); // Đảm bảo submission cũ bị xóa ngay lập tức
        }

        // Kiểm tra deadline có hợp lệ không
        Date createDate = DateUtils.convertStringToDate(submissionDTO.getSubCreateDate());
        Date deadline = DateUtils.convertStringToDate(submissionDTO.getSubDeadline());
        if (deadline.before(createDate)) {
            throw new IllegalArgumentException("Deadline của Submission không thể nhỏ hơn ngày tạo.");
        }

        // Tạo mới Submission
        Submission submission = new Submission();
        submission.setEvent(event);
        submission.setSubSubject(submissionDTO.getSubSubject());
        submission.setSubCreateDate(createDate);
        submission.setSubFinishDate(null);
        submission.setSubDeadline(deadline);
        submission.setSubFormdata(submissionDTO.getSubFormdata());
        submission.setSubCompanyId(submissionDTO.getSubCompanyId());
        submission.setSubStatus(submissionDTO.getSubStatus() != null ? submissionDTO.getSubStatus() : "PENDING");
        submission.setSubContent(submissionDTO.getSubContent());
        submission.setSubCompanyName(submissionDTO.getSubCompanyName());



        // Lưu Submission mới
        Submission savedSubmission = submissionRepository.save(submission);

        // Gán Submission mới cho Event
        event.setContract(savedSubmission);

        return savedSubmission;
    }


    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public Page<Submission> getAllSubmissionsWithPageAble(Pageable pageable) {
        return submissionRepository.findAll(pageable);
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


    public Submission approvedSubmission(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found for id: " + submissionId));

        submission.setSubFinishDate(new Date());

        // Nếu deadline đã qua, tự động từ chối submission
        if (submission.getSubDeadline().before(new Date())) {
            submission.setSubStatus("REJECTED");
        } else {
            String subject = submission.getSubSubject().trim();


            if (subject.equals("Yêu Cầu Hủy Bỏ Sự Kiện")) {
                if (submission.getEvent() != null) {
                    submission.getEvent().setEventStatus("CANCELLED");
                }
                submission.setSubStatus("COMPLETED");

            } else if (subject.equals("Yêu Cầu Cập Nhật Giá Sự Kiện")) {
                submission.setSubStatus("COMPLETED");

                if (submission.getEvent() != null) {
                    try {
                        double newPrice = Double.parseDouble(submission.getSubFormdata());
                        submission.getEvent().setEventPrice(newPrice);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid price format in subFormdata");
                    }
                }
            } else {
                submission.setSubStatus("COMPLETED");

                if (submission.getEvent() != null) {
                    submission.getEvent().setEventStatus("APPROVED");
                }
            }
        }

        return submissionRepository.save(submission);
    }



    public Submission declinedSubmission(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found for id: " + submissionId));

        String subject = submission.getSubSubject().trim();

        submission.setSubStatus("REJECTED");
        submission.setSubFinishDate(new Date());
        if(subject.equals("Tường trình đề nghị phê duyệt sự kiện")){
            submission.getEvent().setEventStatus("CANCELLED");
        }

        return submissionRepository.save(submission);
    }

}
