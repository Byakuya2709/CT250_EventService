/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.event.model.Event;
import service.event.model.EventTicketCapacity;

/**
 *
 * @author ADMIN
 */
@Repository
public interface  EventTicketCapacityRepository extends JpaRepository<EventTicketCapacity,Long> {
     EventTicketCapacity findByEventAndDay(Event event, Integer day);
     List<EventTicketCapacity> findByEvent(Event event);
}
