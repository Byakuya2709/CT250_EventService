/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.request;

/**
 *
 * @author admin
 */
public class UpdatedZoneRequest {
    Double zoneRate;
    Integer zoneCapacity;
    String zoneName;

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
