package dcom.refrigerator.api.domain.user.repository;

import dcom.refrigerator.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
