package kr.co.anabada.item.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.co.anabada.item.entity.Image;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.service.ImageService;
import kr.co.anabada.item.service.ItemService;
import kr.co.anabada.user.entity.User;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemservice;
	
	@Autowired
	private ImageService imageservice;
	
	@ModelAttribute("itemupCommand")
	public Item defalultCommand() {
		return new Item();
	}
	
	//아이템 등록
	@GetMapping("/mypage/itemup")
	public String form(@ModelAttribute("itemupCommand") Item item, Model model) {
		return "mypage/itemup";
	}
	
	@PostMapping("/mypage/itemup")
	public String submit(@Valid @ModelAttribute("itemupCommand") Item item,
	                     BindingResult errors,
	                     @RequestParam("imageFiles[]") MultipartFile[] imageFiles, HttpServletRequest request) {
	    
		if (errors.hasErrors()) {
	        return "mypage/itemup";
	    }
	    
	    HttpSession session = request.getSession(false);
	    User loggedInUser = (User) session.getAttribute("loggedInUser");

	    if (loggedInUser == null) {
	        return "redirect:/user/login"; 
	    }
	    
	    Integer userNo = loggedInUser.getUserNo(); 
	    item.setUserNo(userNo);
	    
	    item.setItemAuction("waiting");
	    
	    try {
	        itemservice.save(item); 
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "mypage/itemup";  // 아이템 저장 실패 시 폼 다시 반환
	    }
	    
	    Integer itemNo = item.getItemNo();
	    
	    try {
	        // 이미지 파일 여러 장 저장
	        if (imageFiles != null && imageFiles.length > 0) {
	            imageservice.saveImage(itemNo, imageFiles);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "mypage/itemup";  
	    }

	    return "redirect:/mypage/itemsell"; 
	}
	
	
	//아이템 수정
	@GetMapping("/mypage/itemupdate/{itemNo}")
	public String showUpdateForm(@PathVariable("itemNo") int itemNo, Model model) {
	    Item item = itemservice.findItemById(itemNo);

	    List<Image> images = imageservice.getImagesByItemNo(itemNo);

	    List<String> base64Images = imageservice.getImagesBase64(itemNo); 

		model.addAttribute("item", item);
		model.addAttribute("images", images);
		model.addAttribute("base64Images", base64Images);

	    return "mypage/itemupdate";  // 아이템 수정 페이지로 리턴
	}

	@PostMapping("/mypage/itemupdate/{itemNo}")
	public String updateItem(@PathVariable("itemNo") Integer itemNo,
	                         @Valid @ModelAttribute("itemupCommand") Item item,
	                         BindingResult errors,
	                         @RequestParam(value = "imageFiles[]", required = false) MultipartFile[] imageFiles,
	                         HttpServletRequest request) {
		
	    if (errors.hasErrors()) {
	        return "mypage/itemupdate";
	    }

	    HttpSession session = request.getSession(false);
	    User loggedInUser = (User) session.getAttribute("loggedInUser");

	    if (loggedInUser == null) {
	        return "redirect:/user/login";
	    }

	    Integer userNo = loggedInUser.getUserNo();
	    item.setUserNo(userNo);
	    item.setItemAuction("waiting");
	    item.setItemNo(itemNo);

	    try {
	        itemservice.updateItem(item);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "mypage/itemupdate";
	    }

	    try {
	        if (imageFiles != null && imageFiles.length > 0) {
	            imageservice.updateImages(itemNo, imageFiles);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "mypage/itemupdate";
	    }

	    return "redirect:/mypage/itemsell";
	}
	//수정하기-이미지삭제
	@PostMapping("/mypage/itemupdate/deleteImage/{imageNo}/{itemNo}")
	public String deleteImage(@PathVariable("imageNo") int imageNo, @PathVariable("itemNo") int itemNo) {
	    try {
	        imageservice.deleteImage(imageNo);  // 이미지 삭제
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return "redirect:/mypage/itemupdate/" + itemNo;  // 아이템 수정 페이지로 리다이렉트
	}

	//판매현황
	@GetMapping("/mypage/itemsell")
    public String itemList(HttpServletRequest request, Model model) {
        
		 HttpSession session = request.getSession(false);
		    User loggedInUser = (User) session.getAttribute("loggedInUser");

		    if (loggedInUser == null) {
		        return "redirect:/user/login";
		    }

		    Integer userNo = loggedInUser.getUserNo();

		    List<Item> items = itemservice.findItemsByUserNo(userNo);

		    model.addAttribute("items", items);

		    return "/mypage/itemsell";
    }
	
	//삭제하기
	@PostMapping("/mypage/itemdelete/{itemNo}")
	public String deleteItem(@PathVariable("itemNo") Integer itemNo) {
	    try {
	        itemservice.deleteItem(itemNo);  // 해당 itemNo를 사용하여 아이템 삭제
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/mypage/itemsell";  // 삭제 실패시 판매현황으로 리다이렉트
	    }
	    return "redirect:/mypage/itemsell";  // 삭제 성공시 판매현황 페이지로 리다이렉트
	}
}