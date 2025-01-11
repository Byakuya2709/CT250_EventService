package service.event.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "event_ticket")
public class EventTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "ticket_price", nullable = false)
    private Double ticketPrice;

    @Column(name = "ticket_status")
    private String ticketStatus;

    @Column(name = "ticket_validity")
    private String ticketValidity; // Trạng thái đã kích hoạt, hết hạn, còn hiệu lực

    @Column(name = "ticket_position")
    private String ticketPosition;  // Vị trí ghế vé

    @Column(name = "ticket_duration")
    private TicketDay ticketDuration;

    @Column(name = "ticket_date")
    private Date ticketDayActive; // nếu vé single day thì day active là ngày chọn mua, nếu all day thì day acitve là ngày bắt đầu sự kiện

    @Column(name = "ticket_booking_time")
    private Date ticketBookingTime; // Thời gian đặt vé
    // Quan hệ với Event
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnore
    private Event event;

    // Constructor mặc định
    public EventTicket() {
    }

    public enum TicketDay {
        SINGLE_DAY, // Vé cho một ngày cụ thể
        ALL_DAYS;   // Vé cho toàn bộ sự kiệnF
    }

    // Constructor có tham số
    public EventTicket(Double ticketPrice, String ticketStatus, String ticketValidity, String ticketPosition, Event event) {
        this.ticketPrice = ticketPrice;
        this.ticketStatus = ticketStatus;
        this.ticketValidity = ticketValidity;
        this.ticketPosition = ticketPosition;
        this.event = event;
    }

    // Getter và Setter
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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "EventTicket{"
                + "ticketId=" + ticketId
                + ", ticketPrice=" + ticketPrice
                + ", ticketStatus='" + ticketStatus + '\''
                + ", ticketValidity='" + ticketValidity + '\''
                + ", ticketPosition='" + ticketPosition + '\''
                + ", event=" + event
                + '}';
    }

    public TicketDay getTicketDuration() {
        return ticketDuration;
    }

    public void setTicketDuration(TicketDay ticketDuration) {
        this.ticketDuration = ticketDuration;
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
    
    
}
