package app.config;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5116773170960037378L;
	static final String CLAM_KEY_USERNAME = "sub";
	static final String CLAM_KEY_AUDIENCE = "audience";
	static final String CLAM_KEY_CREATED = "created";
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	public String getUsernameFromToken(String token) {
		String username = null;
		try {
			final Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
			
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	private Claims getClaimsFromToken(String token) {
		// TODO Auto-generated method stub
		Claims claims = null;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		}catch(Exception e) {
			claims = null;
		}
		return claims;
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		// TODO Auto-generated method stub
		JwtUser user = (JwtUser)userDetails;
		final String username = getUsernameFromToken(token);
		return (username.equals(user.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		// TODO Auto-generated method stub
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Date getExpirationDateFromToken(String token) {
		// TODO Auto-generated method stub
		Date expiration = null;
		try {
			final Claims claims = getClaimsFromToken(token);
			if(claims != null) {
				expiration = claims.getExpiration();
			} else {
				expiration = null;
			}
		} catch(Exception e) {
			expiration = null;
		} 
		return null;
	}

	public String generateToken(JwtUser userDetails) {
		// TODO Auto-generated method stub
		Map<String, Object> clamis = new HashMap<String, Object>();
		clamis.put(CLAM_KEY_USERNAME, userDetails.getUsername());
		clamis.put(CLAM_KEY_CREATED, new Date());
		
		return generateToken(clamis);
	}

	private String generateToken(Map<String, Object> clamis) {
		// TODO Auto-generated method stub
		return Jwts.builder().setClaims(clamis).setExpiration(generateExpirationDate()).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	private Date generateExpirationDate() {
		// TODO Auto-generated method stub
		return new Date(System.currentTimeMillis()+ expiration * 1000);
	}
}
