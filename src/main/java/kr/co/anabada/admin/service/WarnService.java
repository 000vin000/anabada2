package kr.co.anabada.admin.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.admin.dto.WarnResultRequestDTO;
import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.admin.entity.Warn;
import kr.co.anabada.admin.entity.Warn.WarnStatus;
import kr.co.anabada.admin.repository.WarnRepository;

@Service
public class WarnService {
    @Autowired
    private WarnRepository warnRepository;

    // 신고 승인 처리
    public boolean approveWarn(String warnNo, WarnResultRequestDTO result, Admin admin) {
        Warn warn = warnRepository.findById(Integer.valueOf(warnNo)).orElse(null);
        if (warn != null) {
        	Integer susDays = result.getWarnSuspensionDays();
            warn.approve();  // approve 메서드 호출
            warn.setAdminNo(admin);
            
            warn.setWarnSuspensionDays(susDays); // 정지 ?일
            if (result.getWarnResult().equals("SUSPENSION")) {
            	warn.setWarnSuspensionDate((warn.getWarnProcessedDate().plusDays(susDays))); // 정지 언제까지?
            } else if (result.getWarnResult().equals("PERMANENTSTOP")) {
            	warn.setWarnSuspensionDate(LocalDateTime.of(9999, 12, 31, 23, 59, 59)); // 영구 정지
            }
            warnRepository.save(warn);  // 상태 업데이트 후 저장
            return true;
        }
        return false;
    }

    // 신고 거부 처리
    public boolean rejectWarn(String warnNo, Admin admin) {
        Warn warn = warnRepository.findById(Integer.valueOf(warnNo)).orElse(null);
        if (warn != null) {
            warn.reject();  // reject 메서드 호출
            warn.setAdminNo(admin);
            
            warnRepository.save(warn);  // 상태 업데이트 후 저장
            return true;
        }
        return false;
    }

    // 신고 내역 기록
	public void insertWarn(Warn report) {
		warnRepository.save(report);
	}

	// 처리 전 신고 목록
	public List<Warn> findAllByWarnStatus(WarnStatus status) {
		return warnRepository.findAllByWarnStatus(status);
	}
}
