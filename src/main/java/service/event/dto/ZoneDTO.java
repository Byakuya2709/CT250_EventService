package service.event.dto;

public class ZoneDTO {
    private String zoneName;
    private Double zoneRate;
    private Integer zoneCapacity;

    public ZoneDTO() {
    }

    public ZoneDTO(String zoneName, Double zoneRate, int zoneCapacity ) {
        this.zoneCapacity = zoneCapacity;
        this.zoneRate = zoneRate;
        this.zoneName = zoneName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Double getZoneRate() {
        return zoneRate;
    }

    public void setZoneRate(Double zoneRate) {
        this.zoneRate = zoneRate;
    }

    public Integer getZoneCapacity() {
        return zoneCapacity;
    }

    public void setZoneCapacity(Integer zoneCapacity) {
        this.zoneCapacity = zoneCapacity;
    }
}