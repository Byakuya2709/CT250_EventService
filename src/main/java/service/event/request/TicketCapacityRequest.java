/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.request;

/**
 *
 * @author admin
 */
public class TicketCapacityRequest {
    Long eventId;
    int day;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public TicketCapacityRequest(Long eventId, int day) {
        this.eventId = eventId;
        this.day = day;
    }
}
