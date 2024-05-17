package com.learning.bookshop.service;

import javax.annotation.Nonnull;

public interface SchemaNameResolver {

    @Nonnull
    String resolveSchemaNameForTenantId(@Nonnull String tenantId);

}
