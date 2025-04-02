package kr.co.anabada.admin.controller;

import kr.co.anabada.admin.entity.Notice;
import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.admin.service.NoticeService;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;
import kr.co.anabada.admin.repository.AdminRepository;
import kr.co.anabada.admin.repository.NoticeRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin/notice")
public class NoticeController {
	
	@Autowired
	private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private UserRepository userRepository;

    // 공지사항 작성 페이지
    @GetMapping("/create")
    public String showNoticeForm() {
        return "admin/noticeCreate";  // 공지사항 등록 페이지로 이동
    }

 // 공지사항 등록 처리
    @PostMapping("/save")
    public String createNotice(@RequestParam String noticeTitle,
                               @RequestParam String noticeContent) {
        // 관리자 번호 하드코딩
        int adminNo = 1;  // 예시: 관리자 번호가 1로 설정됨.
        User admin = userRepository.findById(adminNo).orElse(null);
        if (admin == null) {
            return "redirect:/error";  // User가 존재하지 않으면 에러 페이지로 리다이렉트
        }

        Notice notice = new Notice();
        notice.setNoticeTitle(noticeTitle);
        notice.setNoticeContent(noticeContent);
        notice.setAdmin(admin);  // 관리자 번호 할당
        noticeService.saveNotice(notice);

        return "redirect:/admin/management";
    }
    
    // 공지사항 수정 페이지 (선택적으로 구현)
    @GetMapping("/edit/{noticeNo}")
    public String editNoticeForm(@PathVariable Integer noticeNo, Model model) {
        Notice notice = noticeRepository.findById(noticeNo).orElse(null);
        if (notice == null) {
            model.addAttribute("error", "해당 공지사항을 찾을 수 없습니다.");
            return "error";  // 에러 페이지로 리디렉션
        }
        
        model.addAttribute("notice", notice);
        return "admin/noticeUpdate";  // 공지사항 수정 페이지로 이동
    }

    // 공지사항 수정 처리
    @PostMapping("/update/{noticeNo}")
    public String updateNotice(@PathVariable Integer noticeNo,
                               @RequestParam String noticeTitle,
                               @RequestParam String noticeContent) {

        Notice notice = noticeRepository.findById(noticeNo).orElse(null);
        if (notice == null) {
            return "redirect:/error";  // 공지사항이 없으면 에러 페이지로 이동
        }

        notice.setNoticeTitle(noticeTitle);
        notice.setNoticeContent(noticeContent);

        // 현재 시간을 Timestamp로 가져온 후 LocalDateTime으로 변환
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        LocalDateTime localDateTime = timestamp.toLocalDateTime();

        // Notice의 updatedDate 필드에 LocalDateTime을 설정
        notice.setNoticeUpdatedDate(localDateTime);

        noticeRepository.save(notice);

        return "redirect:/admin/management";  // 공지사항 목록 페이지로 리다이렉트
    }


    // 공지사항 삭제 처리
    @PostMapping("/delete/{noticeNo}")
    public String deleteNotice(@PathVariable Integer noticeNo) {
        Notice notice = noticeRepository.findById(noticeNo).orElse(null);
        if (notice == null) {
            return "redirect:/error";  // 공지사항이 없으면 에러 페이지로 이동
        }

        noticeRepository.delete(notice);

        return "redirect:/admin/management";  // 공지사항 목록 페이지로 리다이렉트
    }
}
