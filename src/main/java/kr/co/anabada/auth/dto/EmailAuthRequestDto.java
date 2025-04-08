package kr.co.anabada.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAuthRequestDto {

    private String email;
    private String code;
}
