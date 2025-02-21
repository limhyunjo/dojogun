package test.project.jo.data.repository;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryJ extends JpaRepository<User, Long> {

}
