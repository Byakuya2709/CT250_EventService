/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public interface EventSummary {
    Long getEventId();
    String getEventTitle();
    Date getEventStartDate();
    String getEventDescription();
    Date getEventEndDate();
    Double getEventPrice();
    String getEventStatus();
    List<String> getEventListImgURL();
    Map<Integer, Integer> getEventRatingStart();
}

