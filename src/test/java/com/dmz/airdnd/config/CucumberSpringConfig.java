package com.dmz.airdnd.config;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.dmz.airdnd.AbstractContainerBase;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CucumberSpringConfig extends AbstractContainerBase {
	// 이 클래스는 별도의 코드 없이 어노테이션만 있으면 됩니다.
	// 필요하다면 @ActiveProfiles 등도 추가할 수 있습니다.
}
