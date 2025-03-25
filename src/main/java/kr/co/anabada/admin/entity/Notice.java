package kr.co.anabada.admin.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer noticeNo;
	
	@ManyToOne
	@JoinColumn(name = "adminNo", nullable = false) 
    private User admin;
	
	@Column(name = "noticeTitle", nullable = false, length = 30)
	private String noticeTitle;
	
	@Column(name = "noticeContent", nullable = false, columnDefinition = "TEXT")
	private String noticeContent;
	
	@CreationTimestamp
	@Column(name = "noticeCreatedDate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime noticeCreatedDate;

	@UpdateTimestamp
	@Column(name = "noticeUpdatedDate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	private LocalDateTime noticeUpdatedDate;

}
