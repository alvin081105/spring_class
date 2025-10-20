package com.kch.api_project.controller;

import com.kch.api_project.dto.*;
import com.kch.api_project.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/test")
@AllArgsConstructor
@Tag(name = "TestPosts", description = "테스트 게시글 API")
@SecurityRequirement(name = "BearerAuth")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 목록", description = "테스트 게시글 목록을 조회합니다.")
    @GetMapping("/post")
    public ResponseEntity<ApiResponse<List<PostListDTO>>> getTestPostList() {
        List<PostListDTO> list = postService.testPostListDto();
        return ResponseEntity.ok(ApiResponse.ok(list, "성공"));
    }

    @Operation(summary = "검색 키워드 에코", description = "쿼리 파라미터로 받은 키워드를 그대로 반환합니다.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<String>> test(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.ok(keyword, "성공"));
    }

    @Operation(summary = "게시글 삭제", description = "ID로 테스트 게시글을 삭제합니다.")
    @DeleteMapping("/post/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePostTest(@PathVariable int id) {
        postService.deleteTestPost(id);
        return ResponseEntity.ok(ApiResponse.ok("삭제 성공"));
    }

    @Operation(summary = "게시글 수정", description = "ID로 테스트 게시글을 수정합니다.")
    @PatchMapping("/post/{id}")
    public ResponseEntity<ApiResponse<Void>> patachPostTest(
            @PathVariable int id,
            @RequestBody PatachTestPostDTO dto
    ) {
        postService.patachTestPost(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("수정 성공"));
    }

    @Operation(summary = "게시글 단건 조회", description = "ID로 테스트 게시글 상세를 조회합니다.")
    @GetMapping("/post/{id}")
    public ResponseEntity<ApiResponse<PostDetailDTO>> pvTest(@PathVariable int id) {
        PostDetailDTO detail = postService.getTestPostDetail(id);
        return ResponseEntity.ok(ApiResponse.ok(detail, "성공"));
    }

    @Operation(summary = "게시글 생성", description = "테스트 게시글을 생성하고 Location 헤더에 경로를 포함합니다.")
    @PostMapping("/post")
    public ResponseEntity<ApiResponse<Integer>> postTest(@Valid @RequestBody CreateTestPost dto) {
        int createdId = postService.saveTestPost(dto);
        URI location = URI.create("/api/test/post/" + createdId);
        return ResponseEntity.created(location)
                .body(ApiResponse.ok(createdId, "생성 성공"));
    }
}
