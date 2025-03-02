package test.project.backend.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import test.project.backend.data.dto.PostResponseDto;
import test.project.backend.data.entity.SearchOption;

public interface PostRepositoryCustom {

    // 반환을 페이지, PostResponseDto로 해줄거임
    Page<PostResponseDto>getPosts(Pageable pageable);


    // 게시글 검색하는 기능
    Page<PostResponseDto>searchPost(String keyword, SearchOption option, Pageable pageable);
}
