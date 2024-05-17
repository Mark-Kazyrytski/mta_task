package com.learning.bookshop.service.impl;

import com.learning.bookshop.service.SchemaNameResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

@Component
public class SchemaNameResolverImpl implements SchemaNameResolver {

    @Nonnull
    @Override
    public String resolveSchemaNameForTenantId(@Nonnull final String tenantId) {
        Assert.notNull(tenantId, "tenantId cannot be null");

        return String.format("tenant_%s", tenantId.replace("-", "_"));
    }

}
