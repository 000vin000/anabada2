package kr.co.anabada.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Point_Transaction {
	@Id
	private Integer pointTrNo;
	
	@ManyToOne
	@JoinColumn(name = "buyerNo", nullable = false)
	private Buyer buyerNo;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PointTrType pointTrType = PointTrType.charge; // 기본값 설정
	
	@Column(nullable = false)
	private Double pointTrAmount;
	
	@Column(nullable = false)
	private Double pointTrAfterBalance;
	
	@Column(nullable = false)
	private String pointTrDesc;
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime pointTrCreatedDatel;
	
	public enum PointTrType {
        charge, use, refund
    }
}
