Feature: User Signup

  Scenario: 중복된 유저가 없으면 회원가입을 성공한다.
    Given 유효한 회원가입 요청 정보가 준비되어 있다.
    And 저장소에 동일한 loginId, email, phone을 가진 유저가 존재하지 않는다.
    When 사용자가 회원가입 API를 호출했을 때
    Then 응답 상태로 201 Created를 받는다.
    And 응답 본문은 success는 true이고 data와 error는 null이어야 한다.

  Scenario Outline: 중복된 필드가 있을 경우 회원가입에 실패한다.
    Given 유효한 회원가입 요청 정보가 준비되어 있다.
    And 저장소에 "<field>"가 중복된 유저가 등록되어 있다.
    When 사용자가 회원가입 API를 호출했을 때
    Then 오류 코드는 "<code>"이고, 메시지는 "<message>"이어야 한다.

    Examples:
      | field   | code               | message             |
      | loginId | DUPLICATE_LOGIN_ID | 이미 존재하는 로그인 아이디입니다. |
      | email   | DUPLICATE_EMAIL    | 이미 존재하는 이메일입니다.     |
      | phone   | DUPLICATE_PHONE    | 이미 존재하는 전화번호입니다.    |
