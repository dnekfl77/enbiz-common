package com.x2bee.common.base.token;

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
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.x2bee.common.base.util.AuthUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * api 서버간 restapi 호출 시 인증을 위한 토큰 필터
 * @author choiyh44
 *
 */
public class TokenFilter extends GenericFilterBean {
	private TokenServiceForFilter tokenService;

	public TokenFilter(TokenServiceForFilter tokenService) {
		this.tokenService = tokenService;
	}
	
    @Override
    public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) 
    		throws IOException, ServletException {

    	Optional<String> jwtOpt = AuthUtil.resolveToken((HttpServletRequest)request);
    	if (jwtOpt.isPresent() && tokenService.verifyToken(jwtOpt.get())) {
    		Jws<Claims> jws = tokenService.parseToken(jwtOpt.get());
    		String userName = (String)jws.getBody().get("userName");
    		String mbrNo = (String)jws.getBody().get("mbrNo");
    		List<String> roles = (List<String>)jws.getBody().get("roles");
	    	List<GrantedAuthority> authorities = new ArrayList<>();
	    	if (CollectionUtils.isNotEmpty(roles)) {
	    		roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
	    	}
	    	UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(new UserDetail().setUserName(userName).setMbrNo(mbrNo), "", authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
	    	
    	}
    	
    	chain.doFilter(request, response);
    }
    
}
