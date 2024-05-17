package com.learning.bookshop.service;

import com.learning.bookshop.model.BookModel;
import javax.annotation.Nonnull;

public interface BookService {

    @Nonnull
    BookModel createBook(@Nonnull BookModel book);

    @Nonnull
    BookModel getBookById(@Nonnull Long id);

}
