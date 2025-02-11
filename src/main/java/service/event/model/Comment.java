/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "comment")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cmt_id")
    private Long cmtId;

    @Column(name = "cmt_content", nullable = false)
    private String cmtContent;

    @Column(name = "cmt_create_date")
    private Date cmtCreateDate;

    @Column(name = "cmt_emotions_number")
    private Integer cmtEmotionsNumber;

    @Column(name = "cmt_userid")
    private String cmt_userId;

    // Quan hệ 1 Comment thuộc về 1 Blog
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    // Getters, Setters, Constructors
    public Comment() {
    }

    public Long getCmtId() {
        return cmtId;
    }

    public void setCmtId(Long cmtId) {
        this.cmtId = cmtId;
    }

    public String getCmtContent() {
        return cmtContent;
    }

    public void setCmtContent(String cmtContent) {
        this.cmtContent = cmtContent;
    }

    public Date getCmtCreateDate() {
        return cmtCreateDate;
    }

    public void setCmtCreateDate(Date cmtCreateDate) {
        this.cmtCreateDate = cmtCreateDate;
    }

    public Integer getCmtEmotionsNumber() {
        return cmtEmotionsNumber;
    }

    public void setCmtEmotionsNumber(Integer cmtEmotionsNumber) {
        this.cmtEmotionsNumber = cmtEmotionsNumber;
    }

    public String getCmt_userId() {
        return cmt_userId;
    }

    public void setCmt_userId(String cmt_userId) {
        this.cmt_userId = cmt_userId;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }
}


