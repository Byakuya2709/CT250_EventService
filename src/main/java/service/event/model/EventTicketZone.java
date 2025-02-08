package service.event.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event_ticket_zone")
public class EventTicketZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zone_id")
    private Long zoneId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnore
    private Event event;

    @Column(name = "zone_name", nullable = false)
    private String zoneName;

    @Column(name = "zone_rate", nullable = true)
    private Double zoneRate; // Giá vé cho khu vực

    @Column(name = "zone_capacity", nullable = true)
    private Integer zoneCapacity; // Tổng số chỗ

    @Column(name = "day", nullable = false)
    private Integer day; // Ngày trong sự kiện

    @Column(name = "remaining_capacity", nullable = false)
    private Integer remainingCapacity; // Số chỗ còn trống cho ngày đó

    public EventTicketZone() {

    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Double getZoneRate() {
        return zoneRate;
    }

    public void setZoneRate(Double zoneRate) {
        this.zoneRate = zoneRate;
    }

    public Integer getZoneCapacity() {
        return zoneCapacity;
    }

    public void setZoneCapacity(Integer zoneCapacity) {
        this.zoneCapacity = zoneCapacity;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setRemainingCapacity(Integer remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

// Constructors, Getters & Setters
}



