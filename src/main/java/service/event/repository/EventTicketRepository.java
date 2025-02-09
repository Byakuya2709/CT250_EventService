/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service.event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.event.model.Event;
import service.event.model.EventTicket;

/**
 *
 * @author admin
 */
@Repository
public interface EventTicketRepository extends JpaRepository<EventTicket,Long> {
        List<EventTicket> findByEvent(Event event);
        List<EventTicket> findByEventAndTicketDay(Event event, Integer day);
        List<EventTicket> findByEventAndTicketStatus(Event event, EventTicket.TicketStatus status);
        List<EventTicket> findByEventAndTicketUserId(Event event, String userId);

}
