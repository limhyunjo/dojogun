package test.project.backend.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import test.project.backend.data.dto.PostDto;
import test.project.backend.data.dto.PostResponseDto;

public interface PostRepositoryCustom {

    // 반환을 페이지, PostResponseDto로 해줄거임
    Page<PostResponseDto>searchPage(Pageable pageable);
}
