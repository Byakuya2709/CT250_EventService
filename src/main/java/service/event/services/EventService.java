/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.services;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.event.dto.EventDTO;
import service.event.model.Event;
import service.event.model.EventTicket;
import service.event.repository.EventRepository;
import service.event.repository.EventTicketRepository;

/**
 *
 * @author admin
 */
@Service
public class EventService {

    public class EventTicketService {

        @Autowired
        EventRepository eventRepository;
        
        @Autowired
        EventTicketRepository eventTicketRepository;
        // Phương thức để đặt vé cho sự kiện

        public void bookTicket(Long eventId, String position, String ticketType, Date eventDate) {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

            // Kiểm tra trạng thái vé
//            EventTicket ticket = eventTicketRepository.findByEventAndPosition(event, position);
//
//            if (ticket == null || !ticket.getTicketStatus().equals("AVAILABLE")) {
//                throw new TicketNotAvailableException("Ticket is not available");
//            }
//
//            // Cập nhật trạng thái vé và thông tin vé
//            ticket.setTicketStatus("BOOKED");
//            ticket.setTicketValidity("ACTIVE");
//            ticket.setTicketDayActive(ticketType.equals("SINGLE_DAY") ? eventDate : event.getEventStartDate());
//            eventTicketRepository.save(ticket);
//
//            // Thực hiện các thao tác khác (Thanh toán, v.v.)
        }
        
        public Event saveEvent(EventDTO eventDTO){
            Event event = new Event();
            
            
            eventRepository.save(event);
            return event;
        }
    }

}
