package com.searchcourses.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.searchcourses.api.entities.ClickCountEntity;
import com.searchcourses.api.entities.PostEntity;

public interface ClickCountRepository extends JpaRepository<ClickCountEntity, Long>{
    Optional<ClickCountEntity> findByPostAndDateClickStartsWith(PostEntity post, String datePrefix);

    @Query("SELECT c FROM ClickCountEntity c JOIN FETCH c.post")
    List<ClickCountEntity> findAllWithPost();

}

