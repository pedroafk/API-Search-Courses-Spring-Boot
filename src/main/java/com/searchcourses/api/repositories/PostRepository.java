package com.searchcourses.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.searchcourses.api.entities.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    
    @Query("SELECT p FROM PostEntity p WHERE " +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.summary) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY p.pubDate DESC")
    List<PostEntity> findByTitleOrSummaryContaining(@Param("searchTerm") String searchTerm);

    List<PostEntity> findAllByOrderByPubDateDesc();
}