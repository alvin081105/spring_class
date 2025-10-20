package com.kch.api_project.service;

import com.kch.api_project.dto.CreateTestPost;
import com.kch.api_project.dto.PatachTestPostDTO;
import com.kch.api_project.dto.PostDetailDTO;
import com.kch.api_project.dto.PostListDTO;
import com.kch.api_project.entity.TestPost;
import com.kch.api_project.repository.TestPostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {
    private final TestPostRepository testPostRepository;


    public int saveTestPost(CreateTestPost dto) {
        TestPost testPost = TestPost.builder()
                .body(dto.getBody())
                .title(dto.getTitle())
                .build();

        // 가설 세우기
        // 1. save가 무언가를 반환활것이다.
        // 2. save가 무언가를 반환하지 않으면, ㅈㄴ 귀찮지만 find로 다시 찾자

        TestPost createdTest = testPostRepository.save(testPost);

        return createdTest.getId();
    }

    public PostDetailDTO getTestPostDetail(int id) {
        Optional<TestPost> otp = testPostRepository.findById((long) id);
        if(otp.isEmpty()) {
            throw new IllegalArgumentException();
        }

        TestPost tp = otp.get();

        PostDetailDTO result = PostDetailDTO.builder()
                .title(tp.getTitle())
                .body(tp.getBody())
                .created_at(tp.getCreated_at())
                .updated_at(tp.getUpdated_at())
                .build();

        return result;
    }

    public List<PostListDTO> testPostListDto() {
        List<TestPost> tpList =  testPostRepository.findAll();

        List<PostListDTO> result = new ArrayList<>();
        for (TestPost tp : tpList) {
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
        testPostRepository.deleteById(id);
    }

    public void patachTestPost(int id, PatachTestPostDTO dto){
        //수정
        Optional<TestPost> otp = testPostRepository.findById((long) id);
        if(otp.isEmpty()) {
            throw new IllegalArgumentException();
        }

        TestPost tp = otp.get();

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