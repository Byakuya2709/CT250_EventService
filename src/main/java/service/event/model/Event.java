/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author admin
 */
@Entity
@Table(name = "event")
public class Event implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "event_title", nullable = false)
    private String eventTitle;

    @Column(name = "event_startdate")
    private Date eventStartDate;

    @Lob
    @Column(name = "event_description")
    private String eventDescription;

    @Column(name = "event_agetag")
    private String eventAgeTag;

    @Column(name = "event_enddate")
    private Date eventEndDate;

    @Column(name = "event_tags")
    private String eventTags;

    @Column(name = "event_duration")
    private String eventDuration;

    @Column(name = "event_address")
    private String eventAddress;
    @Column(name = "event_price")
    private Double eventPrice;
    @Column(name = "event_capacity")
    private Integer eventCapacity;

    @Column(name = "event_status")
    private String eventStatus;

    @Column(name = "event_company_id")
    private String eventCompanyId;

    @ElementCollection
    @CollectionTable(name = "event_list_artist", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "artist_name")
    private List<String> eventListArtist = new ArrayList<>();

    // Mối quan hệ ElementCollection với Map lưu trữ số lượng đánh giá cho từng sao
    @ElementCollection
    @CollectionTable(name = "event_rating_start", joinColumns = @JoinColumn(name = "event_id"))
    @MapKeyColumn(name = "star_rating")  // Lưu chỉ số sao (1, 2, 3, 4, 5)
    @Column(name = "rating_count")  // Lưu số lượng đánh giá cho mỗi sao
    private Map<Integer, Integer> eventRatingStart = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "event_image_url", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "event_image_url")
    private List<String> eventListImgURL = new ArrayList<>();

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Submission contract;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Blog> blogs = new ArrayList<>();


    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FeedBack> feedbacks = new ArrayList<>();


    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<EventTicketZone> ticketZones = new ArrayList<>();

    // Getters, Setters, Constructors
    public Event() {
    }
    public int getTotalDays() {
        if (eventStartDate == null || eventEndDate == null) {
            return 0;
        }
        LocalDate start = eventStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = eventEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return (int) ChronoUnit.DAYS.between(start, end);
    }
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

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Date eventStartDate) {
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

    public Date getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(Date eventEndDate) {
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

    public List<String> getEventListArtist() {
        return eventListArtist;
    }

    public void setEventListArtist(List<String> eventListArtist) {
        this.eventListArtist = eventListArtist;
    }


    public Map<Integer, Integer> getEventRatingStart() {
        return eventRatingStart;
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

    public List<EventTicketZone> getTicketZones() {
        return ticketZones;
    }

    public void setTicketZones(List<EventTicketZone> ticketZones) {
        this.ticketZones = ticketZones;
    }

    public Submission getContract() {
        return contract;
    }

    public void setContract(Submission contract) {
        this.contract = contract;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public Double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(Double eventPrice) {
        this.eventPrice = eventPrice;
    }

    public List<FeedBack> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedBack> feedbacks) {
        this.feedbacks = feedbacks;
    }

}
