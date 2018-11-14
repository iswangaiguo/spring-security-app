package com.imooc.security.app.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Controller;

import com.imooc.security.core.properties.LoginType;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.support.SimpleResponse;

@Controller("imoocAuthenticationFailureHandler")
public class ImoocAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private static final Logger log = LoggerFactory.getLogger(ImoocAuthenticationFailureHandler.class);

	@Autowired
	private SecurityProperties securityProperties;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.info("登陆失败");
		if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(new ObjectMapper().writeValueAsString(new SimpleResponse(exception.getMessage())));;
		} else {
			 super.onAuthenticationFailure(request, response, exception);
		}
	}

}
