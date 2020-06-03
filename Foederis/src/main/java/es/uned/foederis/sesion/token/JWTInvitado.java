package es.uned.foederis.sesion.token;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTInvitado {
	
	@Value("${jwt.secretkey}")
	private String SECRET_KEY;

	public String createJWT(long idEvento, String creador, String invitado, long mmCaducidad) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		JwtBuilder builder = Jwts.builder().setId(Long.toString(idEvento))
				.setIssuedAt(now)
				.setSubject(invitado)
				.setIssuer(creador)
				.signWith(signatureAlgorithm, signingKey);

		if (mmCaducidad >= 0) {
			long expMillis = nowMillis + mmCaducidad;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}

		return builder.compact();
	}

	public Claims decodeJWT(String jwt) {
		Claims claims = Jwts.parser()
				.setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
				.parseClaimsJws(jwt).getBody();
		
		return claims;
	}
}
