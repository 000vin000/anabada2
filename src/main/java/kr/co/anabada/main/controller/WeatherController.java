package kr.co.anabada.main.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import kr.co.anabada.main.dto.ItemInclude1Image;
import kr.co.anabada.main.dto.TempRequest;
import kr.co.anabada.main.service.MainService;
import kr.co.anabada.main.service.WeatherService;

@Controller
public class WeatherController {
	@Autowired
	private WeatherService service;
	
	@Autowired
	private MainService mainService;
	
    @GetMapping("/weather")
    public String weather() {
        return "main/weather";
    }

    // 클라이언트에서 온도 값을 전달받고 처리
    @PostMapping("/weather")
    public ResponseEntity<List<ItemInclude1Image>> updateTemp(@RequestBody TempRequest tempRequest, Model model) throws IOException {
        String temp = tempRequest.getTemp();
        System.out.println("서버에서 받은 온도 값: " + temp);        
        
        // lat, lon기준 3km 반경에 있는 아이템 가져오기
        String lat = tempRequest.getLat(); // 위도
        String lon = tempRequest.getLon(); // 경도
        
        List<ItemInclude1Image> itemList = service.getTempList(temp, lat, lon);
        mainService.encodeImageFile(itemList);

        return ResponseEntity.ok(itemList);
    }
}
