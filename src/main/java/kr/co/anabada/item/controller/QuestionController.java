package kr.co.anabada.item.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.anabada.item.entity.QnA;
import kr.co.anabada.item.entity.Question;
import kr.co.anabada.item.service.QuestionService;
import kr.co.anabada.user.entity.User;

@Controller
public class QuestionController {
	 @Autowired
	 private QuestionService qService;
	 
		// 상품별 문의 목록 페이지 
		@GetMapping("/item/detail/qna/{itemNo}")
		public String getQnaPage(@PathVariable int itemNo, 
		                         @SessionAttribute(name = "loggedInUser", required = false) User user, 
		                         Model model) {
		    if (user != null) {
		        int userNo = user.getUserNo();
		        boolean canAnswer = qService.canAnswer(itemNo, userNo);
		            
		        model.addAttribute("canAnswer", canAnswer);

		        List<QnA> myQuestionsList = qService.getQListByUserForItem(userNo, itemNo);
		        model.addAttribute("myQuestionsList", myQuestionsList);
		    }
		        
		    List<QnA> list = qService.getQListByItem(itemNo);        
		    model.addAttribute("list", list);
		        
		    return "item/qna";
		}
 	
	//내가 작성한 모든 문의글 조회
	@GetMapping("/mypage/q")
	public String getQList(@SessionAttribute(name = "loggedInUser", required = false) User user, Model model) {

		int userNo = user.getUserNo();
		List<QnA> list = qService.getQList(userNo); 
		model.addAttribute("list", list);
		return "mypage/q";
	}
	
	// 문의 등록
	@PostMapping("/item/detail/insertQ/{itemNo}")
	public String insertQ(@PathVariable int itemNo, 
	                      @RequestParam String qTitle, 
	                      @RequestParam String qContent, 
	                      @SessionAttribute(name = "loggedInUser", required = false) User user,
	                      RedirectAttributes redirectAttributes) {


	    int userNo = user.getUserNo();
	    LocalDateTime currentTime = LocalDateTime.now();  
	    
	    qService.insertQ(itemNo, userNo, qTitle, qContent, currentTime); 

	    redirectAttributes.addAttribute("itemNo", itemNo);	    
	    return "redirect:/item/detail/qna/" + itemNo;
	}
	
	// 마이페이지에서 문의 삭제
	@PostMapping("/mypage/q/deleteQ/{qNo}/{itemNo}")
	public String deleteQ(@PathVariable int itemNo, @PathVariable int qNo, 
						@SessionAttribute(name = "loggedInUser", required = false) User user,
						RedirectAttributes redirectAttributes) {
	        
			qService.deleteQ(qNo, itemNo);
	        
	        redirectAttributes.addFlashAttribute("message", "문의가 삭제되었습니다.");
	        return "redirect:/mypage/q";
	}
	
	// 상세페이지에서 문의삭제
	@PostMapping("/item/detail/deleteQ/{qNo}/{itemNo}")
	public String deleteQQ(@PathVariable int itemNo, @PathVariable int qNo, 
	                       @SessionAttribute(name = "loggedInUser", required = false) User user, Model model,
	                       RedirectAttributes redirectAttributes) {
		
		int userNo = user.getUserNo();	    
		boolean canQDelete = qService.canQDelete(qNo, userNo);
		model.addAttribute("canQDelete", canQDelete);
		qService.deleteQQ(qNo, itemNo, userNo);
	    redirectAttributes.addFlashAttribute("message", "문의가 삭제되었습니다.");
	    redirectAttributes.addAttribute("itemNo", itemNo);
	    return "redirect:/item/detail/qna/" + itemNo;
	}
	

	
	


}
	




