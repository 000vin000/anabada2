package kr.co.anabada.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.main.dto.ItemInclude1Image;
import kr.co.anabada.main.repository.ItemInclude1ImageRepository;

@Service
public class MainService {
	@Autowired 
	private ItemInclude1ImageRepository repo;
	
	public List<ItemInclude1Image> findAll() {
		return repo.findAllItems();
	}
}
