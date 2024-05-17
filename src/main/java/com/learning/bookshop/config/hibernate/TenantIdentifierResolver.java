package com.learning.bookshop.config.hibernate;

import com.sap.cloud.security.xsuaa.token.AuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    @Value("${multitenant.defaultTenant}")
    private String defaultTenant;

    @Override
    public String resolveCurrentTenantIdentifier() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(AuthenticationToken.class::isInstance)
                .map(AuthenticationToken.class::cast)
                .map(AuthenticationToken::getTokenAttributes)
                .map(tokenAttributes -> (String) tokenAttributes.get("zid"))
                .filter(this::isValidTenant)
                .orElse(defaultTenant);
    }

    private boolean isValidTenant(String tenant) {
        return Objects.nonNull(tenant) && !Objects.equals("sap-provisioning", tenant);
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
