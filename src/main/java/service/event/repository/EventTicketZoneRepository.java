package service.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import service.event.model.Event;
import service.event.model.EventTicketZone;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventTicketZoneRepository extends JpaRepository<EventTicketZone,Long> {
    List<EventTicketZone> findByEventAndDay(Event event, Integer  day);

    Optional<EventTicketZone> findByEventAndDayAndZoneName(Event event, Integer day, String zoneName);

    List<EventTicketZone> findByEventAndZoneName(Event event,String zoneName);

    List<EventTicketZone> findByEvent(Event event);

    // Kiểm tra số lượng chỗ trống trong một khu vực của sự kiện
    List<EventTicketZone> findByEventAndRemainingCapacityGreaterThan(Event event, Integer remainingCapacity);

    // Tìm khu vực vé theo tên (nếu cần tìm kiếm theo tên)
    List<EventTicketZone> findByZoneNameContaining(String zoneName);
}
