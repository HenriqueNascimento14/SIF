package com.novosiga.novosiga.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FaixaPreco {
    TODOS("Qualquer valor", null, null),
    ABAIXO_100("Abaixo de R$ 100", null, 100.0),
    DE_100_A_500("De R$ 100 a R$ 499,99", 100.0, 500.0),
    A_PARTIR_500("A partir de R$ 500", 500.0, null);

    private final String rotulo;
    private final Double minimo;
    private final Double maximo;
}
