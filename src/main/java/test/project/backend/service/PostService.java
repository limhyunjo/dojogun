package test.project.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import test.project.backend.data.dto.PostDto;
import test.project.backend.data.dto.PostResponseDto;

import java.util.Optional;

@Service
public interface PostService {

    // 게시글 저장
    PostResponseDto savePost(Long userid,PostDto postDto);

    Optional<PostResponseDto> getPost(Long id);

    // 게시글 전체 조회
    Page<PostResponseDto> getPosts(Pageable pageable);

    PostResponseDto updatePost(Long id, PostDto postDto);

    void deletePost(Long id) throws Exception;

}
