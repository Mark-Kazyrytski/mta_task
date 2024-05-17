package com.learning.bookshop.facade.impl;

import com.learning.bookshop.facade.BooksFacade;
import com.learning.bookshop.facade.converter.Converter;
import com.learning.bookshop.facade.data.BookData;
import com.learning.bookshop.facade.payload.CreateBookPayload;
import com.learning.bookshop.model.BookModel;
import com.learning.bookshop.service.BookService;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class BookFacadeImpl implements BooksFacade {

    private final BookService bookService;
    private final Converter<CreateBookPayload, BookModel> createBookPayloadToBookModelConverter;
    private final Converter<BookModel, BookData> bookModelToDataConverter;

    @Nonnull
    @Override
    public BookData createBook(@Nonnull final CreateBookPayload payload) {
        Assert.notNull(payload, "payload cannot be null");

        final BookModel bookToCreate = createBookPayloadToBookModelConverter.convert(payload);
        final BookModel createdBook = bookService.createBook(bookToCreate);
        return bookModelToDataConverter.convert(createdBook);
    }

    @Nonnull
    @Override
    public BookData getBookById(@Nonnull final Long id) {
        Assert.notNull(id, "id cannot be null");

        final BookModel book = bookService.getBookById(id);

        return bookModelToDataConverter.convert(book);
    }
}
