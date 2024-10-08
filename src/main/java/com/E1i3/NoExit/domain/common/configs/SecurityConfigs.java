package com.E1i3.NoExit.domain.common.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.E1i3.NoExit.domain.common.auth.JwtAuthFilter;

@Configuration
@EnableWebSecurity	// security 관련한 코드
@EnableGlobalMethodSecurity(prePostEnabled = true)	// pre: 사전 검증, post: 사후 검증
public class SecurityConfigs {

	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity

				.csrf().disable()
				.cors().and() // CORS 활성화
				.httpBasic().disable()
				.authorizeRequests()
				.antMatchers(
						"/email/**",
						"/",
						"/doLogin","/owner/create", "/game/list","/findboard/list", "/game/detail/**", "/game/**", "/findboard/create",
						// 김민성 : Swagger 관련 경로를 허용 , 접속 경로 : http://localhost:8080/swagger-ui/#/
						"/member/create", "/swagger-ui/**",
						"/review/all","/review/all/**","/review/count","/review/game/**",
						"/swagger-resources/**",
						"/swagger-ui.html",
						"/v2/api-docs",
						"/webjars/**",
						"/board/list", "/board/detail/**",
						"/webjars/**",
						//웹소켓 test 403 해결
						"/ws-chat/**",
//						"/chat/**", "/ws"
						"/ws-chat/**",
						"/store/list", //매장 정보 접근 권한 추가 김민성
						"/attendance/list",// 참석자 정보 접근 권한 추가 김민성
						"/attendance/check" // 참석자 중복 체크 접근 권한 추가 김민성
				).permitAll()
				.antMatchers("/reservation/create").hasRole("USER")
				.antMatchers("/reservation/storeReservation").hasRole("OWNER")
				.antMatchers("/chat/**").authenticated()
				.anyRequest().authenticated()
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
}
