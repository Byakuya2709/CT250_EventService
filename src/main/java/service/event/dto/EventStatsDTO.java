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
    private Long totalTickets;
    private Double totalRevenue;
    private Double eventPrice;

    public EventStatsDTO(Long eventId, String eventTitle, Double eventPrice, Long totalTickets, Double totalRevenue) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.totalTickets = totalTickets;
        this.totalRevenue = totalRevenue;
        this.eventPrice = eventPrice;
    }

    public Double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(Double eventPrice) {
        this.eventPrice = eventPrice;
    }

  

   

   

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(Long totalTickets) {
        this.totalTickets = totalTickets;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}