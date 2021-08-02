package com.rticcle.server.config.auth

import com.rticcle.server.config.auth.dto.SessionUser
import lombok.RequiredArgsConstructor
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpSession


@RequiredArgsConstructor
@Component
class LoginUserArgumentResolver : HandlerMethodArgumentResolver {
    private val httpSession: HttpSession? = null
    override fun supportsParameter(parameter: MethodParameter): Boolean {

        // 파라미터에 @LoginUser 어노테이션이 붙어있는지?
        val isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser::class.java) != null

        // 파라미터의 클래스 타입이 dto.SessionUser.class 인지?
        val isUserClass = SessionUser::class.java == parameter.getParameterType()
        return isLoginUserAnnotation && isUserClass
    }

    @Throws(Exception::class)
    override fun resolveArgument(
        parameter: MethodParameter?,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest?,
        binderFactory: WebDataBinderFactory?
    ): Any {

        // 파라미터에 전달할 객체 생성
        return httpSession!!.getAttribute("user")
    }
}