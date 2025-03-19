package kr.co.anabada.item.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.anabada.item.entity.Image;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.repository.ImageRepository;

@Service
public class ImageService {
	@Autowired
	private ImageRepository imageRepository;

    public Image saveImage(Item item, MultipartFile file) throws IOException {
        // 원본 이미지 InputStream
//        InputStream inputStream = file.getInputStream();

        // 리사이징된 이미지 저장을 위한 OutputStream
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 세로 500, 가로비율 유지 리사이징
//        Thumbnails.of(inputStream)
//                  .height(500)
//                  .keepAspectRatio(true)
//                  .outputFormat("jpg")
//                  .toOutputStream(outputStream);

        // Entity 저장
        Image image = new Image();
        image.setItemNo(item);
//        image.setImageFile(outputStream.toByteArray());
        image.setImageFile(file.getBytes());

        return imageRepository.save(image);
    }
    
    public Optional<Image> findFirstByItemNo(Item itemNo) {
    	return imageRepository.findFirstByItemNo(itemNo);
    }
}
