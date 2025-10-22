package com.kch.api_project.service;

import com.kch.api_project.dto.CreateTestPost;
import com.kch.api_project.dto.PatachTestPostDTO;
import com.kch.api_project.dto.PostDetailDTO;
import com.kch.api_project.dto.PostListDTO;
import com.kch.api_project.entity.Post;
import com.kch.api_project.entity.Users;
import com.kch.api_project.excepstions.ResourceNotFoundException;
import com.kch.api_project.repository.AuthRepository;
import com.kch.api_project.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.context.ApplicationContextException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

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
                .title(dto.getTitle())
                .body(dto.getBody())
                .user(author)
                .build();

        Post created = postRepository.save(post);
        return created.getId();
    }

    /**
     * 단건 조회: 조회수 +1 포함
     */
    @Transactional
    public PostDetailDTO getPostDetail(int id) {
        Post post = postRepository.findById((long) id)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        if(post.isDeleted()){
            throw new ResourceNotFoundException("삭제된 게시글입니다.");
        }

        // 조회수 증가
        post.setViewCount(post.getViewCount() + 1);
        // @Transactional 이므로 flush 시점에 업데이트 반영

        return PostDetailDTO.builder()
                .title(post.getTitle())
                .body(post.getBody())
                .users(post.getUser().getUsername())
                .created_at(post.getCreated_at())
                .updated_at(post.getUpdated_at())
                .viewCount(post.getViewCount())
                .build();
    }

    /**
     * 목록 조회: 각 항목의 조회수 포함
     */
    public List<PostListDTO> PostListDto() {
        List<Post> posts = postRepository.findAll();

        List<PostListDTO> result = new ArrayList<>();
        for (Post p : posts) {
            result.add(
                    PostListDTO.builder()
                            .id(p.getId())
                            .title(p.getTitle())
                            .username(p.getUser().getUsername())
                            .created_at(p.getCreated_at())
                            .updated_at(p.getUpdated_at())
                            .viewCount(p.getViewCount())
                            .build()
            );
        }
        return result;
    }

    public void deleteTestPost(long id) {
        String requestUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getUsername().equals(requestUserName)) {
            throw new ApplicationContextException("작성자만 게시글을 삭제할 수 있습니다.");
        }

        post.setDeleted(true);
        postRepository.save(post);

        postRepository.deleteById(id);
    }

    public void patachTestPost(Long id, PatachTestPostDTO dto) {
        String requestUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        if(post.isDeleted()){
            throw new ResourceNotFoundException("삭제된 게시글입니다.");
        }

        if (!post.getUser().getUsername().equals(requestUserName)) {
            throw new ApplicationContextException("작성자만 게시글을 수정할 수 있습니다.");
        }

        // 부분 수정 (PATCH)
        if (dto.getBody() != null) {
            post.setBody(dto.getBody());
        }
        if (dto.getTitle() != null) {
            post.setTitle(dto.getTitle());
        }

        postRepository.save(post);
    }

    // (선택) 다른 곳에서 쓰고 있으면 유지
    @Transactional
    public Post getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));
        post.setViewCount(post.getViewCount() + 1);
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Page<PostListDTO> getPostList(Pageable pageable, String sortType) {

        Page<Post> posts = Page.empty();
        if(sortType.equals("RECENT")){
            posts = postRepository.findByIsDeletedFalseOrderByCreateDateTimeDesc(pageable);
        } else if(sortType.equals("HITS")){
            posts = postRepository.findByIsDeletedFalseOrderByHitsDesc(pageable);
        }else { // 정렬 옵션 값이 똑바로 내려오지 않은 상태
            posts = postRepository.findByIsDeletedFalseOrderByCreateDateTimeDesc(pageable);
        }

        return posts.map(post ->
                PostListDTO.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .username(post.getUser().getUsername())
                        .created_at(post.getCreated_at())
                        .build()
        );
    }

    public Page<PostListDTO> getMyPostList(Pageable pageable) {
        String requestUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByUsername(requestUserName)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        return postRepository.findAllByUserId(user.getId(), pageable).map(post ->
                PostListDTO.builder()
                        .title(post.getTitle())
                        .id(post.getId())
                        .username(post.getUser().getUsername())
                        .created_at(post.getCreated_at())
                        .updated_at(post.getUpdated_at())
                        .build()
        );
    }


}
