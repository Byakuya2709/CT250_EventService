package service.event.response;


import service.event.model.Submission;

import java.util.Date;

public class SubmissionResponse {

    private Long submissionId;
    private String subSubject;
    private Date subCreateDate;
    private Date subFinishDate;
    private String subStatus;
    private Date subDeadline;
    private String subContent;
    private String subFormdata;
    private String subCompanyId;
    private String subCompanyName;

    private String eventTitle;
    private long eventId;
    private Double eventPrice;
    private String eventStatus;


    public SubmissionResponse(Submission submission) {
        this.submissionId = submission.getSubmissionId();
        this.subSubject = submission.getSubSubject();
        this.subCreateDate = submission.getSubCreateDate();
        this.subFinishDate = submission.getSubFinishDate();
        this.subStatus = submission.getSubStatus();
        this.subDeadline = submission.getSubDeadline();
        this.subContent = submission.getSubContent();
        this.subFormdata = submission.getSubFormdata();
        this.subCompanyId = submission.getSubCompanyId();
        this.subCompanyName = submission.getSubCompanyName();

        // Lấy thông tin từ Event (nếu có)
        if (submission.getEvent() != null) {
            this.eventId = submission.getEvent().getEventId();
            this.eventTitle = submission.getEvent().getEventTitle();
            this.eventPrice = submission.getEvent().getEventPrice();
            this.eventStatus = submission.getEvent().getEventStatus();
        }
    }


    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public String getSubSubject() {
        return subSubject;
    }

    public void setSubSubject(String subSubject) {
        this.subSubject = subSubject;
    }

    public Date getSubCreateDate() {
        return subCreateDate;
    }

    public void setSubCreateDate(Date subCreateDate) {
        this.subCreateDate = subCreateDate;
    }

    public Date getSubFinishDate() {
        return subFinishDate;
    }

    public void setSubFinishDate(Date subFinishDate) {
        this.subFinishDate = subFinishDate;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public Date getSubDeadline() {
        return subDeadline;
    }

    public void setSubDeadline(Date subDeadline) {
        this.subDeadline = subDeadline;
    }

    public String getSubContent() {
        return subContent;
    }

    public void setSubContent(String subContent) {
        this.subContent = subContent;
    }

    public String getSubFormdata() {
        return subFormdata;
    }

    public void setSubFormdata(String subFormdata) {
        this.subFormdata = subFormdata;
    }

    public String getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(String subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public Double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(Double eventPrice) {
        this.eventPrice = eventPrice;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }
}
