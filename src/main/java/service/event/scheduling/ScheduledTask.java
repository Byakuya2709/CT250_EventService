package service.event.scheduling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import service.event.model.Event;
import service.event.model.EventTicket;
import service.event.model.Submission;
import service.event.repository.EventRepository;
import service.event.repository.EventTicketRepository;
import service.event.repository.SubmissionRepository;

@Component
public class ScheduledTask {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private EventTicketRepository eventTicketRepository;

    
//    @Scheduled(cron = "0 */2 * * * ?") // Chạy mỗi 2 phút
//@Scheduled(cron = "0 0 1 ? * MON")  1tuan 1 lan
//@Scheduled(cron = "0 0 1 1 * ?") 1 thang 1 lan luc 1AM


    @Scheduled(cron = "0 0 1 * * ?") // Chạy lúc 01:00 AM hàng ngày
//    @Scheduled(cron = "0 */2 * * * ?") // Chạy mỗi 2 phút
    @Transactional
    public void updateStatuses() {
        LocalDate today = LocalDate.now();
        Date todayDate = convertToDate(today);

        int rejectedSubCount = updateExpiredSubmissions(todayDate);
        int cancelledEventCount = updateExpiredEvents(todayDate);
        int completedEventCount = updateCompletedEvents(todayDate);

        System.out.println("Scheduled Task Executed: REJECTED Submissions: " + rejectedSubCount
                + ", CANCELLED Events: " + cancelledEventCount + ", COMPLETED Events: " + completedEventCount);
    }

    private int updateExpiredSubmissions(Date todayDate) {
//        Kiểm tra các bài duyệt (Submission) có trạng thái "PENDING" (đang chờ) nhưng đã qua deadline → REJECTED.

        List<Submission> expiredSubmissions = submissionRepository.findBySubStatusAndSubDeadlineBefore("PENDING", todayDate);
        if (expiredSubmissions.isEmpty()) {
            return 0;
        }

        for (Submission submission : expiredSubmissions) {
            submission.setSubStatus("REJECTED");
            System.out.println("Submission REJECTED: " + submission.getSubmissionId());
        }

        submissionRepository.saveAll(expiredSubmissions);
        return expiredSubmissions.size();
    }

    private int updateExpiredEvents(Date todayDate) {
//        Lấy danh sách event có trạng thái "AWAITING_APPROVAL" nhưng đã qua ngày bắt đầu.
        List<Event> expiredEvents = eventRepository.findByEventStatusAndEventStartDateBefore("AWAITING_APPROVAL", todayDate);
        if (expiredEvents.isEmpty()) {
            return 0;
        }
//Cập nhật trạng thái các event này thành "CANCELLED".
        for (Event event : expiredEvents) {
            event.setEventStatus("CANCELLED");
            System.out.println("Event CANCELLED: " + event.getEventId());
        }

        eventRepository.saveAll(expiredEvents);
        return expiredEvents.size();
    }

    private int updateCompletedEvents(Date todayDate) {
//        Kiểm tra các sự kiện có trạng thái "UP_COMMING" (sắp diễn ra) nhưng đã qua ngày kết thúc → COMPLETED.
        List<Event> completedEvents = eventRepository.findByEventStatusAndEventEndDateBefore("UP_COMMING", todayDate);
        if (completedEvents.isEmpty()) {
            return 0;
        }

        for (Event event : completedEvents) {
            event.setEventStatus("COMPLETED");
            System.out.println("Event COMPLETED: " + event.getEventId());
        }

        eventRepository.saveAll(completedEvents);
        return completedEvents.size();
    }

    @Scheduled(cron = "0 0 3 * * ?") // Chạy lúc 03:00 AM hàng ngày
    public void checkAndUpdateTicketStatus() {
        LocalDate today = LocalDate.now();
        Date now = new Date();

        List<EventTicket> tickets = eventTicketRepository.findAll();
        int updatedCount = 0;

        for (EventTicket ticket : tickets) {
            if (updateTicketStatus(ticket, today, now)) {
                eventTicketRepository.save(ticket);
                updatedCount++;
            }
        }

    }

    private boolean updateTicketStatus(EventTicket ticket, LocalDate today, Date now) {
        boolean isUpdated = false;

        if ("UNPAID".equals(ticket.getTicketStatus()) && ticket.getTicketDayActive() != null
                && ticket.getTicketDayActive().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(today)) {
            ticket.setTicketStatus("CANCELLED");
            isUpdated = true;
        }

        if ("PAID".equals(ticket.getTicketStatus()) && ticket.getTicketDayActive() != null
                && ticket.getTicketDayActive().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(today)) {
            ticket.setTicketValidity("VALID");
            isUpdated = true;
        }

        if (ticket.getTicketExpiredTime() != null && ticket.getTicketExpiredTime().before(now)) {
            ticket.setTicketValidity("EXPIRED");
            isUpdated = true;
        }

        return isUpdated;
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
