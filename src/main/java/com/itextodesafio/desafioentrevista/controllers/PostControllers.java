package com.itextodesafio.desafioentrevista.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextodesafio.desafioentrevista.entities.ClickCount;
import com.itextodesafio.desafioentrevista.entities.Posts;
import com.itextodesafio.desafioentrevista.repositories.ClickCountRepository;
import com.itextodesafio.desafioentrevista.repositories.PostsRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(value = "/api/v2/post")
public class PostControllers {

    @Autowired
    private PostsRepository repository;

    @Autowired
    private ClickCountRepository clickCountRepository;

    @Operation(summary = "Registra clique no post", description = """
            Este endpoint registra um clique para o post especificado.
            **Nota:** Este endpoint realiza alterações no banco de dados (incrementa o contador de cliques), o que é um efeito colateral.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Redirecionamento para a URL do post", content = @Content(schema = @Schema(implementation = SimpleUrlResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido ou erro na requisição", content = @Content(schema = @Schema(implementation = Error.class))),
    })
    @GetMapping("/{id}/click")
    public ResponseEntity<?> registerClick(
            @Parameter(description = "ID do post para registrar o clique", required = true) @PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body("ID inválido ou erro na requisição");
            }

            Optional<Posts> postOptional = repository.findById(id);

            if (!postOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Posts post = postOptional.get();

            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Optional<ClickCount> existingClick = clickCountRepository.findByPostAndDateClickStartsWith(post,
                    currentDate);

            if (existingClick.isPresent()) {
                ClickCount clickCount = existingClick.get();
                clickCount.setCount(clickCount.getCount() + 1);
                clickCountRepository.save(clickCount);
            } else {
                ClickCount clickCount = new ClickCount();
                clickCount.setPost(post);
                clickCount.setCount(1);
                clickCount.setDateClick(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                clickCountRepository.save(clickCount);
            }

            return ResponseEntity.ok().body(new SimpleUrlResponse(post.getUrl()));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ID inválido ou erro na requisição");
        }
    }

    @Operation(summary = "Retorna contagem de cliques por post", description = """
            Retorna um array com a contagem de cliques organizada por post.
            Cada item do array contém o título do post como chave e um objeto com:
            - count: número total de cliques
            - date: data do último registro
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Erro interno ao obter contagens de cliques", content = @Content(schema = @Schema(implementation = Error.class))) })
    @GetMapping("/click/counts")
    public ResponseEntity<?> getClickCounts() {
        try {
            List<ClickCount> allClicks = clickCountRepository.findAllWithPost();

            List<Map<String, Object>> result = new ArrayList<>();

            for (ClickCount click : allClicks) {
                if (click.getPost() != null) {
                    Map<String, Object> entry = new HashMap<>();
                    Map<String, Object> details = new HashMap<>();

                    details.put("count", click.getCount());
                    details.put("date", click.getDateClick());

                    entry.put(click.getPost().getTitle(), details);
                    result.add(entry);
                }
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao obter contagens de cliques");
        }
    }

    @Operation(summary = "Busca posts", description = "Retorna uma lista de posts baseados em filtros opcionais")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de posts retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Posts.class)))),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping
    public ResponseEntity<?> findAll(
            @Parameter(description = "Texto para buscar no título ou resumo do post", required = false) @RequestParam(required = false) String content) {
        try {
            List<Posts> posts;

            if (content != null && !content.isEmpty()) {
                posts = repository.findByTitleOrSummaryContaining(content);
            } else {
                posts = repository.findAllByOrderByPubDateDesc();
            }

            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro no servidor");
        }
    }

    private static class SimpleUrlResponse {
        private String url;

        public SimpleUrlResponse(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}