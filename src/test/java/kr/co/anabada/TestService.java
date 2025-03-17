package kr.co.anabada;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.mapper.UserMapper;
import kr.co.anabada.user.service.UserService;

@SpringBootTest
public class TestService {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserMapper mapper;

	@Test
	public void testMapperPhone() {
		String phone = "01012345679";
		
		User selectByUserPhone = mapper.selectByUserPhone(phone);
		
		System.out.println(selectByUserPhone);
		
		assertNull(selectByUserPhone);
	}
	
	@Test
	public void testPhone() {
		String phone = "01012345679";
		
		boolean result = userService.isUserPhoneDuplicate(phone);
		
		assertFalse(result);
	}
	
	@Test
	@Rollback
	public void insert() {
		User user = User.builder().userAdd("asdf")
					.userEmail("asdf@asdf")
					.userId("asdf")
					.userName("asdf")
					.userNick("asdf")
					.userPhone("010123456744")
					.userPw("asdf")
					.build();
		
		String joinUser = userService.joinUser(user);
		
		assertNotNull(joinUser);
	}
}
