package kr.co.anabada.mypage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favor {
	private int favorNo;
	private int itemNo;
	private int userNo;
}
