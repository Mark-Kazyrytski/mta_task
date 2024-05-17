package com.learning.bookshop.service;

import liquibase.exception.LiquibaseException;

import javax.annotation.Nonnull;
import java.sql.SQLException;

public interface TenantProvisioningService {

    void subscribeTenant(@Nonnull String tenantId) throws SQLException, LiquibaseException;

    void unsubscribeTenant(@Nonnull String tenantId) throws SQLException;

}
