package kr.co.anabada.user.service.withdraw;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import kr.co.anabada.user.dto.withdraw.UserWithdrawRequestDto;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.entity.UserWithdrawal;
import kr.co.anabada.user.repository.withdraw.UserWithdrawRepository;
import kr.co.anabada.user.repository.withdraw.UserWithdrawalRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserWithdrawService {

    private final UserWithdrawRepository userRepository;
    private final UserWithdrawalRepository withdrawalRepository;

    public void withdraw(Integer userNo, UserWithdrawRequestDto dto) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        user.setUserStatus(User.UserStatus.INACTIVE);
        user.setUserWithdrawalDate(LocalDateTime.now());
        userRepository.save(user);

        UserWithdrawal withdrawal = UserWithdrawal.builder()
                .userNo(user.getUserNo())
                .userEmail(user.getUserEmail())
                .reason(dto.getReason())
                .withdrawnAt(LocalDateTime.now())
                .build();

        withdrawalRepository.save(withdrawal);
    }
}
