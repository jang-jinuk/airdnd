package com.dmz.airdnd.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

	// 컨트롤러와 서비스 레이어 전체 대상
	@Pointcut("execution(* com.dmz.airdnd..controller..*(..)) || execution(* com.dmz.airdnd..service..*(..))")
	public void applicationLayer() {
	}

	// 메서드 실행 전 요청 로그
	@Before("applicationLayer()")
	public void logRequest(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().toShortString();
		Object[] args = joinPoint.getArgs();
		log.info("▶️요청 - {} | args = {}", methodName, args);
	}

	// 메서드 정상 리턴 후 응답 로그
	@AfterReturning(pointcut = "applicationLayer()", returning = "result")
	public void logResponse(JoinPoint joinPoint, Object result) {
		String methodName = joinPoint.getSignature().toShortString();
		log.info("✅응답 - {} | result = {}", methodName, result);
	}

	// 예외 발생 시 로그
	@AfterThrowing(pointcut = "applicationLayer()", throwing = "e")
	public void logException(JoinPoint joinPoint, Throwable e) {
		String methodName = joinPoint.getSignature().toShortString();
		log.error("❌예외 - {} | message = {}", methodName, e.getMessage(), e);
	}

	// 실행 시간 측정 로그
	@Around("applicationLayer()")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		Object result = joinPoint.proceed();  // 실제 메서드 실행

		long end = System.currentTimeMillis();
		String methodName = joinPoint.getSignature().toShortString();
		log.info("⏱️실행 시간 - {} | {} ms", methodName, (end - start));

		return result;
	}
}
