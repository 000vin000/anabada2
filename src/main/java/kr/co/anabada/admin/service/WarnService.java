package kr.co.anabada.admin.service;

import kr.co.anabada.admin.entity.Warn;
import kr.co.anabada.admin.repository.WarnRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarnService {

    @Autowired
    private WarnRepository warnRepository;

    // 신고 승인 처리
    public boolean approveWarn(Integer warnNo) {
        Warn warn = warnRepository.findById(warnNo).orElse(null);
        if (warn != null) {
            warn.approve();  // approve 메서드 호출
            warnRepository.save(warn);  // 상태 업데이트 후 저장
            return true;
        }
        return false;
    }

    // 신고 거부 처리
    public boolean rejectWarn(Integer warnNo) {
        Warn warn = warnRepository.findById(warnNo).orElse(null);
        if (warn != null) {
            warn.reject();  // reject 메서드 호출
            warnRepository.save(warn);  // 상태 업데이트 후 저장
            return true;
        }
        return false;
    }

    // 신고 내역 기록
	public void insertWarn(Warn report) {
		warnRepository.save(report);
	}
}
