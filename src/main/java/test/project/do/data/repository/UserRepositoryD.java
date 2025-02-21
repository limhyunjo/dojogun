package test.project;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryD extends JpaRepository<User, Long> {

}
