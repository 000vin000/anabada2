package kr.co.anabada.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_withdrawal")
public class UserWithdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long withdrawalNo;

    private Integer userNo;

    private String userEmail;

    private String reason;

    private LocalDateTime withdrawnAt;
}
