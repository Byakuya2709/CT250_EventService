/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.response;

import java.util.Date;
import service.event.model.EventTicket;

/**
 *
 * @author admin
 */
public class TicketResponse {
     private Long ticketId;
    private Double ticketPrice;
    private String ticketStatus;

    private String ticketValidity; // Trạng thái đã kích hoạt, hết hạn, còn hiệu lực

    private String ticketPosition;  // Vị trí ghế vé

    private String ticketDuration;

    private String ticketUserId;
    
    private int ticketDay;

    private Date ticketDayActive; // nếu vé single day thì day active là ngày chọn mua, nếu all day thì day acitve là ngày bắt đầu sự kiện

    private Date ticketBookingTime; // Thời gian đặt vé

    private Long eventId;
    private String companyId;
    private String eventTitle;

public TicketResponse(EventTicket ticket) {
    this.ticketId = ticket.getTicketId();
    this.ticketPrice = ticket.getTicketPrice();
    this.ticketStatus = ticket.getTicketStatus();
    this.ticketValidity = ticket.getTicketValidity();
    this.ticketPosition = ticket.getTicketPosition();
    this.ticketDuration = ticket.getTicketDuration().toString();
    this.ticketUserId = ticket.getTicketUserId();
    this.ticketDay = ticket.getTicketDay();
    this.ticketDayActive = ticket.getTicketDayActive();
    this.ticketBookingTime = ticket.getTicketBookingTime();
    this.eventId = ticket.getEvent().getEventId();
    this.companyId = ticket.getEvent().getEventCompanyId();
    this.eventTitle = ticket.getEvent().getEventTitle();
}


    
    
    
    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getTicketValidity() {
        return ticketValidity;
    }

    public void setTicketValidity(String ticketValidity) {
        this.ticketValidity = ticketValidity;
    }

    public String getTicketPosition() {
        return ticketPosition;
    }

    public void setTicketPosition(String ticketPosition) {
        this.ticketPosition = ticketPosition;
    }

    public String getTicketDuration() {
        return ticketDuration;
    }

    public void setTicketDuration(String ticketDuration) {
        this.ticketDuration = ticketDuration;
    }

    public String getTicketUserId() {
        return ticketUserId;
    }

    public void setTicketUserId(String ticketUserId) {
        this.ticketUserId = ticketUserId;
    }

    public int getTicketDay() {
        return ticketDay;
    }

    public void setTicketDay(int ticketDay) {
        this.ticketDay = ticketDay;
    }

    public Date getTicketDayActive() {
        return ticketDayActive;
    }

    public void setTicketDayActive(Date ticketDayActive) {
        this.ticketDayActive = ticketDayActive;
    }

    public Date getTicketBookingTime() {
        return ticketBookingTime;
    }

    public void setTicketBookingTime(Date ticketBookingTime) {
        this.ticketBookingTime = ticketBookingTime;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    
    
}
