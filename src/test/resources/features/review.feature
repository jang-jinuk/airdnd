#Feature: 후기 작성
#
#  Scenario: 숙소에 대한 유효한 후기이면 작성에 성공한다
#    Given 로그인된 게스트가 예약 완료된 숙소(ID=1)에 대해 별점 5, 내용 "최고예요!" 를 입력했을 때
#    When POST "/api/reviews" 요청을 보내면
#    Then 응답 상태 코드는 201 Created여야 한다
#
#  Scenario: 별점이 1~5 범위를 벗어나면 400 Bad Request를 반환한다
#    Given 별점 6으로 설정했을 때
#    When POST "/api/reviews" 요청을 보내면
#    Then 응답 상태 코드는 400 Bad Request여야 한다
#    And 오류 메시지에 "별점은 1~5 사이여야 합니다."가 포함되어야 한다