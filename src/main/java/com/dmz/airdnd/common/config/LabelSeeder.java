package com.dmz.airdnd.common.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.dmz.airdnd.accommodation.domain.Label;
import com.dmz.airdnd.accommodation.domain.LabelType;
import com.dmz.airdnd.accommodation.repository.LabelRepository;

import lombok.RequiredArgsConstructor;

// 애플리케이션이 시작될 때 Label 들이 항상 DB 에 존재하도록 보장하기 위한 seeding 로직
@Component
@RequiredArgsConstructor
public class LabelSeeder implements ApplicationRunner {
	private final LabelRepository labelRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		for (LabelType type : LabelType.values()) {
			if (!labelRepository.existsById(type.getId())) {
				labelRepository.save(Label.builder()
					.id(type.getId())
					.name(type.getDisplayName())
					.build()
				);
			}
		}
	}
}
