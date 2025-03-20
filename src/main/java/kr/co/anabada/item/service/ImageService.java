package kr.co.anabada.item.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.anabada.item.entity.Image;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.repository.ImageRepository;
import kr.co.anabada.item.repository.ItemRepository;
import net.coobird.thumbnailator.Thumbnails;

@Service
public class ImageService {
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private ItemRepository itemRepository;

    public Image saveImage(Item item, MultipartFile file) throws IOException {
        // 원본 이미지 InputStream
        InputStream inputStream = file.getInputStream();

        // 리사이징된 이미지 저장을 위한 OutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 세로 500, 가로비율 유지 리사이징
        Thumbnails.of(inputStream)
                  .height(500)
                  .keepAspectRatio(true)
                  .outputFormat("jpg")
                  .toOutputStream(outputStream);

        // Entity 저장
        Image image = new Image();
        image.setItemNo(item);
        image.setImageFile(outputStream.toByteArray());

        return imageRepository.save(image);
    }
    
    public Image findFirstByItemNo(Integer itemNo) {
    	Item item = itemRepository.findByItemNo(itemNo);
    	Image image = imageRepository.findFirstByItemNo(item);
    	return image;
    }
    
    // 이미지를 읽고 타입을 판별하는 메소드
    public String detectImageType(byte[] imageFile) {
	    try {
	        String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageFile));
	        return mimeType != null ? mimeType : MediaType.APPLICATION_OCTET_STREAM_VALUE;
	    } catch (IOException e) {
	        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
	    }
	}
}
