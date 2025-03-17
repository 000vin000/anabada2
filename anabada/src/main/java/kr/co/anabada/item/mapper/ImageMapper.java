package kr.co.anabada.item.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.core.io.ByteArrayResource;

import kr.co.anabada.item.entity.Image;

@Mapper
public interface ImageMapper {
	@Insert("INSERT INTO image (itemNo, imageFile) " +
            "VALUES (#{itemNo}, #{imageFile})")
    int save(@Param("itemNo") int itemNo,
    		@Param("imageFile") byte[] imageFile);
	
	@Select("SELECT imageFile FROM image WHERE itemNo = #{itemNo} LIMIT 1")
	ByteArrayResource imageRep(int itemNo); // jhu
	
	@Select("SELECT * FROM image WHERE itemNo = #{itemNo}")
	List<Image> imageList(int itemNo); // jhu
	
	@Update("UPDATE image SET imageFile = #{imageFile} WHERE itemNo = #{itemNo}")
    int updateImage(@Param("itemNo") int itemNo, @Param("imageFile") byte[] imageFile);

	@Delete("DELETE FROM image WHERE itemNo = #{itemNo}")
    int deleteImagesByItemNo(int itemNo);
	
	@Delete("DELETE FROM image WHERE imageNo = #{imageNo}")
	int deleteImageByImageNo(int imageNo);

}