package com.searchcourses.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.searchcourses.api.entities.Posts;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    
    @Query("SELECT p FROM Posts p WHERE " +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.summary) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY p.pubDate DESC")
    List<Posts> findByTitleOrSummaryContaining(@Param("searchTerm") String searchTerm);

    List<Posts> findAllByOrderByPubDateDesc();
}