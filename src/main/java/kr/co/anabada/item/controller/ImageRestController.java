package kr.co.anabada.item.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.anabada.item.entity.Image;
import kr.co.anabada.item.service.ImageService;

@RestController
@RequestMapping("/image")
public class ImageRestController {
	@Autowired
	private ImageService service;
	
	@GetMapping("/{itemNo}")
	public ResponseEntity<byte[]> findFirstByItemNo(@PathVariable Integer itemNo) {
		Optional<Image> firstImage = service.findFirstByItemNo(itemNo);
		
		if (firstImage.isPresent() && firstImage.get().getImageFile() != null) {
			String contentType = service.detectImageType(firstImage.get().getImageFile());
			return ResponseEntity.ok()
								.header(HttpHeaders.CONTENT_TYPE, contentType)
								.body(firstImage.get().getImageFile());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/{itemNo}/{index}")
	public ResponseEntity<byte[]> findByItemNoAndIndex(@PathVariable Integer itemNo, @PathVariable Integer index) {
		Image image = service.findByItemNoAndIndex(itemNo, index);
		String contentType = service.detectImageType(image.getImageFile());
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, contentType)
				.body(image.getImageFile());
	}

}
