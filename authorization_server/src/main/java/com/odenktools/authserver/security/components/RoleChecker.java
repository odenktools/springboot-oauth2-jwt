package com.odenktools.authserver.security.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component("roleChecker")
public class RoleChecker {

  /**
   * We define SUPER ADMIN ROLE to allow all action.
   * This Role must insert to database.
   */
  private static String SUPERADMIN = "ROLE_SUPER_ADMIN";

  /**
   * We define ROLE_MOBILE to allow action.
   * This Role must insert to database.
   */
  public static String ROLE_MOBILE = "ROLE_MOBILE:%s";

  private static final Logger LOG = LoggerFactory.getLogger(RoleChecker.class);

  public static boolean hasValidRole(Principal principal) {

    return hasValidRole(principal, null, null);
  }

  public static boolean hasValidRole(Principal principal, String user) {

    return hasValidRole(principal, null, user);
  }

  public static boolean hasValidRole(Principal principal, String company, String user) {

    OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
    for (GrantedAuthority ga : oAuth2Authentication.getAuthorities()) {
      LOG.info("ROLE_PRINCIPAL {}", ga.getAuthority());
      // Login using SUPERADMIN???
      if (ga.getAuthority().equalsIgnoreCase(SUPERADMIN)) {
        return true;
      }
      else if (user != null &&
          ga.getAuthority().equalsIgnoreCase(String.format(ROLE_MOBILE, user.toUpperCase()))) {
        return true;
      }
      else {
        throw new ResourceUnauthorizedException();
      }
    }
    throw new ResourceUnauthorizedException();
  }
}
