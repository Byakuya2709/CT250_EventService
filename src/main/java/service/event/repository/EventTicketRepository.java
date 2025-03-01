/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service.event.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    List<EventTicket> findByTicketUserId(String ticketUserId); // Lấy danh sách vé theo user

    List<EventTicket> findByEventAndTicketDay(Event event, Integer day);

    List<EventTicket> findByEventAndTicketStatus(Event event, EventTicket.TicketStatus status);

    List<EventTicket> findByEventAndTicketUserId(Event event, String userId);

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

}
