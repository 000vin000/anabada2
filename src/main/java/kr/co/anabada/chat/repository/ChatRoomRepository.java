package kr.co.anabada.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.anabada.chat.entity.Chat_Room;

public interface ChatRoomRepository extends JpaRepository<Chat_Room, Integer> {

	Chat_Room findByRoomNo(Integer roomNo);
}
