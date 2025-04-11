package kr.co.anabada.admin.dto;

import lombok.Data;

@Data
public class ReportDTO { // 신고하기 폼에서 받아올 것
    private String warnDefendantUser; // 신고당한 사람
    
    private String warnWhere; // 신고한 위치(url)
    private String warnItem; // warnWhere = itemDetail (필수 x)
    
    private String warnReason; // 신고 사유
    private String warnReasonDetail; // warnReason = other (필수 x)
}
