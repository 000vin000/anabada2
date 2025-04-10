package kr.co.anabada.user.repository.withdraw;

import kr.co.anabada.user.entity.UserWithdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWithdrawalRepository extends JpaRepository<UserWithdrawal, Long> {
}
