package com.enotes.service.impl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.enotes.entity.User;
import com.enotes.exception.JwtTokenExpiredException;
import com.enotes.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

	private String secretKey;

	public JwtServiceImpl() {

		try {
			// everytime new secret key will be generated
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGen.generateKey();
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", user.getId());
		claims.put("role", user.getRoles());
		claims.put("status", user.getStatus().getIsActive());
		String token = Jwts.builder().claims().add(claims).subject(user.getEmail())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 60 * 10)).and().signWith(getKey())
				.compact();
		return token;
	}

	@Override
	public String extractUsername(String token) {
		Claims claims = extractAllClaims(token);
		String email = claims.getSubject();
		return email;
	}

	public String getRoleFromToken(String token) {
		Claims claims = extractAllClaims(token);
		String role = (String) claims.get("role");
		return role;
	}

	@Override
	public Boolean validateToken(String token, UserDetails userDetails) {

		String username = extractUsername(token);
		Boolean isExpired = isTokenExpired(token);

		if (username.equalsIgnoreCase(userDetails.getUsername()) && !isExpired) {
			return true;
		}
		return false;
	}

	private Boolean isTokenExpired(String token) {
		Claims claims = extractAllClaims(token);
		Date expiryDate = claims.getExpiration();
		return expiryDate.before(new Date());
	}

	private Claims extractAllClaims(String token) {
		try {
			Claims claims = Jwts.parser().verifyWith(decryptKey(secretKey)).build().parseSignedClaims(token)
					.getPayload();
			return claims;
		} catch (ExpiredJwtException e) {
			throw new JwtTokenExpiredException("Token is expired");
		} catch (JwtException e) {
			throw new JwtTokenExpiredException("Invalid jwt token");
		} catch (Exception e) {
			throw e;
		}
	}

	private SecretKey decryptKey(String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		SecretKey hmacShaKeyFor = Keys.hmacShaKeyFor(keyBytes);
		return hmacShaKeyFor;
	}

	private Key getKey() {
		// decode the secret key
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
