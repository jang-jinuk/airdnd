package com.dmz.airdnd.chat;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmz.airdnd.chat.domain.ChatRoom;
import com.dmz.airdnd.chat.repository.ChatRoomRepository;
import com.dmz.airdnd.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatRoomRepository chatRoomRepository;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createChatRoom(@RequestBody String name) {
		ChatRoom chatRoom = ChatRoom.builder()
			.name(name)
			.messages(List.of())
			.build();

		chatRoomRepository.save(chatRoom);

		return ResponseEntity.ok(ApiResponse.success());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ChatRoom>> getChatRoom(@PathVariable Long id) {
		ChatRoom chatRoom = chatRoomRepository.findById(id).get();
		return ResponseEntity.ok(ApiResponse.success(chatRoom));
	}
}
