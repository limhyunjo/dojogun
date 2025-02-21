package test.project.gun.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class userG {

    @Id
    public Long id;

    @Column(nullable = false)
    public String username;

    //asdfasdfasdf
    @Column(nullable = false)
    public String password;
}
