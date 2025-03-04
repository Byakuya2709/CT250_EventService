package service.event.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface EventProjection {
    Long getEventId();
    String getEventTitle();
    Date getEventStartDate();
    String getEventAddress();
    String getEventImageURL();  // Chỉ lấy 1 ảnh
}
