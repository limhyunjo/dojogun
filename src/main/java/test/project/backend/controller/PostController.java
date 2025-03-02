package test.project.backend.controller;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import test.project.backend.data.dto.PostDto;
import test.project.backend.data.dto.PostResponseDto;
import test.project.backend.data.entity.SearchOption;
import test.project.backend.service.PostService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostDto postDto){

        PostResponseDto savedPost = postService.savePost(postDto.getUserId(), postDto);
        //return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
        return ResponseEntity.ok(savedPost);
    }

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getPosts(
            @PageableDefault(size = 10, page = 0) Pageable pageable){

        Page<PostResponseDto> posts = postService.getPosts(pageable);
        //return ResponseEntity.ok(postService.getPosts(pageable));
        return ResponseEntity.ok(posts);
    }



    // 게시글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        PostResponseDto postResponse = postService.getPost(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        return ResponseEntity.ok(postResponse);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @RequestBody PostDto postDto) {
        PostResponseDto updatedPost = postService.updatePost(id, postDto);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) throws Exception {
        postService.deletePost(id);
        return ResponseEntity.status(HttpStatus.OK).body("정상 삭제됨");
    }


    // 게시글 검색




        @GetMapping("/search")
        public ResponseEntity<Page<PostResponseDto>> searchPosts(
                @RequestParam String keyword,
                @RequestParam(required = false) SearchOption option, // 검색 옵션 (선택)
                @PageableDefault(size = 10, page = 0) Pageable pageable) {

            Page<PostResponseDto> searchResults = postService.searchPost(keyword, option, pageable);
            return ResponseEntity.ok(searchResults);
        }



}