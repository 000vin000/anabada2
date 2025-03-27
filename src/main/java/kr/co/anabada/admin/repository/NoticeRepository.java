package kr.co.anabada.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.anabada.admin.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
	
	List<Notice> findAll();
}
