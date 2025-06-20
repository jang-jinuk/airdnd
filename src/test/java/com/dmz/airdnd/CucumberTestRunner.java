package com.dmz.airdnd;

import static io.cucumber.junit.platform.engine.Constants.*;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:build/reports/cucumber.html, summary")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.dmz.airdnd.stepdefinitions")
public class CucumberTestRunner {
	//	Cucumber 테스트 러너
}
