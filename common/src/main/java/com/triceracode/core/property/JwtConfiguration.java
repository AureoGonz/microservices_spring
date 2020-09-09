package com.triceracode.core.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Configuration
@ConfigurationProperties(prefix = "jwt.config")
@Getter
@Setter
@ToString
public class JwtConfiguration {
	private String loginUri = "/login/**";
	private Header header = new Header();
	private int expiration = 3600;
	private String privateKey = "55mlGLUeR7MAtd1rqtlHHQioM8OCCV7M";
	private String type = "encrypted";
	
	@Getter
	@Setter
	public static class Header {
		private String name = "Authorization";
		private String prefix = "Bearer ";
	}
}
