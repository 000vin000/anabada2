package kr.co.anabada.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.anabada.admin.entity.Notice;
import kr.co.anabada.admin.repository.NoticeRepository;
import kr.co.anabada.admin.service.NoticeService;

@Controller
@RequestMapping("/notice")
public class NoticeController {
	
	@Autowired
	private NoticeService noticeService;
	

    
    // 공지사항 작성 페이지
    @GetMapping("/create")
    public String showNoticeForm() {
        return "admin/noticeCreate";  // 공지사항 등록 페이지로 이동
    }
    
    @GetMapping("/edit/{noticeNo}")
    public String showEditNoticeForm(@PathVariable Integer noticeNo, Model model) {
        Notice notice = noticeService.getNoticeById(noticeNo); // 서비스에서 공지사항 조회
  
        model.addAttribute("notice", notice);
        return "admin/noticeUpdate"; // 공지사항 수정 페이지로 이동
    }
    // 공지사항 리스트
    @GetMapping("/list")
    public String showNoticeList(Model model) {
        model.addAttribute("noticeList", noticeService.getAllNotices());
        return "mypage/noticeList";
    }
    
    @GetMapping("/detail")
    public String noticeDetail(@RequestParam("noticeNo") Integer noticeNo, Model model) {
        Notice notice = noticeService.getNoticeById(noticeNo);

        model.addAttribute("notice", notice);

        return "mypage/noticeDetail";
    }

}
