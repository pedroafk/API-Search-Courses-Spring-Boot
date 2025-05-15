package com.searchcourses.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.searchcourses.api.entities.ClickCountEntity;

public interface ClickCountRepository extends JpaRepository<ClickCountEntity, Long>{

    @Query("SELECT c FROM ClickCountEntity c WHERE c.post.id = ?1")
    Optional<ClickCountEntity> findByPostId(Long postId);

    @Query("SELECT c FROM ClickCountEntity c JOIN FETCH c.post")
    List<ClickCountEntity> findAllWithPost();

}

