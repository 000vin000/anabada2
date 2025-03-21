package kr.co.anabada.syeon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class test {
	@GetMapping("/1")
	public String getmap() {
		return "test";
	}
	
	@GetMapping("/2")
	public String getLocation() {
		return "test2";
	}
	
	@GetMapping("/3")
	public String getWeather() {
		return "test3";
	}
}
