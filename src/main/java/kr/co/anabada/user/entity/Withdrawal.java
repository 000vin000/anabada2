package kr.co.anabada.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
