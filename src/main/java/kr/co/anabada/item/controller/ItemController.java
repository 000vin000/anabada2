package kr.co.anabada.item.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.service.ImageService;
import kr.co.anabada.item.service.ItemService;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;
    
    @Autowired
    private ImageService imageService;
    
    @ModelAttribute("itemupCommand")
    public Item defaultCommand() {
        return new Item();
    }

    // 아이템 등록 폼
    @GetMapping("/mypage/itemup")
    public String form(@ModelAttribute("itemupCommand") Item item) {
        return "mypage/itemup"; 
    }
    
    // 아이템 등록 처리
    @PostMapping("/mypage/itemup")
    public String submit(@Valid @ModelAttribute("itemupCommand") Item item,
                         BindingResult errors,
                         @RequestParam("imageFiles[]") MultipartFile[] imageFiles,
                         @RequestParam("position") String position,
                         HttpServletRequest request) {
        
        if (errors.hasErrors()) {
            return "mypage/itemup";  // 오류 발생 시 폼 다시 반환
        }
        
/*        // 세션에서 로그인된 사용자 정보 확인
        HttpSession session = request.getSession(false);
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/user/login";  // 로그인 안 되어 있으면 로그인 페이지로 리디렉션
        }

        Integer userNo = loggedInUser.getUserNo(); 
        item.setUserNo(userNo); */
        
        // 위치 정보 파싱 (위도, 경도)
        if (position != null && !position.isEmpty()) {
            String[] coords = position.split(" "); // 위도와 경도는 "위도 경도" 형태로 전달됨
            if (coords.length == 2) {
                try {
                    double latitude = Double.parseDouble(coords[0]);
                    double longitude = Double.parseDouble(coords[1]);
                    item.setItemLatitude(latitude);  // 아이템에 위도 설정
                    item.setItemLongitude(longitude);  // 아이템에 경도 설정
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return "mypage/itemup";  // 위도, 경도 값이 잘못된 경우 폼 다시 반환
                }
            }
        }

        // 아이템 저장
        try {
            itemService.saveItem(item);  // 아이템 저장
        } catch (Exception e) {
            e.printStackTrace();
            return "mypage/itemup";  // 아이템 저장 실패 시 폼 다시 반환
        }

        // 이미지 파일 여러 장 저장
        if (imageFiles != null && imageFiles.length > 0) {
            try {
                // 각 이미지 파일을 반복하여 저장
                for (MultipartFile file : imageFiles) {
                    imageService.saveImage(item, file);  // 아이템에 해당하는 이미지 저장
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "mypage/itemup";  // 이미지 저장 실패 시 폼 다시 반환
            }
        }

        // 아이템 등록 완료 후, 판매 목록 페이지로 리디렉션
        return "redirect:/mypage/itemsell"; 
    }
}
