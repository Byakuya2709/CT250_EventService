package service.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.event.model.EventTicketZone;

@Repository
public interface EventTicketZoneRepository extends JpaRepository<EventTicketZone,Long> {
}
