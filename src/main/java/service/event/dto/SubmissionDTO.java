/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.dto;

import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class SubmissionDTO {

    private Long submissionId; // ID của submission (nếu cần để cập nhật)

    private String subSubject; // Chủ đề của submission

    private String subCreateDate; // Ngày tạo submission

    private String subFinishDate; // Ngày hoàn thành submission (nếu có)

    private String subStatus; // Trạng thái của submission

    private String subDeadline; // Hạn nộp submission

    private String subContent; // Nội dung của submission

    private Long subCompanyId; // ID của công ty liên quan

    private String subCompanyName; // Tên công ty liên quan

    private Long eventId; // ID của sự kiện liên kết

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

    public String getSubCreateDate() {
        return subCreateDate;
    }

    public void setSubCreateDate(String subCreateDate) {
        this.subCreateDate = subCreateDate;
    }

    public String getSubFinishDate() {
        return subFinishDate;
    }

    public void setSubFinishDate(String subFinishDate) {
        this.subFinishDate = subFinishDate;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public String getSubDeadline() {
        return subDeadline;
    }

    public void setSubDeadline(String subDeadline) {
        this.subDeadline = subDeadline;
    }

    public String getSubContent() {
        return subContent;
    }

    public void setSubContent(String subContent) {
        this.subContent = subContent;
    }

    public Long getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Long subCompanyId) {
        this.subCompanyId = subCompanyId;
    }



    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }


}
