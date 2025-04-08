package kr.co.anabada.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AcceptConversionController {
	@GetMapping("/admin/acceptConversion")
	public String acceptConversion() {
		return "admin/acceptConversion";
	}
}
