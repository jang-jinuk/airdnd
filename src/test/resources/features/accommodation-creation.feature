#Feature: 호스트의 숙소 생성
#
#  Scenario: 모든 필수 정보가 올바르면 숙소 생성에 성공한다
#    Given 호스트가 유효한 숙소 정보를 입력했을 때
#    When 호스트가 POST "/api/accommodations" 요청을 보내면
#    Then 응답 상태 코드는 201 Created여야 한다
#    And 응답 바디에 생성된 숙소의 id, 생성일시(createdAt)가 포함되어야 한다
#
#  Scenario: 비로그인 사용자가 숙소 생성 요청을 하면 401 Unauthorized를 반환한다
#    Given 로그인되지 않은 사용자가
#    When POST "/api/accommodations" 요청을 보내면
#    Then 응답 상태 코드는 401 Unauthorized여야 한다
#
#  Scenario: 필수 필드(name)가 없으면 400 Bad Request를 반환한다
#    Given name이 비어 있는 숙소 정보가 주어졌을 때
#    When POST "/api/accommodations" 요청을 보내면
#    Then 응답 상태 코드는 400 Bad Request여야 한다
#    And 오류 메시지에 "name은 필수 입력 항목입니다."가 포함되어야 한다
#
#  Scenario: pricePerDay가 0 이하이면 400 Bad Request를 반환한다
#    Given pricePerDay가 -1인 숙소 정보가 주어졌을 때
#    When POST "/api/accommodations" 요청을 보내면
#    Then 응답 상태 코드는 400 Bad Request여야 한다
#    And 오류 메시지에 "pricePerDay는 1 이상이어야 합니다."가 포함되어야 한다
#
#  Scenario: maxGuests가 너무 크면 400 Bad Request를 반환한다
#    Given maxGuests가 1000인 숙소 정보가 주어졌을 때
#    When POST "/api/accommodations" 요청을 보내면
#    Then 응답 상태 코드는 400 Bad Request여야 한다
#    And 오류 메시지에 "maxGuests는 1~(최대수용인원) 사이여야 합니다."가 포함되어야 한다