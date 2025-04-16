package kr.co.anabada.admin.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Column(name = "adminNo", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer adminNo;

    @Column(name = "canManageIndivisual", nullable = false)
    private Boolean canManageIndivisual = false;

    @Column(name = "canManageBrand", nullable = false)
    private Boolean canManageBrand = false;

    @Column(name = "canManageFinances", nullable = false)
    private Boolean canManageFinances = false;

    @Column(name = "adminDept", nullable = false, length = 30)
    private String adminDept;

    @Column(name = "adminLevel", nullable = false)
    private Byte adminLevel = 1; 
    
    @ManyToOne
    @JoinColumn(name = "userNo", nullable = false) 
    private User user; 
    
    @Column(name = "admin_created_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime adminCreatedDate;
    
    @Column(name = "admin_id", nullable = false)
    private String adminId;
    
    @Column(name = "admin_pw", updatable = false)
    private String adminPw;
    
    public static Admin createDefaultAdmin(User user, String adminDept) {
        Admin admin = new Admin();
        admin.setUser(user);
        admin.setAdminId(user.getUserId());
        admin.setAdminPw(user.getUserPw());
        admin.setAdminLevel((byte) 1);
        admin.setCanManageIndivisual(true);
        admin.setCanManageBrand(true);
        admin.setCanManageFinances(true);
        admin.setAdminDept(adminDept);
        return admin;
    }


}
