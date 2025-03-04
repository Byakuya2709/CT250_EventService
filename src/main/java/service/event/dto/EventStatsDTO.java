/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.dto;

/**
 *
 * @author admin
 */
public class EventStatsDTO {

    private Long eventId;
    private String eventTitle;
    private Double eventPrice;
    private Long totalTickets;
    private Double totalRevenue;
    private Long paidTickets;
    private Long unpaidTickets;

    public EventStatsDTO(Long eventId, String eventTitle, Double eventPrice,
                         Long totalTickets, Double totalRevenue,
                         Long paidTickets, Long unpaidTickets) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventPrice = eventPrice;
        this.totalTickets = totalTickets;
        this.totalRevenue = totalRevenue;
        this.paidTickets = paidTickets;
        this.unpaidTickets = unpaidTickets;
    }

    // Getters và Setters


    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(Double eventPrice) {
        this.eventPrice = eventPrice;
    }

    public Long getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(Long totalTickets) {
        this.totalTickets = totalTickets;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Long getPaidTickets() {
        return paidTickets;
    }

    public void setPaidTickets(Long paidTickets) {
        this.paidTickets = paidTickets;
    }

    public Long getUnpaidTickets() {
        return unpaidTickets;
    }

    public void setUnpaidTickets(Long unpaidTickets) {
        this.unpaidTickets = unpaidTickets;
    }
}
