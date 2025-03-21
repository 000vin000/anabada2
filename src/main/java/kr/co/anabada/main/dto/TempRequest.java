package kr.co.anabada.main.dto;

import org.hibernate.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Immutable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempRequest {
	private String temp;
}