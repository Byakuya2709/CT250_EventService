/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.event.model.FeedBack;

/**
 *
 * @author admin
 */
@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack,Long> {
    
}
