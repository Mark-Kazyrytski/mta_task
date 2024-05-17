package com.learning.bookshop.facade;

import com.learning.bookshop.facade.data.BookData;
import com.learning.bookshop.facade.payload.CreateBookPayload;
import com.learning.bookshop.model.BookModel;
import javax.annotation.Nonnull;

public interface BooksFacade {

    @Nonnull
    BookData createBook(@Nonnull CreateBookPayload payload);

    @Nonnull
    BookData getBookById(@Nonnull Long id);

}
