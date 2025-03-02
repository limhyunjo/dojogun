package test.project.backend.data.repository.Impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import test.project.backend.data.dto.PostResponseDto;
import test.project.backend.data.entity.SearchOption;
import test.project.backend.data.repository.PostRepositoryCustom;

import java.util.List;

import static test.project.backend.data.entity.QPost.post;
import static test.project.backend.data.entity.QUser.user;


@Repository // 반드시 추가해야 빈으로 등록됨
public class PostRepositoryImpl implements PostRepositoryCustom {// 커스텀 repository 구현

    //private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    
    // 생성자 주입 권장하는 방식
    public PostRepositoryImpl(EntityManager em) {
        //this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(em);
    }


    /*
    * total count query에서 join문이 없어도 결과가 똑같이 나옴!
    *
    * */
    
    @Override
    public Page<PostResponseDto> getPosts(Pageable pageable) {

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
                .leftJoin(post.user, user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch(); // 내용만 반환 카운트 쿼리는 따로

        JPAQuery<Long> totalCount = queryFactory
                .select(post.count())
                .from(post)
                .leftJoin(post.user, user);
        // user가 없는 게시글도 조회

        return PageableExecutionUtils.getPage(content,pageable, totalCount::fetchOne);
    }

    // 게시글 옵션으로 검색
    @Override
    public Page<PostResponseDto> searchPost(String keyword, SearchOption option, Pageable pageable) {

        List<PostResponseDto> content =queryFactory
                // Projections.constructor() 방식이 실무에서 더 많이 쓰고 유연
                .select(Projections.constructor(PostResponseDto.class,
                        post.id,
                        post.postTitle,
                        post.postContent,
                        user.username))
                .from(post)
                .leftJoin(post.user, user)
                .where(searchOptions(keyword, option))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        JPAQuery<Long> totalCount = queryFactory
                .select(post.count())
                .from(post)
                .leftJoin(post.user, user)
                .where(searchOptions(keyword, option));

        return PageableExecutionUtils.getPage(content,pageable, totalCount::fetchOne);
    }


    private BooleanExpression searchOptions(String keyword, SearchOption option) {
       
        // 옵션 값이 없으면 전체 조회
        if(option == null){

            return null;
        }

        if (option == SearchOption.CONTENT) {

            return post.postContent.contains(keyword);

        } else if (option == SearchOption.TITLE) {

            return post.postTitle.contains(keyword);

        } else if (option == SearchOption.USERNAME) {

            return user.username.contains(keyword);
        }

        // 기본 전체 조회
        return null;
    }
}
