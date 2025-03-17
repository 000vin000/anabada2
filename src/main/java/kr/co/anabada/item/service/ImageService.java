package kr.co.anabada.item.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.anabada.item.entity.Image;
import kr.co.anabada.item.mapper.ImageMapper;

@Service
public class ImageService {
	@Autowired
	private ImageMapper imageMapper;


	public List<String> getImagesBase64(int itemNo) {
	    // itemNo에 해당하는 모든 이미지를 가져옵니다.
	    List<Image> images = imageMapper.imageList(itemNo); 
	    List<String> base64Images = new ArrayList<>();
	    
	    for (Image image : images) {
	        // 이미지 파일을 Base64로 변환하여 리스트에 추가
	        String base64Image = Base64.getEncoder().encodeToString(image.getImageFile());
	        base64Images.add(base64Image);
	    }
	    
	    return base64Images;  // Base64 형식으로 변환된 이미지 목록 반환
	}

	
	public void saveImage(int itemNo, MultipartFile[] imageFiles) throws Exception {
		 for (MultipartFile imageFile : imageFiles) {
	            if (!imageFile.isEmpty()) {
	                byte[] imageBytes = imageFile.getBytes();
	                imageMapper.save(itemNo, imageBytes); // 각 이미지 데이터를 DB에 저장
	            }
	        }
	    }
	
	public Integer getImageNo(Image image) {
        return image.getImageNo(); 
    }
	
	public void deleteImage(int imageNo) {
	    imageMapper.deleteImageByImageNo(imageNo);  // imageNo에 해당하는 이미지 삭제
	}
	
	 public List<Image> getImagesByItemNo(int itemNo) {
	        return imageMapper.imageList(itemNo);  // itemNo에 해당하는 이미지 목록 반환
	}
	
	public void updateImages(int itemNo, MultipartFile[] newImageFiles) throws Exception {
       // imageMapper.deleteImagesByItemNo(itemNo);

        // 새로운 이미지를 DB에 저장
        for (MultipartFile imageFile : newImageFiles) {
            if (!imageFile.isEmpty()) {
                byte[] imageBytes = imageFile.getBytes();
                imageMapper.save(itemNo, imageBytes); // 각 이미지 데이터를 DB에 저장
            }
        }
    }
}