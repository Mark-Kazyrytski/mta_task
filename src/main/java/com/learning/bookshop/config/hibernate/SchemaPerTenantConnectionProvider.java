package com.learning.bookshop.config.hibernate;

import com.learning.bookshop.service.SchemaNameResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchemaPerTenantConnectionProvider implements MultiTenantConnectionProvider {

    @Value("${multitenant.defaultTenant}")
    private String defaultTenant;

    private final DataSource dataSource;
    private final SchemaNameResolver schemaNameResolver;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(final Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(final String tenantIdentifier) throws SQLException {
        final Connection connection = getAnyConnection();
        try {
            final String schemaName = tenantIdentifier.equals(defaultTenant) ? defaultTenant :
                    schemaNameResolver.resolveSchemaNameForTenantId(tenantIdentifier);
            connection.setSchema(schemaName);
        } catch (SQLException e) {
            throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e);
        }
        return connection;
    }

    @Override
    public void releaseConnection(final String tenantIdentifier, final Connection connection) throws SQLException {
        try {
            final String schemaName = tenantIdentifier.equals(defaultTenant) ? defaultTenant :
                    schemaNameResolver.resolveSchemaNameForTenantId(tenantIdentifier);
            connection.setSchema(schemaName);
        } catch (SQLException e) {
            throw new HibernateException("SchemaPerTenantConnectionProvider: Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e);
        }
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @Override
    public boolean isUnwrappableAs(Class aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
