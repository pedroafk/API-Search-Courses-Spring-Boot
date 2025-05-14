package com.searchcourses.api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.searchcourses.api.dtos.IdClickUrlDto;
import com.searchcourses.api.entities.ClickCountEntity;
import com.searchcourses.api.entities.PostEntity;
import com.searchcourses.api.repositories.ClickCountRepository;
import com.searchcourses.api.repositories.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository repository;

    @Autowired
    private ClickCountRepository clickCountRepository;

    public IdClickUrlDto registerClick(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID inválido ou erro na requisição");
        }

        Optional<PostEntity> postOptional = repository.findById(id);
        if (!postOptional.isPresent()) {
            throw new Exception("Post não encontrado");
        }

        PostEntity post = postOptional.get();
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Optional<ClickCountEntity> existingClick = clickCountRepository.findByPostAndDateClickStartsWith(post, currentDate);

        int count;
        if(existingClick.isPresent()) {
            ClickCountEntity clickCount = existingClick.get();
            count = clickCount.getCount() + 1;
            clickCount.setCount(count);
            clickCountRepository.save(clickCount);
        } else {
            ClickCountEntity newClickCount = new ClickCountEntity();
            newClickCount.setPost(post);
            newClickCount.setCount(1);
            newClickCount.setDateClick(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            clickCountRepository.save(newClickCount);
            count = 1;
        }

        return new IdClickUrlDto(post.getUrl(), post.getId().toString(), post.getTitle(), count);
    }

    public List<Map<String, Object>> getClickCount() throws Exception {
        try {
            List<ClickCountEntity> allClicks = clickCountRepository.findAllWithPost();

            List<Map<String, Object>> result = new ArrayList<>();
            
            for(ClickCountEntity click : allClicks) {
                Map<String, Object> entry = new HashMap<>();
                Map<String, Object> details = new HashMap<>();

                details.put("count", click.getCount());
                details.put("date", click.getDateClick());

                entry.put(click.getPost().getTitle(), details);
                result.add(entry);
            }

            return result;
        } catch (Exception e) {
            throw new Exception("Erro interno ao obter contagens de cliques");
        }
    }

    public List<PostEntity> getPosts(String content) throws Exception {
        try {
            List<PostEntity> posts;

            if(content != null && !content.isEmpty()) {
                posts = repository.findByTitleOrSummaryContaining(content);
            } else {
                posts = repository.findAllByOrderByPubDateDesc();
            }
            
            return posts;
        } catch (Exception e) {
            throw new Exception("Erro no servidor");
        }
    }
}
