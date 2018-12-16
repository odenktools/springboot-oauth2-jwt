package com.odenktools.authserver.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * @author Odenktools.
 */
@JsonSerialize
@ToString
@Entity(name = "OauthClientDetails")
@Table(name = "oauth_client_details")
@SuppressWarnings("serial")
public class OauthClientDetails implements ClientDetails, Serializable {

	private static final long serialVersionUID = 1L;

	private static final ObjectMapper mapper = new ObjectMapper();

	@Id
	@Column(nullable = false, unique = true)
	private String clientId;

	@Column(name = "client_secret", nullable = false)
	private String clientSecret;

	@Column(name = "resource_ids")
	private String resourceIds;


	@Column(name = "scope")
	private String scope;

	@Column(name = "authorized_grant_types", nullable = false)
	private String authorizedGrantTypes;

	@Column(name = "web_server_redirect_uri")
	private String registeredRedirectUri;

	@Column(name = "authorities")
	private String authorities;

	@Column(name = "access_token_validity", nullable = false)
	private Integer accessTokenValiditySeconds;

	@Column(name = "refresh_token_validity", nullable = false)
	private Integer refreshTokenValiditySeconds;

	@Column(name = "autoapprove", nullable = false)
	private String autoApproveScope;

	@Column
	private String additionalInformation;

	@Override
	public String getClientId() {
		return this.clientId;
	}

	@Override
	public Set<String> getResourceIds() {
		if (StringUtils.isEmpty(this.resourceIds)) {
			return new HashSet<>();
		} else {
			return StringUtils.commaDelimitedListToSet(this.resourceIds);
		}
	}

	@Override
	public boolean isSecretRequired() {
		return !StringUtils.isEmpty(this.clientSecret);
	}

	@Override
	public String getClientSecret() {
		return this.clientSecret;
	}

	@Override
	public boolean isScoped() {
		return this.getScope().size() > 0;
	}

	@Override
	public Set<String> getScope() {
		return StringUtils.commaDelimitedListToSet(this.scope);
	}

	@Override
	public Set<String> getAuthorizedGrantTypes() {
		return StringUtils.commaDelimitedListToSet(this.authorizedGrantTypes);
	}

	@Override
	public Set<String> getRegisteredRedirectUri() {
		return StringUtils.commaDelimitedListToSet(this.registeredRedirectUri);
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		Set<String> set = StringUtils.commaDelimitedListToSet(this.authorities);
		Set<GrantedAuthority> result = new HashSet<>();
		set.forEach(authority -> result.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return authority;
			}
		}));
		return result;
	}

	@Override
	public Integer getAccessTokenValiditySeconds() {
		return this.accessTokenValiditySeconds;
	}

	@Override
	public Integer getRefreshTokenValiditySeconds() {
		return this.refreshTokenValiditySeconds;
	}

	@Override
	public boolean isAutoApprove(String scope) {
		return this.getAutoApproveScope().contains(scope);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> getAdditionalInformation() {
		try {
			return mapper.readValue(this.additionalInformation, Map.class);
		} catch (IOException e) {
			return new HashMap<>();
		}
	}

	public Set<String> getAutoApproveScope() {
		return StringUtils.commaDelimitedListToSet(this.autoApproveScope);
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setResourceIds(Set<String> resourceIds) {
		this.resourceIds = StringUtils.collectionToCommaDelimitedString(resourceIds);
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setScope(Set<String> scope) {
		this.scope = StringUtils.collectionToCommaDelimitedString(scope);
	}

	public void setAuthorizedGrantTypes(Set<String> authorizedGrantType) {
		this.authorizedGrantTypes = StringUtils.collectionToCommaDelimitedString(authorizedGrantType);
	}

	public void setRegisteredRedirectUri(Set<String> registeredRedirectUriList) {
		this.registeredRedirectUri = StringUtils.collectionToCommaDelimitedString(registeredRedirectUriList);
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = StringUtils.collectionToCommaDelimitedString(authorities);
	}

	public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}

	public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}

	public void setAutoApproveScope(Set<String> autoApproveScope) {
		this.autoApproveScope = StringUtils.collectionToCommaDelimitedString(autoApproveScope);
	}

	public void setAdditionalInformation(Map<String, Object> additionalInformation) {
		try {
			this.additionalInformation = mapper.writeValueAsString(additionalInformation);
		} catch (IOException e) {
			this.additionalInformation = "";
		}
	}
}