package kr.co.anabada.admin.controller;

import kr.co.anabada.admin.entity.Notice;
import kr.co.anabada.admin.service.NoticeService;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;
import kr.co.anabada.admin.repository.NoticeRepository;
import kr.co.anabada.jwt.JwtTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/notice")
public class NoticeRestController {
    
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    // 현재 로그인한 관리자 정보 가져오기
    private User getLoggedInUser(HttpServletRequest request) {
        var userTokenInfo = jwtTokenHelper.getUserFromRequest(request);
        if (userTokenInfo == null) {
            return null;
        }
        return userRepository.findById(userTokenInfo.getUserNo()).orElse(null);
    }

    // 공지사항 등록 처리
    @PostMapping("/save")
    public ResponseEntity<?> createNotice(@RequestBody Map<String, String> requestData, HttpServletRequest request) {
        User admin = getLoggedInUser(request);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "로그인이 필요합니다."));
        }

        String noticeTitle = requestData.get("noticeTitle");
        String noticeContent = requestData.get("noticeContent");

        if (noticeTitle == null || noticeContent == null || noticeTitle.trim().isEmpty() || noticeContent.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "제목과 내용을 입력해주세요."));
        }

        Notice notice = new Notice();
        notice.setNoticeTitle(noticeTitle);
        notice.setNoticeContent(noticeContent);
        notice.setAdmin(admin);
        noticeService.saveNotice(notice);

        return ResponseEntity.ok(Map.of("message", "공지사항이 성공적으로 등록되었습니다."));
    }

    // 공지사항 수정 처리
    @PutMapping("/update/{noticeNo}")
    public ResponseEntity<?> updateNotice(@PathVariable Integer noticeNo, @RequestBody Map<String, String> requestData) {
        Notice notice = noticeRepository.findById(noticeNo).orElse(null);
        if (notice == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "해당 공지사항을 찾을 수 없습니다."));
        }

        notice.setNoticeTitle(requestData.get("noticeTitle"));
        notice.setNoticeContent(requestData.get("noticeContent"));
        notice.setNoticeUpdatedDate(LocalDateTime.now());

        noticeRepository.save(notice);

        return ResponseEntity.ok(Map.of("message", "공지사항이 성공적으로 수정되었습니다."));
    }

    // 공지사항 삭제 처리
    @DeleteMapping("/delete/{noticeNo}")
    public ResponseEntity<?> deleteNotice(@PathVariable Integer noticeNo) {
        Notice notice = noticeRepository.findById(noticeNo).orElse(null);
        if (notice == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "해당 공지사항을 찾을 수 없습니다."));
        }

        noticeRepository.delete(notice);

        return ResponseEntity.ok(Map.of("message", "공지사항이 성공적으로 삭제되었습니다."));
    }
}
