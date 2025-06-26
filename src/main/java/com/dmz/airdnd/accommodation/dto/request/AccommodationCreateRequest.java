package com.dmz.airdnd.accommodation.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationCreateRequest {

	@NotBlank(message = "숙소 이름은 필수 입력 항목입니다.")
	@Size(min = 1, max = 50, message = "숙소 이름은 1~50자 이내여야 합니다.")
	private String name;

	private String description;

	@NotNull(message = "하루 가격은 필수 입력 항목입니다.")
	@PositiveOrZero(message = "하루 가격은 0 이상이어야 합니다.")
	private Long pricePerDay;

	@NotBlank(message = "통화 단위는 필수 입력 항목입니다.")
	@Pattern(
		regexp = "^[A-Z]{3}$",
		message = "통화 단위는 3자리 영문 대문자여야 합니다.")
	private String currency;

	@NotNull(message = "최대 인원 수는 필수 입력 항목입니다.")
	@Positive(message = "최대 인원 수는 1 이상이어야 합니다.")
	private Integer maxGuests;

	@NotNull(message = "침대 수는 필수 입력 항목입니다.")
	@PositiveOrZero(message = "침대 수는 0 이상이어야 합니다.")
	private Integer bedCount;

	@NotNull(message = "침실 수는 필수 입력 항목입니다.")
	@PositiveOrZero(message = "침실 수는 0 이상이어야 합니다.")
	private Integer bedroomCount;

	@NotNull(message = "욕실 수는 필수 입력 항목입니다.")
	@PositiveOrZero(message = "욕실 수는 0 이상이어야 합니다.")
	private Integer bathroomCount;

	@NotBlank(message = "국가 이름은 필수 입력 항목입니다.")
	@Size(max = 50, message = "국가 이름은 50자 이내여야 합니다.")
	private String country;

	@NotBlank(message = "기본 주소는 필수 입력 항목입니다.")
	@Size(max = 255, message = "기본 주소는 255자 이내여야 합니다.")
	private String baseAddress;

	@Size(max = 255, message = "상세 주소는 255자 이내여야 합니다.")
	private String detailedAddress;

	@NotEmpty(message = "라벨 목록은 하나 이상 선택해야 합니다.")
	private List<String> labelIds;
}
