package test.project.backend.data.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import test.project.backend.data.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
