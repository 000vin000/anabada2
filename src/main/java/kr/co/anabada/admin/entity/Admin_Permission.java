package kr.co.anabada.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Admin_Permission")
public class Admin_Permission {

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

}
