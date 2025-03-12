/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service.event.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import service.event.model.Event;
import service.event.model.EventTicket;
import service.event.model.VNPayTransaction;

/**
 * @author admin
 */
@Repository
public interface EventTicketRepository extends JpaRepository<EventTicket, Long> {

    List<EventTicket> findByEvent(Event event);

    Page<EventTicket> findByTicketUserId(String ticketUserId,Pageable pageable); // Lấy danh sách vé theo user

    List<EventTicket> findByEventAndTicketDay(Event event, Integer day);

    Page<EventTicket> findByTicketStatus(String ticketStatus, Pageable pageable);

    Page<EventTicket> findByTicketUserEmail(String email, Pageable pageable);

    Page<EventTicket> findByTicketStatusAndTicketUserEmailAndEvent_EventId(
            String ticketStatus, String email, Long eventId, Pageable pageable
    );
    // 🔹 Thêm các phương thức mới
    Page<EventTicket> findByTicketStatusAndEvent(String ticketStatus, Event event, Pageable pageable);
    Page<EventTicket> findByTicketUserEmailAndEvent(String email, Event event, Pageable pageable);
    Page<EventTicket> findByTicketStatusAndTicketUserEmail(String ticketStatus, String email, Pageable pageable);
    Page<EventTicket> findByEvent(Event event, Pageable pageable);

    //    @Query("SELECT e FROM EventTicket e WHERE e.ticketDate BETWEEN :startDate AND :endDate")
//    List<EventTicket> findTicketsByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    long count();

//    long countByTicketStatus(String ticketStatus);

    // Tính tổng giá vé theo ticketStatus
    @Query("SELECT COALESCE(SUM(e.ticketPrice), 0) FROM EventTicket e WHERE e.ticketStatus = :ticketStatus")
    Double sumPriceByTicketStatus(@Param("ticketStatus") String ticketStatus);

    long countByEvent(Event event); // Đếm số vé của sự kiện


    @Query("SELECT SUM(e.ticketPrice) FROM EventTicket e " +
            "WHERE FUNCTION('YEAR', e.ticketBookingTime) = :year " +
            "AND FUNCTION('MONTH', e.ticketBookingTime) = :month " +
            "AND e.ticketStatus = :status")
    Double getTotalPriceForMonthAndStatus(@Param("year") int year,
                                          @Param("month") int month,
                                          @Param("status") String status);

//    @Query("SELECT FUNCTION('MONTH', e.ticketBookingTime) AS month, SUM(e.ticketPrice) " +
//            "FROM EventTicket e " +
//            "WHERE FUNCTION('YEAR', e.ticketBookingTime) = :year " +
//            "AND e.ticketStatus = :status " +
//            "GROUP BY FUNCTION('MONTH', e.ticketBookingTime) " +
//            "ORDER BY month")
//    List<Object[]> getMonthlyTotalPrice(@Param("year") int year,
//                                        @Param("status") String status);


    @Query("SELECT FUNCTION('MONTH', e.ticketBookingTime) AS month, e.ticketStatus, SUM(e.ticketPrice) " +
            "FROM EventTicket e " +
            "WHERE FUNCTION('YEAR', e.ticketBookingTime) = :year " +
            "GROUP BY FUNCTION('MONTH', e.ticketBookingTime), e.ticketStatus " +
            "ORDER BY month")
    List<Object[]> getMonthlyTotalPriceByStatus(@Param("year") int year);


    @Query("SELECT t.ticketStatus, COUNT(t) FROM EventTicket t GROUP BY t.ticketStatus")
    List<Object[]> countTicketsByStatus();


    @Query("SELECT SUM(e.ticketPrice) FROM EventTicket e WHERE e.event = :event")
    Double sumTicketPriceByEvent(@Param("event") Event event); // Tổng giá vé theo sự kiện

    Optional<EventTicket> findByTransaction(VNPayTransaction transaction);


    @Query("SELECT et.ticketStatus, SUM(et.ticketPrice) FROM EventTicket et " +
            "JOIN et.event e WHERE e.eventCompanyId = :companyId GROUP BY et.ticketStatus")
    List<Object[]> calculateTotalTicketPriceByStatusAndCompanyId(String companyId);

    List<EventTicket> findByTicketStatusAndTicketDayActiveBefore(String status, Date now);
    List<EventTicket> findByTicketExpiredTimeBefore(Date now);
    
    
}
