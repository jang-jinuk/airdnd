package com.dmz.airdnd.chat.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "chat_room", cascade = CascadeType.ALL) // 간단 구현을 위해 onetomany로 일단 구현
	private List<ChatMessage> messages = new ArrayList<>();
}
