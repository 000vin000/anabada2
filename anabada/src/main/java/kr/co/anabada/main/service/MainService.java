package kr.co.anabada.main.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.anabada.item.entity.ItemImage;
import kr.co.anabada.main.mapper.MainMapper;

@Service
public class MainService {
    @Autowired
    private MainMapper mapper;
    
    // 전체 item List
    @Transactional(readOnly = true)
    public List<ItemImage> selectAll() {
        return mapper.selectAll();
    }
    
    // itemNo의 대표 이미지
    @Transactional(readOnly = true)
    public Resource selectImage1(int itemNo) {
        return mapper.selectImage1(itemNo);
    }

    // 이미지파일이 포함된 Item List
    public List<ItemImage> includeImage(List<ItemImage> itemList) throws IOException {    	
    	for (ItemImage i : itemList) {
    		Resource imageResource = mapper.selectImage1(i.getItemNo());
    		String image = null;
    		
    		if (imageResource != null) {
    			byte[] bytes = imageResource.getContentAsByteArray();
    			image = Base64.getEncoder().encodeToString(bytes);
    		}
    		i.setBase64Image(image);
    	}
    	
    	return itemList;
    }
    
    // List 정렬
    @Transactional(readOnly = true)
    public List<ItemImage> sortByOrder(List<ItemImage> itemList, String sortOrder) {
    	LocalDateTime now = LocalDateTime.now();
    	List<ItemImage> list = new ArrayList<>();
    	for (ItemImage i : itemList) {
    		if (i.getItemEnd().isAfter(now)) {
    			list.add(i);
    		}
    	}
    	
        switch (sortOrder) {
            case "new":
                Collections.sort(list, Comparator.comparing(ItemImage::getItemStart));
                break;
            case "deadline":
                Collections.sort(list, Comparator.comparing(ItemImage::getItemEnd));
                break;
            case "popular":
                Collections.sort(list, Comparator.comparing(ItemImage::getBidCount).reversed());
                break;
            case "asc":
                Collections.sort(list, Comparator.comparing(ItemImage::getItemPrice));
                break;
            case "desc":
                Collections.sort(list, Comparator.comparing(ItemImage::getItemPrice).reversed());
                break;
            default:
                break;
        }
        return list;
    }
}
