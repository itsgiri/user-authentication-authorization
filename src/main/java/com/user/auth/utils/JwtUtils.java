package com.user.auth.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.user.auth.constants.CommonConstants;
import com.user.auth.constants.ErrorMessageConstants;
import com.user.auth.constants.RoleName;
import com.user.auth.entity.User;
import com.user.auth.exception.CustomException;
import com.user.auth.repo.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtils {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Autowired
	PreLoadProperties properties;

	@Autowired
	private UserRepository userRepository;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(properties.getJwtSecret()).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + properties.getJwtExpirationMs()))
				.signWith(SignatureAlgorithm.HS256, properties.getJwtSecret()).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public void validateTokenByUserId(String bearerToken, long userId) {

		String jwtToken = null;
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(CommonConstants.BEARER)) {
			jwtToken = bearerToken.substring(7, bearerToken.length());
		} else {
			throw new CustomException(ErrorMessageConstants.INVALID_TOKEN);
		}

		final String username = extractUsername(jwtToken);
		User user = userRepository.findUserByEmail(username).get();

		if (null == user) {
			throw new CustomException(ErrorMessageConstants.USER_NOT_EXISTS);
		}

		if (1 == user.getRoles().size()
				&& RoleName.ROLE_USER.name().equals(user.getRoles().iterator().next().getRoleName().name())
				&& user.getId() != userId) {
			throw new CustomException(ErrorMessageConstants.ACCESS_DENIED);
		}
		
	}
}
