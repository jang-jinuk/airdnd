package com.dmz.airdnd.stepdefinitions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.dmz.airdnd.AbstractContainerBase;
import com.dmz.airdnd.common.auth.AuthService;
import com.dmz.airdnd.user.domain.Role;
import com.dmz.airdnd.user.domain.User;
import com.dmz.airdnd.user.dto.request.UserRequest;
import com.dmz.airdnd.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@CucumberContextConfiguration
public class SignUpStepDef extends AbstractContainerBase {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	private UserRequest request;
	private ResultActions resultActions;
	@Autowired
	private ObjectMapper objectMapper;

	@After
	public void tearDown() {
		userRepository.deleteAll();
	}

	@Given("유효한 회원가입 요청 정보가 준비되어 있다.")
	public void 유효한_회원가입_요청_정보_준비되어_있다() {
		request = new UserRequest(
			"user123", "pass!234", "user@example.com", "01012345678"
		);
	}

	@Given("저장소에 동일한 loginId, email, phone을 가진 유저가 존재하지 않는다.")
	public void 저장소에_동일한_유저가_존재하지_않는다() {
		userRepository.deleteAll();
	}

	@When("사용자가 회원가입 API를 호출했을 때")
	public void 사용자가_회원가입_API를_호출하면() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		resultActions = mockMvc.perform(post("/api/auth/signup")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json));
	}

	@Then("응답 상태로 201 Created를 받는다.")
	public void 회원가입_반환_정보_비교() throws Exception {
		resultActions.andExpect(status().isCreated());
	}

	@Then("응답 본문은 success는 true이고 data와 error는 null이어야 한다.")
	public void 응답_본문은_다음과_같아야_한다() throws Exception {
		resultActions
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data").value(nullValue()))
			.andExpect(jsonPath("$.error").value(nullValue()));
	}

	@Given("저장소에 {string}가 중복된 유저가 등록되어 있다.")
	public void 저장소에_중복된_유저가_등록되어_있다(String field) {
		User.UserBuilder builder = User.builder()
			.loginId("otherUserId")
			.password("otherPassword")
			.email("other@test.com")
			.role(Role.USER)
			.phone("01098765432");

		User user = switch (field) {
			case "loginId" -> builder
				.loginId("user123")
				.build();
			case "email" -> builder
				.email("user@example.com")
				.build();
			case "phone" -> builder
				.phone("01012345678")
				.build();
			default -> builder.build();
		};
		userRepository.save(user);
	}

	@Then("오류 코드는 {string}이고, 메시지는 {string}이어야 한다.")
	public void 오류_메시지_검증(String code, String expected) throws Exception {
		resultActions
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.data").isEmpty())
				.andExpect(jsonPath("$.error").isNotEmpty())
				.andExpect(jsonPath("$.error.code").value(code))
				.andExpect(jsonPath("$.error.message").value(expected));
	}
}
