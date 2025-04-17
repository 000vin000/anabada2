package kr.co.anabada.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userNo", nullable = false)
    private Integer userNo;

    // 사용자 타입 (개인 or 브랜드)
    @Enumerated(EnumType.STRING)
    @Column(name = "userType", nullable = false, length = 20)
    private UserType userType = UserType.INDIVIDUAL;
    
    public String getRole() {
        return userType.getRole();  // Enum에서 가져오기
    }

    // 사업자 등록번호 (브랜드만 해당)
    @Column(name = "businessRegNo", length = 20, unique = true)
    private String businessRegNo;

    // 아이디
    @NotBlank(message = "필수 입력값입니다.")
    @Column(name = "userId", nullable = false, length = 20, unique = true)
    private String userId;

    // 비밀번호 (암호화 대비 길이 확장)
    @NotBlank(message = "필수 입력값입니다.")
    @Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
    @Column(name = "userPw", nullable = false, length = 255) // 길이 255 유지
    private String userPw;
    
    // 이름
    @NotBlank(message = "필수 입력값입니다.")
    @Column(name = "userName", nullable = false, length = 20)
    private String userName;

    // 닉네임
    @NotBlank(message = "필수 입력값입니다.")
    @Column(name = "userNick", nullable = false, length = 20, unique = true)
    private String userNick;

    // 이메일
    @NotBlank(message = "필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Column(name = "userEmail", length = 100, unique = true) // 길이 100 유지
    private String userEmail;
    //검증
    @Column(name = "emailVerified", nullable = false)
    private boolean emailVerified = false;

    // 전화번호
    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Column(name = "userPhone", length = 20, unique = true)
    private String userPhone;

    // 주소 (기본 주소 + 상세 주소 포함) 복구됨
    @NotBlank(message = "주소는 필수 입력값입니다.")
    @Column(name = "userAddress", length = 255)
    private String userAddress;

    // 사용자 상태 (active, inactive)
    @Enumerated(EnumType.STRING)
    @Column(name = "userStatus", nullable = false, length = 20)
    private UserStatus userStatus = UserStatus.ACTIVE;

    // 경고 횟수
    @Column(name = "userWarnCnt", nullable = false)
    private Byte userWarnCnt = 0;

    // 생성일자 (가입일)
    @Column(name = "userCreatedDate", nullable = false, updatable = false)
    private LocalDateTime userCreatedDate = LocalDateTime.now();

    // 수정일자
    @Column(name = "userUpdatedDate", nullable = false)
    private LocalDateTime userUpdatedDate = LocalDateTime.now();

    // 탈퇴 요청 일자 (탈퇴 전 inactive 상태에서 저장됨)
    @Column(name = "userWithdrawalDate")
    private LocalDateTime userWithdrawalDate;

    // 보안 비밀번호
    @Column(nullable = true)
    private String userPin; 
    
    
    // 사용자 타입 ENUM
    public enum UserType {
        INDIVIDUAL, // 개인 회원 (오타 수정)
        BRAND,   // 사업자 회원
        ADMIN, // 관리자
    	SOCIAL;
    	
    	public String getRole() {
            return "ROLE_" + this.name();  // 예: "ROLE_ADMIN"
        }
        
        
    }

    // 사용자 상태 ENUM
    public enum UserStatus {
        ACTIVE,   // 활성 상태
        INACTIVE,  // 비활성 (n일 후 삭제)
        WITHDRAWN,
        SUSPENSION, //임시정지
        PERMANENTSTOP //영구정지
    }

    // 업데이트 시 시간 자동 설정
    @PrePersist
    public void prePersist() {
        this.userCreatedDate = LocalDateTime.now();
        this.userUpdatedDate = LocalDateTime.now(); // 자동으로 현재 시간 설정
    }

    @PreUpdate
    public void preUpdate() {
        this.userUpdatedDate = LocalDateTime.now(); // 업데이트할 때 자동 변경
    }
}


