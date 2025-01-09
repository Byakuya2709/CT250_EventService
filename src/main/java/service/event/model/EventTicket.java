/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "EventTicket")
public class EventTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "ticket_number", nullable = false)
    private String ticketNumber;

    @Column(name = "ticket_price", nullable = false)
    private Double ticketPrice;

    @Column(name = "ticket_status")
    private String ticketStatus;  // Trạng thái trống/đã đặt

    @Column(name = "ticket_validity")
    private String ticketValidity; // Trạng thái đã kích hoạt, hết hạn, còn hiệu lực

    @Column(name = "ticket_position")
    private String ticketPosition;  // Vị trí ghế vé

    // Quan hệ với Event
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // Constructor mặc định
    public EventTicket() {
    }

    // Constructor đầy đủ
    public EventTicket(String ticketNumber, Double ticketPrice, String ticketPosition, Event event) {
        this.ticketNumber = ticketNumber;
        this.ticketPrice = ticketPrice;
        this.ticketPosition = ticketPosition;
        this.event = event;
        this.ticketStatus = "available";  // Mặc định là vé có sẵn
        this.ticketValidity = "valid";    // Mặc định là vé còn hiệu lực
    }

    // Getters, Setters

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    // Phương thức cập nhật trạng thái
    public void reserveTicket() {
        if ("available".equals(this.ticketStatus)) {
            this.ticketStatus = "reserved";
        } else {
            throw new IllegalStateException("Ticket is not available to reserve");
        }
    }

    public void activateTicket() {
        if ("reserved".equals(this.ticketStatus)) {
            this.ticketStatus = "sold";
            this.ticketValidity = "activated";
        } else {
            throw new IllegalStateException("Ticket is not reserved to activate");
        }
    }

    public void expireTicket() {
        if ("activated".equals(this.ticketValidity)) {
            this.ticketValidity = "expired";
        } else {
            throw new IllegalStateException("Ticket is not activated to expire");
        }
    }
}
