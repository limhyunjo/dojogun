package test.project.backend.data.repository.Impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import test.project.backend.data.dto.PostResponseDto;
import test.project.backend.data.repository.PostRepositoryCustom;

import java.util.List;

import static test.project.backend.data.entity.QPost.post;
import static test.project.backend.data.entity.QUser.user;


@Repository // 반드시 추가해야 빈으로 등록됨
public class PostRepositoryImpl implements PostRepositoryCustom {// 커스텀 repository 구현

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    
    // 생성자 주입 권장하는 방식
    public PostRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    
    @Override
    public Page<PostResponseDto> searchPage(Pageable pageable) {

        // post와 user를 합친 dto를 만들어서야 하는데 그냥 responsedto 사용
        // Q로 만드는 건 클래스를 만들어줘야 함 - 실무에서 잘 안 씀
        // dto가 querydsl에 종속되지 않도록 하자
        //Projections.constructor() 또는 Projections.fields() 같은 QueryDSL
        // 의 Projection 기능을 사용하자
        //Projections.constructor() - querydsl에서 dto를 매핑 시 사용
        // dto의 생성자를 호출해서 파라미터를 매핑 -데이터를 채움

        List<PostResponseDto> content =queryFactory
                // Projections.constructor() 방식이 실무에서 더 많이 쓰고 유연
                .select(Projections.constructor(PostResponseDto.class,
                    post.id,
                    post.postTitle,
                    post.postContent,
                    user.username))
                .from(post)
                .join(post.user, user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch(); // 내용만 반환 카운트 쿼리는 따로

        JPAQuery<Long> totalCount = queryFactory
                .select(post.count())
                .from(post)
                .leftJoin(post.user, user);

        return PageableExecutionUtils.getPage(content,pageable, totalCount::fetchOne);
    }
}
