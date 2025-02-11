/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service.event.repository;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import service.event.model.Blog;
import service.event.model.Event;
import service.event.model.EventSummary;

/**
 *
 * @author admin
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAll(Pageable pageable);

    List<EventSummary> findAllByEventCompanyId(String eventCompanyId);
  
    Page<EventSummary> findByEventStatus(String eventStatus,Pageable pageable);

}
