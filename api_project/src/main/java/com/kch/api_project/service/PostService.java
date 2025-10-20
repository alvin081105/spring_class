package com.kch.api_project.service;

import com.kch.api_project.dto.CreateTestPost;
import com.kch.api_project.dto.PatachTestPostDTO;
import com.kch.api_project.dto.PostDetailDTO;
import com.kch.api_project.dto.PostListDTO;
import com.kch.api_project.entity.Post;
import com.kch.api_project.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository testPostRepository;


    public int savePost(CreateTestPost dto) {
        Post Post = Post.builder()
                .body(dto.getBody())
                .title(dto.getTitle())
                .build();

        // 가설 세우기
        // 1. save가 무언가를 반환활것이다.
        // 2. save가 무언가를 반환하지 않으면, ㅈㄴ 귀찮지만 find로 다시 찾자

        Post createdTest = PostRepository.save(Post);

        return createdTest.getId();
    }

    public PostDetailDTO getPostDetail(int id) {
        Optional<Post> otp = PostRepository.findById((long) id);
        if(otp.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Post tp = otp.get();

        PostDetailDTO result = PostDetailDTO.builder()
                .title(tp.getTitle())
                .body(tp.getBody())
                .created_at(tp.getCreated_at())
                .updated_at(tp.getUpdated_at())
                .build();

        return result;
    }

    public List<PostListDTO> PostListDto() {
        List<Post> tpList =  PostRepository.findAll();

        List<PostListDTO> result = new ArrayList<>();
        for (Post tp : tpList) {
            result.add(
                    PostListDTO.builder()
                            .title(tp.getTitle())
                            .id(tp.getId())
                            .created_at(tp.getCreated_at())
                            .updated_at(tp.getUpdated_at())
                            .build()
            );
        }
        return result;
    }


    public void deleteTestPost(long id){
        PostRepository.deleteById(id);
    }

    public void patachTestPost(int id, PatachTestPostDTO dto){
        //수정
        Optional<Post> otp = PostRepository.findById((long) id);
        if(otp.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Post tp = otp.get();

        //if 문을 씌워야 형식이 PUT이 아닌 PATCH임
        if (dto.getBody() != null){
            tp.setBody(dto.getBody());
        }
        if (dto.getBody() != null){
            tp.setTitle(dto.getTitle());
        }


        testPostRepository.save(tp);
    }


}