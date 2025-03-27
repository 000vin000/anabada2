package kr.co.anabada.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.anabada.user.service.FavorService;

@Controller
@RequestMapping("/mypage/favor")
public class FavorController {
	@Autowired
	FavorService service;
	
	@GetMapping
	public String favorList() throws IOException {
		return "mypage/favorPage";
	}
}
