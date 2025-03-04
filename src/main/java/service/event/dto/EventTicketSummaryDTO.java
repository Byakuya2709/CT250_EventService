package service.event.dto;

import java.util.Map;

public class EventTicketSummaryDTO {
    private Map<String, Long> eventCountByStatus;
    private Map<String, Double> totalTicketPriceByStatus;

    // Getters and Setters

    public Map<String, Long> getEventCountByStatus() {
        return eventCountByStatus;
    }

    public void setEventCountByStatus(Map<String, Long> eventCountByStatus) {
        this.eventCountByStatus = eventCountByStatus;
    }

    public Map<String, Double> getTotalTicketPriceByStatus() {
        return totalTicketPriceByStatus;
    }

    public void setTotalTicketPriceByStatus(Map<String, Double> totalTicketPriceByStatus) {
        this.totalTicketPriceByStatus = totalTicketPriceByStatus;
    }
}
