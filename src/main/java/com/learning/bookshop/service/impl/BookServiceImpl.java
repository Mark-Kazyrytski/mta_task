package com.learning.bookshop.service.impl;

import com.learning.bookshop.model.BookModel;
import com.learning.bookshop.repository.BookRepository;
import com.learning.bookshop.service.BookService;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Nonnull
    @Override
    public BookModel createBook(@Nonnull final BookModel book) {
        Assert.notNull(book, "book cannot be null");

        return bookRepository.save(book);
    }

    @Nonnull
    @Override
    public BookModel getBookById(@Nonnull final Long id) {
        Assert.notNull(id, "id cannot be null");

        return bookRepository.getReferenceById(id);
    }
}
