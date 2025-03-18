package kr.co.anabada.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userNo")
    private Integer userNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "userType", nullable = false)
    private UserType userType = UserType.INDIVISUAL;

    @Column(name = "businessRegNo", length = 20, unique = true)
    private String businessRegNo;

    @Column(name = "userName", nullable = false, length = 20)
    private String userName;

    @Column(name = "userNick", nullable = false, length = 20, unique = true)
    private String userNick;

    @Column(name = "userId", nullable = false, length = 20, unique = true)
    private String userId;

    @Column(name = "userPw", nullable = false, length = 100)
    private String userPw;

    @Column(name = "userPhone", nullable = false, length = 20, unique = true)
    private String userPhone;

    @Column(name = "userEmail", nullable = false, length = 100, unique = true)
    private String userEmail;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "userStatus", nullable = false)
    private UserStatus userStatus = UserStatus.ACTIVE;

    @Column(name = "userWarnCnt", nullable = false)
    private Byte userWarnCnt = 0;

    @CreationTimestamp
    @Column(name = "userCreatedDate", nullable = false, updatable = false)
    private LocalDateTime userCreatedDate;

    @UpdateTimestamp
    @Column(name = "userUpdatedDate", nullable = false)
    private LocalDateTime userUpdatedDate;

    @Column(name = "userWithdrawalDate")
    private LocalDateTime userWithdrawalDate;
    
    
    
    public enum UserType {
        INDIVISUAL,
        BRAND
    }
    
    public enum UserStatus {
    	ACTIVE,
    	INACTIVE
    }
}

