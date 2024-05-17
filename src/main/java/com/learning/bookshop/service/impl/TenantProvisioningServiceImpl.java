package com.learning.bookshop.service.impl;

import com.learning.bookshop.service.SchemaNameResolver;
import com.learning.bookshop.service.TenantProvisioningService;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class TenantProvisioningServiceImpl implements TenantProvisioningService {

    private static final String LIQUIBASE_PATH = "db/changelog/db.changelog-master.yaml";
    private static final Pattern TENANT_PATTERN = Pattern.compile("[-\\w]+");

//    @Value("${spring.liquibase.change-log}")
//    private String liquibasePath;
    private final DataSource dataSource;
    private final SchemaNameResolver schemaNameResolver;

    @Override
    public void subscribeTenant(@Nonnull final String tenantId) throws SQLException, LiquibaseException {
        Validate.isTrue(isValidTenantId(tenantId), String.format("Invalid tenant id: \"%s\"", tenantId));
        String schemaName = schemaNameResolver.resolveSchemaNameForTenantId(tenantId);

        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(String.format("CREATE SCHEMA IF NOT EXISTS \"%s\"", schemaName));
            log.info("SCHEMA with name: {} was created", schemaName);
            runLiquibaseScript(connection, schemaName);
        } catch (final Exception e) {
            log.error("TenantProvisioningService: Tenant subscription failed for {}.", tenantId, e);
            throw e;
        }
    }

    private void runLiquibaseScript(final Connection connection, final String schemaName) throws LiquibaseException, SQLException {
        connection.setSchema(schemaName);
        final ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(getClass().getClassLoader());
        final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        final Liquibase liquibase = new Liquibase(LIQUIBASE_PATH, resourceAccessor, database);
        liquibase.update(schemaName);
        log.info("Initial script for schema: {} was performed successfully", connection.getSchema());
        connection.commit();
    }

    @Override
    public void unsubscribeTenant(@Nonnull final String tenantId) throws SQLException {
        Validate.isTrue(isValidTenantId(tenantId), String.format("Invalid tenant id: \"%s\"", tenantId));
        final String schemaName = schemaNameResolver.resolveSchemaNameForTenantId(tenantId);

        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(String.format("DROP SCHEMA IF EXISTS \"%s\" CASCADE", schemaName));
            log.info("SCHEMA with name: {} was deleted successfully", schemaName);
        } catch (final SQLException e) {
            log.error("Tenant unsubscription failed for {}.", tenantId, e);
            throw e;
        }
    }

    private boolean isValidTenantId(final String tenantId) {
        return tenantId != null && TENANT_PATTERN.matcher(tenantId).matches();
    }
}