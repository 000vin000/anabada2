package kr.co.anabada;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.anabada.item.entity.QnA;
import kr.co.anabada.item.service.QuestionService;
import kr.co.anabada.main.mapper.MainMapper;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class AnabadaApplicationTests {
	@Autowired
	private MainMapper mapper;
	
	@Autowired
	private QuestionService service;
	
	@Test
	public void testService() {
		List<QnA> qList = service.getQList(4);
		
		log.info(qList.toString());
		
		assertNotNull(qList);
	}
	
	@Test
	void contextLoads() {
		
		
	}

}