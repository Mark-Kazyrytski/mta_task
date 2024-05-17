package com.learning.bookshop.facade.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateBookPayload {

    @NotBlank
    @Size(max = 100)
    String title;
    @NotBlank
    @Size(max = 1000)
    String description;

}
