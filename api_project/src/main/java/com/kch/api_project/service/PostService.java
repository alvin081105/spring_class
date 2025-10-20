package com.kch.api_project.service;

import com.kch.api_project.dto.CreateTestPost;
import com.kch.api_project.dto.PatachTestPostDTO;
import com.kch.api_project.dto.PostDetailDTO;
import com.kch.api_project.dto.PostListDTO;
import com.kch.api_project.entity.Post;
import com.kch.api_project.entity.Users;
import com.kch.api_project.excepstions.ResourceNotFoundException;
import com.kch.api_project.global.SecurityConfig;
import com.kch.api_project.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.kch.api_project.repository.AuthRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AuthRepository userRepository;


    public int savePost(CreateTestPost dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("유저를 찾을 수 없습니다"));
        Post post = Post.builder()
                .body(dto.getBody())
                .title(dto.getTitle())
                .user(author)
                .build();

        // 가설 세우기
        // 1. save가 무언가를 반환활것이다.
        // 2. save가 무언가를 반환하지 않으면, ㅈㄴ 귀찮지만 find로 다시 찾자

        Post createdTest = postRepository.save(post);

        return createdTest.getId();
    }

    public PostDetailDTO getPostDetail(int id) {
        Optional<Post> otp = postRepository.findById((long) id);
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
        List<Post> tpList =  postRepository.findAll();

        List<PostListDTO> result = new ArrayList<>();
        for (Post tp : tpList) {
            result.add(
                    PostListDTO.builder()
                            .title(tp.getTitle())
                            .id(tp.getId())
                            .username(tp.getUser().getUsername())
                            .created_at(tp.getCreated_at())
                            .updated_at(tp.getUpdated_at())
                            .build()
            );
        }
        return result;
    }


    public void deleteTestPost(long id){
        // 1. 지금 로그인된 사용자의 이름을 가져온다.
        String requestUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        // 2. 삭제하고자 하는 게시글 정보를 가져온다.
        Post post = postRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(("게시글을 찾을 수 없습니다.")));
        // 3. 삭제하고자 하는 게시글의 작성자의 이름이, 지금 로그인된 사용자의 이름과 같은지 확인
        if(!post.getUser().getUsername().equals(requestUserName)){
            throw new ApplicationContextException("작성자만 게시글을 삭제할 수 있습니다.");
        }

        postRepository.deleteById(id);
    }



    public void patachTestPost(Long id, PatachTestPostDTO dto){
        String requestUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(("게시글을 찾을 수 없습니다.")));

        if(!post.getUser().getUsername().equals(requestUserName)){
            throw new ApplicationContextException("작성자만 게시글을 수정할 수 있습니다.");
        }

        //수정
        Optional<Post> otp = postRepository.findById((long) id);
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


        postRepository.save(tp);
    }


}