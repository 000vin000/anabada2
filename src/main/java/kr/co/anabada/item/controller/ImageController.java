package kr.co.anabada.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.anabada.item.entity.Image;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.service.ImageService;

@RestController
@RequestMapping("/images")
public class ImageController {
	@Autowired
	private ImageService service;
	
	@GetMapping("/{itemNo}")
	public ResponseEntity<byte[]> findFirstByItemNo(@PathVariable Item itemNo) {
		Image firstImage = service.findFirstByItemNo(itemNo);
		
		if (firstImage != null) {
			return ResponseEntity.ok()
								.header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
								.body(firstImage.getImageFile());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
