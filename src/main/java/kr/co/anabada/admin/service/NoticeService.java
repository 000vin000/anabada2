package kr.co.anabada.admin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import kr.co.anabada.admin.entity.Notice;
import kr.co.anabada.admin.repository.NoticeRepository;

@Service
public class NoticeService {
	@Autowired
	private NoticeRepository noticeRepository;

	public void saveNotice(Notice notice) {
		noticeRepository.save(notice);
		
	}

	public Notice getNoticeById(Integer noticeNo) {
		return noticeRepository.findById(noticeNo).orElse(null);
	}
	
	public List<Notice> getAllNotices() {
	    return noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "noticeCreatedDate")); // 최신순 정렬
	}

}
