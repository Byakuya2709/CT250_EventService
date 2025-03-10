/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service.event.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import service.event.dto.EventProjection;
import service.event.dto.EventStatsDTO;
import service.event.model.Event;
import service.event.model.EventSummary;

/**
 *
 * @author admin
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE LOWER(e.eventTitleNormalized) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Event> searchByVietnameseKeyword(@Param("keyword") String keyword, Pageable pageable);


    Page<Event> findAll(Pageable pageable);

    List<EventSummary> findAllByEventCompanyId(String eventCompanyId);

    Page<EventSummary> findByEventStatus(String eventStatus, Pageable pageable);

    Page<EventSummary> findByEventCompanyId(String eventCompanyId,Pageable pageable);

    // Tìm theo companyId, eventStatus, eventStartDate (Có phân trang)
    Page<EventSummary> findByEventCompanyIdAndEventStatus(
            String companyId, String eventStatus, Pageable pageable);


    @Query("SELECT e FROM Event e WHERE MONTH(e.eventStartDate) = :month AND YEAR(e.eventStartDate) = :year")
    Page<EventSummary> findByEventStartMonth(@Param("month") int month, @Param("year") int year, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.eventCompanyId = :companyId AND MONTH(e.eventStartDate) = :month AND YEAR(e.eventStartDate) = :year")
    Page<EventSummary> findByEventCompanyIdAndStartMonth(@Param("companyId") String companyId, @Param("month") int month, @Param("year") int year, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.eventStatus = :eventStatus AND MONTH(e.eventStartDate) = :month AND YEAR(e.eventStartDate) = :year")
    Page<EventSummary> findByEventStatusAndStartMonth(@Param("eventStatus") String eventStatus, @Param("month") int month, @Param("year") int year, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.eventCompanyId = :companyId AND e.eventStatus = :eventStatus AND MONTH(e.eventStartDate) = :month AND YEAR(e.eventStartDate) = :year")
    Page<EventSummary> findByEventCompanyIdAndEventStatusAndStartMonth(
            @Param("companyId") String companyId,
            @Param("eventStatus") String eventStatus,
            @Param("month") int month,
            @Param("year") int year,
            Pageable pageable);


    long count();

    long countByEventStatus(String eventStatus);


    @Query("SELECT e.eventId, e.eventTitle FROM Event e WHERE e.eventCompanyId = :eventCompanyId")
    List<Object[]> findEventIdAndTitleByCompanyId(@Param("eventCompanyId") String eventCompanyId);


    long countByEventCompanyId(String eventCompanyId);

    @Query(value = "SELECT * FROM event e WHERE (e.event_tags LIKE CONCAT('%|', :tag, '|%') OR e.event_tags LIKE CONCAT(:tag, '|%') OR e.event_tags LIKE CONCAT('%|', :tag) OR e.event_tags = :tag) AND e.event_status = :status LIMIT 10", nativeQuery = true)
    List<Event> findByEventTagAndEventStatus(@Param("tag") String tag, @Param("status") String status);

//    @Query("SELECT e FROM Event e WHERE MONTH(e.eventStartDate) = MONTH(CURRENT_DATE) AND YEAR(e.eventStartDate) = YEAR(CURRENT_DATE)")
//    List<Event> findEventsInCurrentMonth();


    @Query(value = "SELECT e.event_id AS eventId, e.event_title AS eventTitle, e.event_startdate AS eventStartDate, " +
            "e.event_address AS eventAddress, " +
            "(SELECT i.event_image_url FROM event_image_url i " +
            " WHERE i.event_id = e.event_id AND i.event_image_url LIKE '%poster%' LIMIT 1) AS eventImageURL " +
            "FROM event e " +
            "WHERE MONTH(e.event_startdate) = MONTH(CURDATE()) " +
            "AND YEAR(e.event_startdate) = YEAR(CURDATE()) " +
            "LIMIT 6",
            nativeQuery = true)
    List<EventProjection> findEventsInCurrentMonth();





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

    @Query("SELECT new service.event.dto.EventStatsDTO( " +
            "e.event.eventId, " +
            "e.event.eventTitle, " +
            "e.event.eventPrice, " +
            "COUNT(e), " +
            "COALESCE(SUM(e.ticketPrice), 0), " +
            "COUNT(CASE WHEN e.ticketStatus = 'PAID' THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN e.ticketStatus = 'UNPAID' THEN 1 ELSE NULL END) " +
            ") FROM EventTicket e " +
            "WHERE e.event.eventCompanyId = :companyId " +
            "GROUP BY e.event.eventId")
    Page<EventStatsDTO> getEventTicketStatisticsByCompanyId(@Param("companyId") String companyId, Pageable pageable);




    @Query("SELECT e.eventStatus, COUNT(e) FROM Event e WHERE e.eventCompanyId = :companyId GROUP BY e.eventStatus")
    List<Object[]> countEventsByCompanyIdAndStatus(String companyId);


}
