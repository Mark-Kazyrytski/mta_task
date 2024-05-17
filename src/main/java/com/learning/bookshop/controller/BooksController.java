package com.learning.bookshop.controller;

import com.learning.bookshop.facade.BooksFacade;
import com.learning.bookshop.facade.data.BookData;
import com.learning.bookshop.facade.payload.CreateBookPayload;
import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/books")
@RequiredArgsConstructor
public class BooksController {

    private final BooksFacade booksFacade;

    @PostMapping
    @Nonnull
    public ResponseEntity<BookData> createBook(@Nonnull @Valid @RequestBody final CreateBookPayload payload) {
        Assert.notNull(payload, "payload cannot be null");

        final BookData createdBook = booksFacade.createBook(payload);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdBook);
    }

    @GetMapping("/{id}")
    @Nonnull
    public ResponseEntity<BookData> getBookById(@Positive @PathVariable final long id) {
        final BookData book = booksFacade.getBookById(id);

        return ResponseEntity.ok(book);
    }

}
