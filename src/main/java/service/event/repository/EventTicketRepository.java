/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service.event.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import service.event.model.Event;
import service.event.model.EventTicket;

/**
 *
 * @author admin
 */
@Repository
public interface EventTicketRepository extends JpaRepository<EventTicket, Long> {

    List<EventTicket> findByEvent(Event event);

    List<EventTicket> findByTicketUserId(String ticketUserId); // Lấy danh sách vé theo user

    List<EventTicket> findByEventAndTicketDay(Event event, Integer day);

    List<EventTicket> findByEventAndTicketStatus(Event event, EventTicket.TicketStatus status);

    List<EventTicket> findByEventAndTicketUserId(Event event, String userId);

//    @Query("SELECT e FROM EventTicket e WHERE e.ticketDate BETWEEN :startDate AND :endDate")
//    List<EventTicket> findTicketsByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    long countByEvent(Event event); // Đếm số vé của sự kiện
    
    @Query("SELECT SUM(e.ticketPrice) FROM EventTicket e WHERE e.event = :event")
    Double sumTicketPriceByEvent(@Param("event") Event event); // Tổng giá vé theo sự kiện

}
