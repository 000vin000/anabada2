package kr.co.anabada.item.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.service.ImageService;
import kr.co.anabada.item.service.ItemService;

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

    
    // 정빈 추가 (상품문의 시 아이템 정보)
    @GetMapping("/{itemNo}")
    public ResponseEntity<?> getItem(@PathVariable Integer itemNo) {
        Item item = itemService.findById(itemNo);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 아이템을 찾을 수 없습니다.");
        }

        Map<String, Object> response = Map.of(
            "itemNo", item.getItemNo(),
            "itemTitle", item.getItemTitle(),
            "sellerNo", item.getSeller().getUser().getUserNo()
        );

        return ResponseEntity.ok(response);
    }
    
    

    // 아이템 등록 폼
    @GetMapping("/mypage/itemup")
    public String form(Model model) {
        model.addAttribute("itemupCommand", new Item());
        return "mypage/itemup";
    }
    // jhu : 불필요한 객체 생성 -> 모든 요청에 빈 Item 객체 생성 -> GET 요청에만 명시적으로 생성 
    
    // 아이템 등록 처리
    @PostMapping("/mypage/itemup")
    public String submit(@Valid @ModelAttribute("itemupCommand") Item item,
    						@RequestParam String categoryNo,
    						@RequestParam String userToken,
    						@RequestParam String position,
							BindingResult errors,
							@RequestParam("imageFiles[]") MultipartFile[] imageFiles,
							Model model) {
        
        if (errors.hasErrors()) {
            return "mypage/itemup";  // 오류 발생 시 폼 다시 반환
        }
        
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
            itemService.saveItem(item, categoryNo, userToken);  // 아이템 저장
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

        // 아이템 등록 완료 후, 판매 목록 페이지로 리디렉션 할 예정임(임시로 마이페이지로 이동)
        return "redirect:/mypage";
    }
}
