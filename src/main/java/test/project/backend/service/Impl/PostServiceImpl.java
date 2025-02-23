package test.project.backend.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.project.backend.data.dto.PostDto;
import test.project.backend.data.dto.PostResponseDto;
import test.project.backend.data.entity.Post;
import test.project.backend.data.entity.User;
import test.project.backend.data.repository.PostRepository;
import test.project.backend.data.repository.UserRepository;
import test.project.backend.service.PostService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;



    // 커스텀repository 받기
    @Override
    public Page<PostResponseDto> getPosts(Pageable pageable) {
        return postRepository.searchPage(pageable); // 바로 돌려줌
    }

    // 작성한 유저 아이디와 게시글 데이터를 받아서
    // 새 객체를 생성하고
    // 레포지토리에 저장 기능 불러와서 담아줌
    // dto에 담아서 작성된 게시글을 다시 클라이언트에게 보내줄 것임
    // 저장된 애를 돌려보내 주는 건 작업 결과에 대한 정보를 호출한 곳에 돌려주기 위한 것임
    @Override
    @Transactional
    public PostResponseDto savePost(Long userId, PostDto postDto){


        User user = userRepository.findById(postDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        Post post = Post.builder()
                .postTitle(postDto.getTitle())
                .postContent(postDto.getContent())
                .user(user)
                .build();

        //id는 Repository에 저장될때 자동 생성
        Post savePost = postRepository.save(post);
        return new PostResponseDto(savePost);
    }

    // 게시글 단건 조회
    //게시글 번호가 url로 들어와서 그거로 조회
    // 해당 id를 가진 게시글을 조회해 dto 객체로 만들어서 반환
    @Override
    public Optional<PostResponseDto> getPost(Long id) {
        // 전달받은 게시글 번호로 게시글 조회
        return postRepository.findById(id).map(PostResponseDto::new);
    }

    /**
     *
     * 옵셔널 유지
     @Override
     public Optional<PostResponseDto> getPost(Long id) {
        return postRepository.findById(id)
            .map(PostResponseDto::new)
            .or(() -> {
                throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
            });
     }
     * 옵셔널 제거
     @Override
     public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return new PostResponseDto(post);
     }
     */

    // 게시글 수정
    // 게시글 번호와 게시글 데이터 받아서
    // 게시글 번호에 해당하는 게시글을 찾고
    // 업데이트된 게시글 dto 객체로 반환
    @Override
    @Transactional
    public PostResponseDto updatePost(Long id, PostDto postDto) {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        User user = userRepository.findById(postDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        if(!post.getUser().equals(user)) {
            throw new IllegalArgumentException("본인 게시글만 수정 가능합니다.");
        }

        post.update(postDto.getTitle(), postDto.getContent());

        return new PostResponseDto(post);
    }

    @Override
    @Transactional
    public void deletePost(Long id) throws Exception {

        // 전달 받은 아이디로 게시글 찾아서 지우기
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        //삭제 권한 로직 -> 유저 담당
        postRepository.deleteById(id);
    }
}
