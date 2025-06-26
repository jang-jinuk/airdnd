Feature: 호스트의 숙소 생성

  Background:
    Given HOST 사용자로 로그인이 되어 있다.

  Scenario: 모든 필수 정보가 올바르면 숙소 생성에 성공한다
    Given 유효한 숙소 등록 정보가 준비되어 있다.
    And 저장소에 동일한 name 을 가진 숙소가 존재하지 않는다.
    When 호스트가 숙소생성 API를 호출했을 때
    Then 숙소 생성 응답 상태로 201 Created를 받는다.
    And 응답 본문은 success는 true이고 바디에 생성된 숙소의 정보가 포함되어야 한다.

  Scenario Outline: 필수 필드가 없으면 400 Bad Request를 반환한다
    Given "<field>" 값이 "<value>" 인 숙소 정보가 주어진다.
    When 호스트가 숙소생성 API를 호출했을 때
    Then 응답 상태 코드는 400 Bad Request여야 한다.
    And 오류 메시지에 "<message>"가 포함되어야 한다

    Examples:
      | field          | value | message                   |
      | name           | <blank> | 숙소 이름은 필수 입력 항목입니다. |
      | pricePerDay    | null  | 하루 가격은 필수 입력 항목입니다. |
      | currency       | <blank> | 통화 단위는 필수 입력 항목입니다. |
      | maxGuests      | null  | 최대 인원 수는 필수 입력 항목입니다. |
      | bedCount       | null  | 침대 수는 필수 입력 항목입니다.    |
      | bedroomCount   | null  | 침실 수는 필수 입력 항목입니다.    |
      | bathroomCount  | null  | 욕실 수는 필수 입력 항목입니다.    |
      | country        | <blank> | 국가 이름은 필수 입력 항목입니다.   |
      | baseAddress    | <blank> | 기본 주소는 필수 입력 항목입니다.   |
