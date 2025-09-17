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
    public List<TestPostListDTO> getTestPostList() {
        return testPostService.testPostListDto();
    }


    @GetMapping("/search")
    public String test(@RequestParam String keyword) {
        System.out.println("검색 키워드는 : " + keyword + " 입니다");
        return keyword;
    }

    //글 삭제
    @DeleteMapping("/post/{id}")
    public ResponseEntity<Void> deletePostTest(@PathVariable int id){
        try{
            testPostService.deleteTestPost(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    //글 수정 -> Patch
    // Put ->
    @PatchMapping("/post/{id}")
    public ResponseEntity<Void> patachPostTest(
            @PathVariable int id,
            @RequestBody PatachTestPostDTO dto
    ){
        try{
            testPostService.patachTestPost(id, dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/post/{id}")
    public ResponseEntity<TestPostDetailDTO> pvTest(@PathVariable int id){
        try{
            return ResponseEntity.ok(
                    testPostService.getTestPostDetail(id)
            );
        } catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/post")
    public ResponseEntity<Void> postTest(@Valid @RequestBody CreateTestPost dto){
        try{
            int createdId = testPostService.saveTestPost(dto);

            testPostService.saveTestPost(dto);
            URI location = URI.create("/post/" + createdId);
            return ResponseEntity.created(location).build();
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}