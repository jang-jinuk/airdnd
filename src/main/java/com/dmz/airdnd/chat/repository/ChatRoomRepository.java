package com.dmz.airdnd.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmz.airdnd.chat.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
