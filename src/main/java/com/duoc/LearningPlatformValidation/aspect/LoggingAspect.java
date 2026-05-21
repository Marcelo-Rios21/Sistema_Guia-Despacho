package com.duoc.LearningPlatformValidation.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.duoc.LearningPlatformValidation.controller..*(..)) || execution(* com.duoc.LearningPlatformValidation.service..*(..))")
    public Object registrarEjecucion(ProceedingJoinPoint joinPoint) throws Throwable {

        String nombreClase = joinPoint.getSignature().getDeclaringTypeName();
        String nombreMetodo = joinPoint.getSignature().getName();

        logger.info("Iniciando ejecución: {}.{}", nombreClase, nombreMetodo);

        long inicio = System.currentTimeMillis();

        try {
            Object resultado = joinPoint.proceed();

            long tiempoTotal = System.currentTimeMillis() - inicio;
            logger.info("Finalizó ejecución: {}.{} en {} ms", nombreClase, nombreMetodo, tiempoTotal);

            return resultado;

        } catch (Exception e) {
            logger.error("Error en ejecución: {}.{} - {}", nombreClase, nombreMetodo, e.getMessage());
            throw e;
        }
    }
}
