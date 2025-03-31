package kr.co.anabada.admin.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "withdrawal")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Withdrawal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer withdrawalNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "withdrawalUserNo",nullable = false)
    private User user;
	
	@Column(nullable = false)
	private String withdrawalType;
	
	@Column(nullable = false)
	private String withdrawalEmail;
	
	private String withdrawalReason;
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime withdrawalCreatedDate;
	
	// 기본값 설정
	@PrePersist
	public void prePersist() {
		if (withdrawalType == null) {
			withdrawalType = "voluntary";
		}
	}
}
