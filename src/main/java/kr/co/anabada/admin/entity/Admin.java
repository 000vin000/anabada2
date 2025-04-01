package kr.co.anabada.admin.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Admin")
public class Admin {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)  
	 @Column(name = "adminNo")
	 private Integer adminNo;  

	 @Column(nullable = false, unique = true, length = 20)
	 String adminId; 

	 @Column(name = "adminPw", nullable = false, length = 100)
	 private String adminPw;  

	 @Column(nullable = false)
	 private Boolean canManageIndivisual = false;  // 개인관리 권한

	 @Column(nullable = false)
	 private Boolean canManageBrand = false;  // 브랜드관리 권한

	 @Column(nullable = false)
	 private Boolean canManageFinances = false;  // 재무관리 권한

	 @Column(nullable = false, length = 30)
	 private String adminDept;

	 @Enumerated(EnumType.STRING)
	 @Column(nullable = false)
	 private AdminLevel adminLevel; 

	 @CreationTimestamp
	 @Column(nullable = false, updatable = false)
	 private LocalDateTime adminCreatedDate;  // 생성일자
	 
	 public enum AdminLevel {
		    MANAGER, MASTER
		}
}
