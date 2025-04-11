package kr.co.anabada.user.repository.withdraw;

import kr.co.anabada.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWithdrawRepository extends JpaRepository<User, Integer> {
}
