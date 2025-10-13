package com.kch.api_project.controller;

import com.kch.api_project.dto.*;
import com.kch.api_project.service.TestPostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/test")
@AllArgsConstructor
public class TestController {

    private final TestPostService testPostService;

    @GetMapping("/post")
    public ResponseEntity<ApiResponse<List<TestPostListDTO>>> getTestPostList() {
        List<TestPostListDTO> list = testPostService.testPostListDto();
        return ResponseEntity.ok(ApiResponse.ok(list, "성공"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<String>> test(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.ok(keyword, "성공"));
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePostTest(@PathVariable int id) {
        testPostService.deleteTestPost(id);
        return ResponseEntity.ok(ApiResponse.ok("삭제 성공"));
    }

    @PatchMapping("/post/{id}")
    public ResponseEntity<ApiResponse<Void>> patachPostTest(
            @PathVariable int id,
            @RequestBody PatachTestPostDTO dto
    ) {
        testPostService.patachTestPost(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("수정 성공"));
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<ApiResponse<TestPostDetailDTO>> pvTest(@PathVariable int id) {
        TestPostDetailDTO detail = testPostService.getTestPostDetail(id);
        return ResponseEntity.ok(ApiResponse.ok(detail, "성공"));
    }

    @PostMapping("/post")
    public ResponseEntity<ApiResponse<Integer>> postTest(@Valid @RequestBody CreateTestPost dto) {
        int createdId = testPostService.saveTestPost(dto);
        URI location = URI.create("/api/test/post/" + createdId);
        return ResponseEntity.created(location)
                .body(ApiResponse.ok(createdId, "생성 성공"));
    }
}
