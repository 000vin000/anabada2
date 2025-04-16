package kr.co.anabada.user.service;

import java.time.LocalDateTime;
import java.util.Optional;

import kr.co.anabada.auth.entity.EmailAuthToken;
import kr.co.anabada.auth.repository.EmailAuthTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.co.anabada.user.dto.IndividualUserJoinDTO;
import kr.co.anabada.user.entity.Buyer;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.entity.Seller.SellerType;
import kr.co.anabada.user.repository.BuyerRepository;
import kr.co.anabada.user.repository.IndividualUserJoinRepository;
import kr.co.anabada.user.repository.SellerRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IndividualUserJoinService {

    @Autowired
    private IndividualUserJoinRepository userJoinRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailAuthTokenRepository emailAuthTokenRepository;
    
    @Autowired
    private BuyerRepository buyerRepo;
    @Autowired
    private SellerRepository sellerRepo; // jhu

    // 아이디 중복 체크
    public boolean isUserIdAvailable(String userId) {
        return userJoinRepository.findByUserId(userId).isEmpty();
    }

    // 닉네임 중복 체크
    public boolean isUserNickAvailable(String userNick) {
        return userJoinRepository.findByUserNick(userNick).isEmpty();
    }

    public void joinUser(IndividualUserJoinDTO userJoinDTO) {

        if (userJoinRepository.findByUserId(userJoinDTO.getUserId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        if (userJoinRepository.findByUserNick(userJoinDTO.getUserNick()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        if (userJoinRepository.findByUserEmail(userJoinDTO.getUserEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        //이메일 인증 여부 확인
        Optional<EmailAuthToken> authTokenOpt = emailAuthTokenRepository
                .findTopByEmailAndIsUsedTrueOrderByExpiresAtDesc(userJoinDTO.getUserEmail());

        if (authTokenOpt.isEmpty() || authTokenOpt.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }

        String encryptedPassword = passwordEncoder.encode(userJoinDTO.getUserPw());

        // 전화번호 조합
        String fullPhone = userJoinDTO.getUserPhone();

        // 주소 조합
        String fullAddress = userJoinDTO.getBaseAddress() + "::" + userJoinDTO.getDetailAddress();

        // 회원 생성
        User user = User.builder()
                .userId(userJoinDTO.getUserId())
                .userPw(encryptedPassword)
                .userName(userJoinDTO.getUserName())
                .userNick(userJoinDTO.getUserNick())
                .userEmail(userJoinDTO.getUserEmail())
                .userPhone(fullPhone)
                .userAddress(fullAddress)
                .userType(userJoinDTO.getUserType())
                .userStatus(User.UserStatus.ACTIVE)
                .userCreatedDate(LocalDateTime.now())
                .userUpdatedDate(LocalDateTime.now())
                .userWarnCnt((byte) 0)
                .emailVerified(true) //인증된 사용자로 저장
                .build();

        Seller newSeller = Seller.builder().user(user).sellerDesc(userJoinDTO.getUserNick() + "의 옷장").sellerType(SellerType.INDIVIDUAL).build();
        Buyer newBuyer = Buyer.builder().user(user).build();
        log.info("회원가입 요청: {}", user);
        
        individualUserJoinTransaction(user, newSeller, newBuyer);
    }
    
    // 로그인 시도시 구매자 판매자 정보가 생성되지 않으면 유저도 생성되지 않는 트랜잭션 : jhu
    @Transactional
    public void individualUserJoinTransaction(User user, Seller newSeller, Buyer newBuyer) {
    	userJoinRepository.save(user);
        sellerRepo.save(newSeller);
        buyerRepo.save(newBuyer);
        log.info("회원가입 완료! userUpdatedDate={}", user.getUserUpdatedDate());
    }
}
