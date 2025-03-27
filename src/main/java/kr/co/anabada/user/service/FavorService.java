package kr.co.anabada.user.service;

import org.springframework.stereotype.Service;

@Service
public class FavorService {
//	@Autowired
//	private FavorMapper favorMapper;
//	@Autowired
//	private ItemMapper itemMapper;
//	@Autowired
//	private ImageMapper imageMapper;
//	
//	public List<ItemImage> selectMyFavor(int userNo) throws IOException {
//		List<Favor> list = favorMapper.selectMyFavor(userNo);
//		List<ItemImage> favorItemList = new ArrayList<>();
//		for (Favor f : list) {
//			ItemImage itemImage = itemMapper.findItemsByItemNo(f.getItemNo());
//			Resource imageRep = imageMapper.imageRep(f.getItemNo());
//			String base64Image = null;
//			if (imageRep != null) {
//				byte[] bytes = imageRep.getContentAsByteArray();
//				base64Image = Base64.getEncoder().encodeToString(bytes);
//			}
//			itemImage.setBase64Image(base64Image);
//			
//			favorItemList.add(itemImage);
//		}
//		
//		return favorItemList;
//	}
//	
//	public boolean isFavorite(int userNo, int itemNo) {
//		 return favorMapper.isFavor(userNo, itemNo);
//	}
//	
//	public int removeFavor(int userNo, int itemNo) {
//		return favorMapper.removeFavor(userNo, itemNo);
//	}
//
//	public boolean toggleFavorite(int userNo, int itemNo) {
//		Favor favor = favorMapper.selectMyFavorItem(userNo, itemNo);
//		if (favor != null) {
//			favorMapper.removeFavor(userNo, itemNo);
//			return false; // 삭제됨
//    	} else {
//		    favorMapper.addFavor(userNo, itemNo);
//		    return true; // 추가됨
//    	}
//	}
}

// TODO 마이페이지, 물품페이지가 완성되면 마이페이지에서 연결해서 부르기, 물품페이지에서 버튼을 눌렀을때 등록하고 삭제되도록 구현하기