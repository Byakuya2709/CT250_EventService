package service.event.response;

import service.event.model.Event;
import service.event.model.EventTicketZone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OneEventResponse {
    private Long eventId;
    private String eventTitle;

    private String eventStartDate;

    private String eventDescription;

    private String eventAgeTag;

    private String eventEndDate;

    private Double eventPrice;

    private String eventTags;

    private String eventDuration;

    private String eventAddress;

    private Integer eventCapacity;

    private String eventStatus;

    private String eventCompanyId;

    private int totalDay;

    private List<String> eventListArtist;

    private List<String> eventListImgURL;

    private Map<Integer, Integer> eventRatingStart;

    private Map<Integer, Integer> eventTicketCapacity = new HashMap<>();

    public static OneEventResponse toEventResponse(Event event) {
        OneEventResponse res = new OneEventResponse();
        res.setEventId(event.getEventId());
        res.setEventAddress(event.getEventAddress());
        res.setEventTitle(event.getEventTitle());
        res.setEventStartDate(event.getEventStartDate().toString()); // Chuyển Date thành String
        res.setEventEndDate(event.getEventEndDate().toString()); // Chuyển Date thành String
        res.setEventDescription(event.getEventDescription());
        res.setEventAgeTag(event.getEventAgeTag());
        res.setEventPrice(event.getEventPrice());
        res.setEventTags(event.getEventTags());
        res.setEventDuration(event.getEventDuration());
        res.setEventAddress(event.getEventAddress());
        res.setEventCapacity(event.getEventCapacity());
        res.setEventStatus(event.getEventStatus());
        res.setEventCompanyId(event.getEventCompanyId());
        res.setEventListArtist(event.getEventListArtist());
        res.setEventListImgURL(event.getEventListImgURL());
        res.setEventRatingStart(event.getEventRatingStart());

        res.setTotalDay(event.getTotalDays());

        Map<Integer, Integer> eventTicketCapacity = event.getTicketZones().stream()
                .collect(Collectors.groupingBy(
                        EventTicketZone::getDay,
                        Collectors.summingInt(EventTicketZone::getRemainingCapacity)
                ));

        res.setEventTicketCapacity(eventTicketCapacity);

        return res;
    }

    public OneEventResponse() {
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Map<Integer, Integer> getEventTicketCapacity() {
        return eventTicketCapacity;
    }

    public void setEventTicketCapacity(Map<Integer, Integer> eventTicketCapacity) {
        this.eventTicketCapacity = eventTicketCapacity;
    }

    public Map<Integer, Integer> getEventRatingStart() {
        return eventRatingStart;
    }

    public int getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(int totalDay) {
        this.totalDay = totalDay;
    }

    public void setEventRatingStart(Map<Integer, Integer> eventRatingStart) {
        this.eventRatingStart = eventRatingStart;
    }

    public List<String> getEventListImgURL() {
        return eventListImgURL;
    }

    public void setEventListImgURL(List<String> eventListImgURL) {
        this.eventListImgURL = eventListImgURL;
    }

    public List<String> getEventListArtist() {
        return eventListArtist;
    }

    public void setEventListArtist(List<String> eventListArtist) {
        this.eventListArtist = eventListArtist;
    }

    public String getEventCompanyId() {
        return eventCompanyId;
    }

    public void setEventCompanyId(String eventCompanyId) {
        this.eventCompanyId = eventCompanyId;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Integer getEventCapacity() {
        return eventCapacity;
    }

    public void setEventCapacity(Integer eventCapacity) {
        this.eventCapacity = eventCapacity;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(String eventDuration) {
        this.eventDuration = eventDuration;
    }

    public String getEventTags() {
        return eventTags;
    }

    public void setEventTags(String eventTags) {
        this.eventTags = eventTags;
    }

    public Double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(Double eventPrice) {
        this.eventPrice = eventPrice;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getEventAgeTag() {
        return eventAgeTag;
    }

    public void setEventAgeTag(String eventAgeTag) {
        this.eventAgeTag = eventAgeTag;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
}
