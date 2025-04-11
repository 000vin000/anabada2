package kr.co.anabada.admin.dto;

import lombok.Data;

@Data
public class WarnResultRequestDTO {
    private String warnResult;
    private int warnSuspensionDays;
}
