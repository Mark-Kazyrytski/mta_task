package com.learning.bookshop.facade.converter;

import javax.annotation.Nonnull;

public interface Converter<S, T> {

    @Nonnull
    T convert(@Nonnull S source);

}
