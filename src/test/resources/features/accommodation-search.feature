#Feature: 숙소 검색 및 필터링
#
#  Scenario: 위치, 날짜, 인원 수로 숙소 검색 결과를 반환한다
#    Given "서울" 지역으로 2025-07-01 부터 2025-07-05까지, 게스트 2명으로 검색했을 때
#    When GET "/api/accommodations?location=서울&start=2025-07-01&end=2025-07-05&guests=2" 요청을 하면
#    Then 응답 상태 코드는 200 OK여야 한다
#    And 응답 바디에 최소 하나 이상의 숙소 리스트가 포함되어야 한다
#
#  Scenario: 검색 결과가 없으면 빈 리스트를 반환한다
#    Given 존재하지 않는 location="지구끝"으로 검색했을 때
#    When GET "/api/accommodations?location=지구끝" 요청을 하면
#    Then 응답 상태 코드는 200 OK여야 한다
#    And 응답 바디에 빈 리스트([])가 반환되어야 한다