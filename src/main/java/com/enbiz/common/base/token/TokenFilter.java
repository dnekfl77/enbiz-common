package com.enbiz.common.base.token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.enbiz.common.base.util.AuthUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * api 서버간 restapi 호출 시 인증을 위한 토큰 필터
 * 
 * @author choiyh44
 *
 */
public class TokenFilter extends GenericFilterBean {
	private TokenServiceForFilter tokenService;

	public TokenFilter(TokenServiceForFilter tokenService) {
		this.tokenService = tokenService;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		Optional<String> jwtOpt = AuthUtils.resolveToken((HttpServletRequest) request);
		if (jwtOpt.isPresent() && tokenService.verifyToken(jwtOpt.get())) {
			Jws<Claims> jws = tokenService.parseToken(jwtOpt.get());
			String username = (String) jws.getBody().get("username");
			Long id = (Long) jws.getBody().get("id");
			List<String> roles = (List<String>) jws.getBody().get("roles");
			List<GrantedAuthority> authorities = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(roles)) {
				roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
			}
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					UserDetail.builder().username(username).id(id).build(), "", authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		chain.doFilter(request, response);
	}

}
