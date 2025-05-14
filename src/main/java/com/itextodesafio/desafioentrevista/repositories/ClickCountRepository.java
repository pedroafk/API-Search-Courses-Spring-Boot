package com.itextodesafio.desafioentrevista.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itextodesafio.desafioentrevista.entities.ClickCount;
import com.itextodesafio.desafioentrevista.entities.Posts;

public interface ClickCountRepository extends JpaRepository<ClickCount, Long>{
    Optional<ClickCount> findByPostAndDateClickStartsWith(Posts post, String datePrefix);

    @Query("SELECT c FROM ClickCount c JOIN FETCH c.post")
    List<ClickCount> findAllWithPost();

}

