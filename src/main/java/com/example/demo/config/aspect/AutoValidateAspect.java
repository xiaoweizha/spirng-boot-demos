package com.example.demo.config.aspect;

import com.example.demo.common.dto.BaseResponse;
import com.example.demo.config.aspect.annotation.AutoValidate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Parameter;
import java.util.Set;

@Component
@Aspect
public class AutoValidateAspect {
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 切入点
     */
    @Pointcut(value = "execution(* com.example.demo..*.*(..))")
    public void autoValidatePointcut() {
    }

    /**
     * 环绕通知：验证有@AutoValidate注解的方法中被@RequestBody注解修饰对象的参数的合法性。
     * 常用的正则注解参考包：javax.validation.constraints,
     *
     * @throws Throwable e
     */
    @Around(value = "autoValidatePointcut() && @annotation(autoValidate)", argNames = "joinPoint,autoValidate")
    public Object autoValidateAround(ProceedingJoinPoint joinPoint, AutoValidate autoValidate) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        //获取方法的所有参数
        Parameter[] parameters = methodSignature.getMethod().getParameters();
        Object[] args = joinPoint.getArgs();
        Class targetParamType = autoValidate.targetParamType();
        //获取被@RequestBody注解修饰的参数
        for(int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if(void.class.equals(targetParamType) && parameter.getDeclaredAnnotation(RequestBody.class) != null) {
                //只有一个或多个@RequestBody入参，且有validate注解的修饰的实体类用不带属性的@AutoValidate
                BaseResponse baseResponse = this.validate(methodSignature, args[i]);
                if(baseResponse != null) {
                    return baseResponse;
                }
            } else if(!void.class.equals(targetParamType) && targetParamType.equals(parameter.getClass())) {
                //只有多个@RequestBody入参，且指定其中一个@RequestBody实体类进行校验，则用带徐行的人@AutoValidate
                BaseResponse baseResponse = this.validate(methodSignature, args[i]);
                if(baseResponse != null) {
                    return baseResponse;
                }
            }
        }
        return joinPoint.proceed();
    }

    private BaseResponse validate(MethodSignature methodSignature, Object targetArg) throws Exception {
        BaseResponse baseResponse = null;
        //对参数进行验证
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(targetArg);
        //若参数不合法，返回错误信息
        if(!constraintViolationSet.isEmpty()) {
            ConstraintViolation<Object> first = constraintViolationSet.iterator().next();
            if(BaseResponse.class.isAssignableFrom(methodSignature.getReturnType())) {//判断返回类型是否继承BaseResponse
                baseResponse = (BaseResponse) methodSignature.getReturnType().newInstance();
                baseResponse.setCode(-2);
                baseResponse.setMsg(first.getMessage());
            } else {
                throw new Exception("被@AutoValidate修饰的方法的返回值必须为BaseResponse类型");
            }
        }
        return baseResponse;
    }

}
