package test.project.jo.data.entity;

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
public class user {

    @Id
    public Long id;

    @Column(nullable = false)
    public String username;


    @Column(nullable = false)
    public String password;
}
