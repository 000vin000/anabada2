package kr.co.anabada.admin.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "warn")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer warnNo; 

    @ManyToOne
    @JoinColumn(name = "warnPlaintiffUser", nullable = false)
    private User warnPlaintiffUser;
    
    @ManyToOne
    @JoinColumn(name = "warnDefendantUser", nullable = false)
    private User warnDefendantUser;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime warnCreatedDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WarnWhere warnWhere;
    
    @ManyToOne
    private Item warnItem; // warnWhere = ITEM
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WarnReason warnReason;
    
    private String warnReasonDetail; // warnReason = OTHER
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WarnStatus warnStatus;
    
    private LocalDateTime warnProcessedDate;
    
    @Enumerated(EnumType.STRING)
    private WarnResult warnResult;
    
    private Integer warnSuspensionDays;
    
    private LocalDateTime warnSuspensionDate; 
    
    @ManyToOne
    @JoinColumn(name = "adminNo")
    private Admin adminNo;
    
    public void approve() { // 확인
        validateStatusChange(WarnStatus.APPROVED);
        this.warnStatus = WarnStatus.APPROVED;
        this.warnProcessedDate = LocalDateTime.now();
    }

    public void reject() { // 거부
        validateStatusChange(WarnStatus.REJECTED);
        this.warnStatus = WarnStatus.REJECTED;
        this.warnProcessedDate = LocalDateTime.now();
    }

    private void validateStatusChange(WarnStatus newStatus) {
        if (this.warnStatus != WarnStatus.REQUESTED) {
            throw new IllegalStateException("이미 처리된 경고입니다.");
        }
        if (this.warnStatus == newStatus) {
            throw new IllegalArgumentException("동일한 상태로 변경할 수 없습니다.");
        }
    }

    public String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        return dateTime.format(formatter);
    }
    
    public enum WarnWhere { 
    	PROFILE,
    	ITEM,
    	CHATTING
    }
    
    public enum WarnReason {
    	SPAM("스팸홍보/도배글"),
    	PORNOGRAPHY("음란물"),
    	ILLEGALITY("불법정보 포함"),
    	HARM("청소년에게 유해한 내용"),
    	ABUSE("욕설/비방 표현"),
    	PRIVACY("개인정보 노출 게시물"),
    	OTHER("기타");
    	
		private final String korean;

    	WarnReason(String korean) {
			this.korean = korean;
		}

		public String getKorean() {
			return korean;
		}
    }
    
    public enum WarnStatus {
        REQUESTED,
        APPROVED,
        REJECTED
    }
    
    public enum WarnResult {
    	WARNING,
    	SUSPENSION,
    	PERMANENTSTOP
    }
}
