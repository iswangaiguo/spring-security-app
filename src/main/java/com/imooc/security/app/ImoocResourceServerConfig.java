package com.imooc.security.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

import com.imooc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.ValidateCodeSecurityConfig;

@Configuration
@EnableResourceServer
public class ImoocResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	protected AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;
	
	@Autowired
	protected AuthenticationFailureHandler imoocAuthenticationFailureHandler;
	
	@Autowired
	SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
	
	@Autowired
	ValidateCodeSecurityConfig validateCodeSecurityConfig;
	
	@Autowired
	private SpringSocialConfigurer imoocSocialSecurityConfig;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.formLogin()
		.loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
		.loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
		.successHandler(imoocAuthenticationSuccessHandler)
		.failureHandler(imoocAuthenticationFailureHandler);
		
		http//.apply(validateCodeSecurityConfig)
			//.and()
		.apply(smsCodeAuthenticationSecurityConfig)
			.and()	
		.apply(imoocSocialSecurityConfig)
			.and()
		.authorizeRequests()
			.antMatchers(
						SecurityConstants.DEFAULT_UNAUTHENTICATION_URL, 
						//SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
						securityProperties.getBrowser().getLoginPage(),
						SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
						securityProperties.getBrowser().getSignUpUrl(),
						"/user/regist",
						"/social/user",
						"/session/invalid")
						.permitAll()
			.anyRequest()
			.authenticated()
			.and()
		.csrf().disable();
			
		}
}
