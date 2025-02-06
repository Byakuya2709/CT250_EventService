/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.event.exceptions.CapacityExceededException;
import service.event.exceptions.EventNotFoundException;
import service.event.model.Event;
import service.event.model.EventTicket;
import service.event.model.EventTicketCapacity;
import service.event.repository.EventRepository;
import service.event.repository.EventTicketCapacityRepository;
import service.event.repository.EventTicketRepository;
import service.event.request.BookingRequest;

/**
 *
 * @author admin
 */
@Service
public class TicketService {

    @Autowired
    private EventTicketRepository eventTicketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventTicketCapacityRepository eventTicketCapacityRepository;

    @Autowired
    private EventTicketCapacityRepository eventTicketTotalCapacityRepository; // Thêm repository cho tổng số vé

    public EventTicket bookTicket(BookingRequest request) throws Exception {
        // Kiểm tra xem sự kiện có tồn tại không
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        // Xác định loại vé (SINGLE_DAY hoặc ALL_DAYS)
        EventTicket.TicketDay ticketDuration;
        try {
            ticketDuration = EventTicket.TicketDay.valueOf(request.getTicketDuration().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ticket duration type: " + request.getTicketDuration());
        }

        // Tạo đối tượng vé mới và gán thông tin cơ bản
        EventTicket eventTicket = new EventTicket();
        eventTicket.setEvent(event);
        eventTicket.setTicketUserId(request.getUserId());
        eventTicket.setTicketPrice(request.getTicketPrice());
        eventTicket.setTicketPosition(request.getTicketPosition());
        eventTicket.setTicketDuration(ticketDuration);
        eventTicket.setTicketStatus(EventTicket.TicketStatus.UNPAID.toString());
        eventTicket.setTicketValidity(EventTicket.TicketValidity.INACTIVE.toString());

        // Xử lý theo từng loại vé
        if (ticketDuration == EventTicket.TicketDay.SINGLE_DAY) {
            // Với vé SINGLE_DAY, phải có thông tin về ngày
            if (request.getDay() == null || request.getDay().isEmpty()) {
                throw new IllegalArgumentException("Day must be provided for single day tickets.");
            }

            int dayNumber;
            try {
                dayNumber = Integer.parseInt(request.getDay());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid day provided: " + request.getDay());
            }

            // Lấy thông tin về khả năng vé của ngày tương ứng
            EventTicketCapacity ticketCapacity = eventTicketCapacityRepository.findByEventAndDay(event, dayNumber);
            if (ticketCapacity == null || ticketCapacity.getRemainingCapacity() <= 0) {
                throw new CapacityExceededException("No remaining capacity for day: " + dayNumber);
            }

            // Giảm số vé còn lại và cập nhật
            ticketCapacity.setRemainingCapacity(ticketCapacity.getRemainingCapacity() - 1);
            eventTicketCapacityRepository.save(ticketCapacity);

            // Tính toán ngày active cho vé dựa vào ngày bắt đầu sự kiện
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(event.getEventStartDate());
            calendar.add(Calendar.DAY_OF_YEAR, dayNumber - 1);
            eventTicket.setTicketDayActive(calendar.getTime());
        } else if (ticketDuration == EventTicket.TicketDay.ALL_DAYS) {
            // Với vé ALL_DAYS, kiểm tra tổng số vé cho sự kiện
            List<EventTicketCapacity> totalCapacity = eventTicketTotalCapacityRepository.findByEvent(event);
            if (totalCapacity == null || totalCapacity.isEmpty()) {
                throw new CapacityExceededException("No remaining capacity for all-day tickets.");
            }
            
            // Duyệt qua tất cả các ngày và kiểm tra số vé còn lại
            for (EventTicketCapacity capacityDay : totalCapacity) {
                if (capacityDay.getRemainingCapacity() <= 0) {
                    throw new CapacityExceededException("No remaining capacity for all-day tickets.");
                } else {
                    // Giảm số vé còn lại cho từng ngày
                    capacityDay.setRemainingCapacity(capacityDay.getRemainingCapacity() - 1);
                    eventTicketCapacityRepository.save(capacityDay); // Lưu từng thay đổi cho ngày
                }
            }

            // Ngày active mặc định là ngày bắt đầu sự kiện
            eventTicket.setTicketDayActive(event.getEventStartDate());
        } else {
            // Trường hợp dự phòng (nếu có loại vé khác)
            eventTicket.setTicketDayActive(event.getEventStartDate());
        }

        // Thiết lập thời gian booking là thời điểm hiện tại
        eventTicket.setTicketBookingTime(new Date());

        // Lưu vé vào cơ sở dữ liệu và trả về đối tượng đã lưu
        return eventTicketRepository.save(eventTicket);
    }
}



