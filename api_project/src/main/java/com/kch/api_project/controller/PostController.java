package com.kch.api_project.controller;

import com.kch.api_project.dto.*;
import com.kch.api_project.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Operation(summary = "게시글 전체조회(페이지네이션)", description = "페이지, 사이즈로 게시글을 페이징 조회합니다. 기본 정렬: created_at DESC")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostListDTO>>> postlist(
            Pageable pageable,
            @RequestParam String sortType
    ) {
        Page<PostListDTO> result = postService.getPostList(pageable, sortType);
        return ResponseEntity.ok(ApiResponse.ok(result, "성공"));
    }


    @Operation(summary = "게시글 목록", description = "테스트 게시글 목록을 조회합니다.")
    @GetMapping("/post")
    public ResponseEntity<ApiResponse<List<PostListDTO>>> getTestPostList() {
        List<PostListDTO> list = postService.PostListDto();
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
        postService.patachTestPost((long) id, dto);
        return ResponseEntity.ok(ApiResponse.ok("수정 성공"));
    }

    @Operation(summary = "게시글 단건 조회", description = "ID로 테스트 게시글 상세를 조회합니다.")
    @GetMapping("/post/{id}")
    public ResponseEntity<ApiResponse<PostDetailDTO>> pvTest(@PathVariable int id) {
        PostDetailDTO detail = postService.getPostDetail(id);
        return ResponseEntity.ok(ApiResponse.ok(detail, "성공"));
    }

    @Operation(summary = "게시글 생성", description = "테스트 게시글을 생성하고 Location 헤더에 경로를 포함합니다.")
    @PostMapping("/post")
    public ResponseEntity<ApiResponse<Integer>> postTest(@Valid @RequestBody CreateTestPost dto) {
        int createdId = postService.savePost(dto);
        URI location = URI.create("/api/test/post/" + createdId);
        return ResponseEntity.created(location)
                .body(ApiResponse.ok(createdId, "생성 성공"));
    }

    @GetMapping("/my-posts")
    public ResponseEntity<ApiResponse<Page<PostListDTO>>> getMyPosts(
            Pageable pageable
    ){
        Page<PostListDTO> result = postService.getMyPostList(pageable);
        return ResponseEntity.ok(ApiResponse.ok(result, "성공"));
    }

}
