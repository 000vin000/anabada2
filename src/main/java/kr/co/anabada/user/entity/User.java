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

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType = UserType.INDIVISUAL;

    @Column(length = 20, unique = true)
    private String businessRegNo;

    @Column(nullable = false, length = 20)
    private String userName;

    @Column(nullable = false, length = 20, unique = true)
    private String userNick;

    @Column(nullable = false, length = 20, unique = true)
    private String userId;

    @Column(nullable = false, length = 100)
    private String userPw;

    @Column(nullable = false, length = 20, unique = true)
    private String userPhone;

    @Column(nullable = false, length = 100, unique = true)
    private String userEmail;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus = UserStatus.ACTIVE;

    @Column(nullable = false)
    private Byte userWarnCnt = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime userCreatedDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime userUpdatedDate;

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

