package com.learning.bookshop.facade.converter;

import com.learning.bookshop.facade.payload.CreateBookPayload;
import com.learning.bookshop.model.BookModel;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CreateBookPayloadToBookModelConverter implements Converter<CreateBookPayload, BookModel> {

    @Nonnull
    @Override
    public BookModel convert(@Nonnull final CreateBookPayload source) {
        Assert.notNull(source, "source cannot be null");

        return BookModel.builder()
                .title(source.getTitle())
                .description(source.getDescription())
                .build();
    }

}
