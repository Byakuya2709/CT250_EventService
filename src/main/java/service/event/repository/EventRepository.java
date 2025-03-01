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

    long countByEventStatus(String eventStatus);
    
    long countByEventCompanyId(String eventCompanyId);

    @Query(value = "SELECT * FROM event e WHERE (e.event_tags LIKE CONCAT('%|', :tag, '|%') OR e.event_tags LIKE CONCAT(:tag, '|%') OR e.event_tags LIKE CONCAT('%|', :tag) OR e.event_tags = :tag) AND e.event_status = :status LIMIT 5", nativeQuery = true)
    List<Event> findByEventTagAndEventStatus(@Param("tag") String tag, @Param("status") String status);

    @Query(value = """
        SELECT e.*
        FROM event e
        LEFT JOIN event_rating_start ers ON e.event_id = ers.event_id
        GROUP BY e.event_id
        ORDER BY 
            SUM(ers.star_rating * ers.rating_count) / NULLIF(SUM(ers.rating_count), 0) DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<Event> findTopRatedEvents(@Param("limit") int limit);

//    @Query("SELECT COUNT(e) FROM EventTicket e WHERE e.event.eventCompanyId = :companyId")
//    long countTotalTicketsByCompanyId(@Param("companyId") String companyId);
//
//    @Query("SELECT COALESCE(SUM(e.ticketPrice), 0) FROM EventTicket e WHERE e.event.eventCompanyId = :companyId")
//    Double sumTotalRevenueByCompanyId(@Param("companyId") String companyId);


    @Query("SELECT e.eventStatus, COUNT(e) FROM Event e GROUP BY e.eventStatus")
    List<Object[]> countEventsByStatus();

    @Query("SELECT new service.event.dto.EventStatsDTO(e.event.eventId,e.event.eventTitle,e.event.eventPrice, COUNT(e), COALESCE(SUM(e.ticketPrice), 0)) FROM EventTicket e WHERE e.event.eventCompanyId = :companyId GROUP BY e.event.eventId")
    List<EventStatsDTO> getEventTicketStatisticsByCompanyId(@Param("companyId") String companyId);
    
    
}
