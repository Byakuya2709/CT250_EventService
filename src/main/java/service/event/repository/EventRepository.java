/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service.event.repository;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import service.event.dto.EventStatsDTO;
import service.event.model.Event;
import service.event.model.EventSummary;

/**
 *
 * @author admin
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAll(Pageable pageable);

    List<EventSummary> findAllByEventCompanyId(String eventCompanyId);

    Page<EventSummary> findByEventStatus(String eventStatus, Pageable pageable);

    long count();

    long countByEventCompanyId(String eventCompanyId);

    @Query("SELECT COUNT(e) FROM EventTicket e WHERE e.event.eventCompanyId = :companyId")
    long countTotalTicketsByCompanyId(@Param("companyId") String companyId);

    @Query("SELECT COALESCE(SUM(e.ticketPrice), 0) FROM EventTicket e WHERE e.event.eventCompanyId = :companyId")
    Double sumTotalRevenueByCompanyId(@Param("companyId") String companyId);
    
    @Query("SELECT new service.event.dto.EventStatsDTO(e.event.eventId,e.event.eventTitle,e.event.eventPrice, COUNT(e), COALESCE(SUM(e.ticketPrice), 0)) FROM EventTicket e WHERE e.event.eventCompanyId = :companyId GROUP BY e.event.eventId")
    List<EventStatsDTO> getEventTicketStatisticsByCompanyId(@Param("companyId") String companyId);
}
