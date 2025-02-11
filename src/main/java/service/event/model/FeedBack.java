/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "feedbacks")
public class FeedBack  implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long fbId;

    @Column(name = "feedback_content", nullable = false)
    private String fbContent;

    @Column(name = "feedback_create_date")
    private Date fbCreateDate;

    // 1 feedback ứng với 1 ticket
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)  // Liên kết với ticket_id
    @JsonIgnore
    private EventTicket eventTicket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnore
    private Event event;

    public FeedBack() {
    }

    public Long getFbId() {
        return fbId;
    }

    public void setFbId(Long fbId) {
        this.fbId = fbId;
    }

    public String getFbContent() {
        return fbContent;
    }

    public void setFbContent(String fbContent) {
        this.fbContent = fbContent;
    }

    public Date getFbCreateDate() {
        return fbCreateDate;
    }

    public void setFbCreateDate(Date fbCreateDate) {
        this.fbCreateDate = fbCreateDate;
    }

}
