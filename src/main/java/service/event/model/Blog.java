/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "blog")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Long blogId;

    @Column(name = "blog_name", nullable = false)
    private String blogName;

    @Lob
    @Column(name = "blog_content", nullable = false)
    private String blogContent;

    @Column(name = "blog_type", nullable = false)
    private String blogType;

    @Column(name = "blog_create_date", nullable = false)
    private Date blogCreateDate;

    @Column(name = "blog_update_date")
    private Date blogUpdateDate;

    @Column(name = "blog_userid")
    private String blog_userId;

    @Column(name = "blog_emotions_number")
    private Integer blogEmotionsNumber;

    // Quan hệ 1 Blog thuộc về 1 Event
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // Quan hệ 1 Blog có 0 hoặc nhiều Comment
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // Getters, Setters, Constructors
    public Blog() {
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public String getBlogType() {
        return blogType;
    }

    public void setBlogType(String blogType) {
        this.blogType = blogType;
    }

    public Date getBlogCreateDate() {
        return blogCreateDate;
    }

    public void setBlogCreateDate(Date blogCreateDate) {
        this.blogCreateDate = blogCreateDate;
    }

    public Date getBlogUpdateDate() {
        return blogUpdateDate;
    }

    public void setBlogUpdateDate(Date blogUpdateDate) {
        this.blogUpdateDate = blogUpdateDate;
    }

    public String getBlog_userId() {
        return blog_userId;
    }

    public void setBlog_userId(String blog_userId) {
        this.blog_userId = blog_userId;
    }

    public Integer getBlogEmotionsNumber() {
        return blogEmotionsNumber;
    }

    public void setBlogEmotionsNumber(Integer blogEmotionsNumber) {
        this.blogEmotionsNumber = blogEmotionsNumber;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
