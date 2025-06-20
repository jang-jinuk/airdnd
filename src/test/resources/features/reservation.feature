#Feature: 예약 생성
#
#  Scenario: 유효한 예약 정보면 예약에 성공한다
#    Given 숙소 ID=1, 날짜 2025-08-10~2025-08-12, 게스트 2명 정보를 준비했을 때
#    When POST "/api/bookings" 요청을 보내면
#    Then 응답 상태 코드는 201 Created여야 한다
#    And 응답 바디에 예약 ID, 총 금액(totalPrice)이 포함되어야 한다
#
#  Scenario: 이미 예약된 날짜에 중복 예약 시도하면 409 Conflict를 반환한다
#    Given 동일한 숙소(ID=1)와 날짜 2025-08-10~2025-08-12로 이미 예약이 있을 때
#    When POST "/api/bookings" 요청을 보내면
#    Then 응답 상태 코드는 409 Conflict여야 한다
#    And 오류 메시지에 "해당 날짜에는 이미 예약이 존재합니다."가 포함되어야 한다
#
#  Scenario: 비로그인 사용자가 예약하면 401 Unauthorized를 반환한다
#    Given 로그인되지 않은 사용자가
#    When POST "/api/bookings" 요청을 보내면
#    Then 응답 상태 코드는 401 Unauthorized여야 한다