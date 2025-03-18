package kr.co.anabada.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.anabada.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "Warn")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer warnNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminNo", nullable = false)
    private Admin admin;

    @Column(name = "warnReason", nullable = false)
    private String warnReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "warnStatus", nullable = false)
    private WarnStatus warnStatus;

    @CreationTimestamp
    @Column(name = "warnCreatedDate", nullable = false, updatable = false)
    private LocalDateTime warnCreatedDate;

    @Column(name = "warnProcessedDate")
    private LocalDateTime warnProcessedDate;

    
    
    public void approve() {
        validateStatusChange(WarnStatus.APPROVED);
        this.warnStatus = WarnStatus.APPROVED;
        this.warnProcessedDate = LocalDateTime.now();
    }

    public void reject() {
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

    public enum WarnStatus {
        REQUESTED,
        APPROVED,
        REJECTED
    }
}
