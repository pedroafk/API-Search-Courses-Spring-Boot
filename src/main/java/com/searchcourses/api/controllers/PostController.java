package com.searchcourses.api.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.searchcourses.api.dtos.ClickCountUrlDto;
import com.searchcourses.api.dtos.IdClickUrlDto;
import com.searchcourses.api.entities.PostEntity;
import com.searchcourses.api.exceptions.ErrorClickPostsResponse;
import com.searchcourses.api.exceptions.ErrorPostResponse;
import com.searchcourses.api.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(value = "/api/v2/post")
public class PostController {

    @Autowired
    private PostService postService;

    // Rota /api/v2/post/{id}/click
    @Operation(summary = "Registra clique no post", description = """
            Este endpoint registra um clique para o post especificado.
            **Nota:** Este endpoint realiza alterações no banco de dados (incrementa o contador de cliques), o que é um efeito colateral.
            """)

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Redirecionamento para a URL do post", content = @Content(mediaType = "application/json", schema = @Schema(implementation = IdClickUrlDto.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido ou erro na requisição", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorClickPostsResponse.class))),
    })

    @GetMapping("/{id}/click")
    public ResponseEntity<?> registerClick(
            @Parameter(description = "ID do post para registrar o clique", required = true) @PathVariable Long id) {
        try {
            IdClickUrlDto response = postService.registerClick(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "ID inválido ou erro na requisição");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // Rota /api/v2/post/click/counts
    @Operation(summary = "Retorna contagem de cliques por post", description = """
            Retorna um array com a contagem de cliques organizada por post.
            Cada item do array contém o título do post como chave e um objeto com:
            - count: número total de cliques
            - date: data do último registro
            """)

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contagens de cliques obtidas com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ClickCountUrlDto.class)))),
            @ApiResponse(responseCode = "500", description = "Erro interno ao obter contagens de cliques", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorClickPostsResponse.class))) })

    @GetMapping("/click/counts")
    public ResponseEntity<?> getClickCounts() {
        try {
            List<ClickCountUrlDto> result = postService.getClickCount();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "Erro interno ao obter contagens de cliques");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // Rota /api/v2/post
    @Operation(summary = "Busca posts", description = "Retorna uma lista de posts baseados em filtros opcionais")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de posts retornada com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PostEntity.class)))),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorPostResponse.class)))
    })
    @GetMapping
    public ResponseEntity<?> findAll(
            @Parameter(description = "Texto para buscar no título ou resumo do post", required = false) @RequestParam(required = false) String content) {
        try {
            List<PostEntity> posts = postService.getPosts(content);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "Erro no servidor");
            return ResponseEntity.internalServerError().body(error);
        }
    }// Fim da rota /api/v2/post
}