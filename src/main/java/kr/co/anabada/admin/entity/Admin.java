package kr.co.anabada.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

}
