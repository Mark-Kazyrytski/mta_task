package com.learning.bookshop.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.learning.bookshop.service.TenantProvisioningService;
import liquibase.exception.LiquibaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/callback/tenant/")
@RequiredArgsConstructor
public class TenantProvisioningController {

    private static final String APP_ROUTER_DOMAIN_NAME = "-dev-bookshop-mt-app.cfapps.us10-001.hana.ondemand.com";
    private static final String HTTPS = "https://";

    private final TenantProvisioningService tenantProvisioningService;

    @PutMapping("/{tenantId}")
    public ResponseEntity<String> subscribeTenant(@RequestBody final JsonNode requestBody,
                                                  @PathVariable final String tenantId)
            throws SQLException, LiquibaseException {

        log.info("Tenant callback service was called with method PUT for tenant {}.", tenantId);
        String subscribedSubdomain = requestBody.get("subscribedSubdomain").asText();
        tenantProvisioningService.subscribeTenant(tenantId);
        log.info("Tenant[id={}] was successfully created.", tenantId);
        return ResponseEntity.ok(generateTenantURL(subscribedSubdomain));
    }

    @DeleteMapping("/{tenantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> unsubscribeTenant(@PathVariable final String tenantId) throws SQLException {
        log.info("Tenant callback service was called with method DELETE for tenant {}.", tenantId);
        tenantProvisioningService.unsubscribeTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    private String generateTenantURL(final String subscribedSubdomain) {
        return HTTPS + subscribedSubdomain + APP_ROUTER_DOMAIN_NAME;
    }
}