/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.event.model.Blog;
import service.event.model.Comment;

/**
 *
 * @author admin
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    
}
