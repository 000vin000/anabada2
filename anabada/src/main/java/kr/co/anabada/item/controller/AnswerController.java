package kr.co.anabada.item.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.anabada.item.entity.Answer;
import kr.co.anabada.item.entity.QnA;
import kr.co.anabada.item.service.AnswerService;
import kr.co.anabada.item.service.QuestionService;
import kr.co.anabada.user.entity.User;

@Controller
public class AnswerController {
    @Autowired
    private AnswerService aService;
    @Autowired
    private QuestionService qService;
    
    // 내 모든 물건에 대한 문의글 조회
    @GetMapping("/mypage/a")
    public String getAList(@SessionAttribute(name = "loggedInUser", required = false) User user, Model model) { 
    	int userNo = user.getUserNo();
        List<QnA> list = aService.getAList(userNo);
        model.addAttribute("list", list);
    	return "mypage/a";
    }
    
    // 답변 등록
    @PostMapping("/mypage/a/insertA/{qNo}")
    public String insertA(@PathVariable int qNo,
                          @RequestParam String aContent,
                          @SessionAttribute(name = "loggedInUser", required = false) User user,
                          RedirectAttributes redirectAttributes) {

        int userNo = user.getUserNo();
        LocalDateTime currentTime = LocalDateTime.now();
     
        aService.insertA(qNo, userNo, aContent, currentTime);

        int itemNo = qService.getQuestionByQNo(qNo).getItemNo();  

     
        redirectAttributes.addAttribute("itemNo", itemNo);
        redirectAttributes.addAttribute("qNo", qNo); 
        return "redirect:/mypage/a";  

    }
    
    // 상품 상세페이지에서 답변 등록
    @PostMapping("/item/detail/insertA/{qNo}")
    public String insertAA(@PathVariable int qNo,
                          @RequestParam String aContent,
                          @SessionAttribute(name = "loggedInUser", required = false) User user,
                          RedirectAttributes redirectAttributes) {
    	int userNo = user.getUserNo();
    	LocalDateTime currentTime = LocalDateTime.now();
    	
    	aService.insertA( qNo, userNo, aContent, currentTime);
    
    	 int itemNo = qService.getQuestionByQNo(qNo).getItemNo();
         redirectAttributes.addAttribute("itemNo", itemNo);
         redirectAttributes.addAttribute("qNo", qNo); 
         return "redirect:/item/detail/qna/" + itemNo;
    	
    }




}