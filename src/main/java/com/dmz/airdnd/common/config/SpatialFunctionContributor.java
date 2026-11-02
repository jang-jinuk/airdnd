package com.dmz.airdnd.common.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Component;

@Component
public class SpatialFunctionContributor implements FunctionContributor {

	@Override
	public void contributeFunctions(FunctionContributions functionContributions) {
		functionContributions.getFunctionRegistry().registerPattern(
			"mbrcontains",
			"MBRContains(?1, ?2)",
			functionContributions.getTypeConfiguration()
				.getBasicTypeRegistry().resolve(StandardBasicTypes.BOOLEAN)
		);
	}
}

