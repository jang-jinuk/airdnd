package com.dmz.airdnd.common.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.dmz.airdnd.common.auth.UserContext;
import com.dmz.airdnd.common.auth.dto.UserInfo;
import com.dmz.airdnd.common.exception.ErrorCode;
import com.dmz.airdnd.common.exception.ForbiddenException;
import com.dmz.airdnd.common.exception.UnauthorizedException;

@Aspect
@Component
public class RoleCheckAspect {
	@Before("@annotation(roleCheck)")
	public void checkRole(RoleCheck roleCheck) {
		UserInfo userInfo = UserContext.get();
		if (userInfo == null)
			throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);

		if (!userInfo.getRole().equals(roleCheck.value())) {
			throw new ForbiddenException(ErrorCode.FORBIDDEN);
		}
	}
}
