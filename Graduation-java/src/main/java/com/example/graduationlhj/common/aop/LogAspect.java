package com.example.graduationlhj.common.aop;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LogAspect {

    // 定义切点
    @Pointcut("@annotation(com.example.graduationlhj.common.aop.LogAnnotation)")
    public void pt() {
    }

    @Around("pt()")
    public Object log(ProceedingJoinPoint pjp) {
        Object proceed = null;
        try {
            long beginTime = System.currentTimeMillis();
            // 执行方法
            proceed = pjp.proceed();
            // 执行时长
            long time = System.currentTimeMillis() - beginTime;
            // 保存日志
            recordLog(pjp, time);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return proceed;
    }

    private void recordLog(ProceedingJoinPoint pjp, long time) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        log.info("=====================log start================================");
        log.info("module: {}", logAnnotation.module());
        log.info("operation: {}", logAnnotation.operator());

        //请求的方法名
        String className = pjp.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("request method: {}", className + "." + methodName + "()");

//        //请求的参数
        Object[] args = pjp.getArgs();
        if (args.length != 0) {
            String params = JSON.toJSONString(args[0]);
            log.info("params: {}", params);
        } else {
            log.info("params: ");
        }


        log.info("execute time: {} ms", time);
        log.info("=====================log end================================");
    }
}
