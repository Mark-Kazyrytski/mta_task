package com.learning.bookshop.facade.converter;

import com.learning.bookshop.facade.data.BookData;
import com.learning.bookshop.model.BookModel;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class BookModelToDataConverter implements Converter<BookModel, BookData> {

    @Override
    @Nonnull
    public BookData convert(@Nonnull final BookModel source) {
        Assert.notNull(source, "source cannot be null");

        return BookData.builder()
                .id(source.getId())
                .title(source.getTitle())
                .description(source.getDescription())
                .build();
    }

}
