package kr.co.anabada.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.anabada.admin.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
	
}
