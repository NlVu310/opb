package com.openbanking.comon;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public abstract class BaseDTO {
    @NotNull
    private Long id;

}
