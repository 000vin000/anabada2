package kr.co.anabada.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoAdminController {
	@GetMapping("/error/noAdmin")
	public String noAdmin() {
		return "error/noManagement";
	}
}
