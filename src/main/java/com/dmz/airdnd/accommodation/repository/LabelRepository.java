package com.dmz.airdnd.accommodation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmz.airdnd.accommodation.domain.Label;

public interface LabelRepository extends JpaRepository<Label, String> {
}
