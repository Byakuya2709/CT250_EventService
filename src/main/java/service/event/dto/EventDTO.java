/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.dto;

import com.sun.istack.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public class EventDTO {

    private String eventTitle;

    private String eventStartDate;

    private String eventDescription;

    private String eventAgeTag;

    private String eventEndDate;

    private String eventTags;

    private String eventDuration;

    private String eventAddress;

    private Integer eventCapacity;

    private String eventStatus;

    private String eventCompanyId;

    private Map<String, String> eventListArtist;

    private List<String> eventListImgURL;

    public EventDTO() {
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventAgeTag() {
        return eventAgeTag;
    }

    public void setEventAgeTag(String eventAgeTag) {
        this.eventAgeTag = eventAgeTag;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getEventTags() {
        return eventTags;
    }

    public void setEventTags(String eventTags) {
        this.eventTags = eventTags;
    }

    public String getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(String eventDuration) {
        this.eventDuration = eventDuration;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public Integer getEventCapacity() {
        return eventCapacity;
    }

    public void setEventCapacity(Integer eventCapacity) {
        this.eventCapacity = eventCapacity;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventCompanyId() {
        return eventCompanyId;
    }

    public void setEventCompanyId(String eventCompanyId) {
        this.eventCompanyId = eventCompanyId;
    }

    public Map<String, String> getEventListArtist() {
        return eventListArtist;
    }

    public void setEventListArtist(Map<String, String> eventListArtist) {
        this.eventListArtist = eventListArtist;
    }

    public List<String> getEventListImgURL() {
        return eventListImgURL;
    }

    public void setEventListImgURL(List<String> eventListImgURL) {
        this.eventListImgURL = eventListImgURL;
    }

}
