package test.project.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import test.project.backend.data.entity.Post;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long postId;
    private String title;
    private String content;
    private String userName;


    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getPostTitle();
        this.content = post.getPostContent();
        this.userName = post.getUser().getUsername();

    }

}
