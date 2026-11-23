package com.novosiga.novosiga.service;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

final class TextoBusca {
    private static final Pattern ACENTOS = Pattern.compile("\\p{M}+");

    private TextoBusca() {}

    static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        String decomposto = Normalizer.normalize(texto.strip().toLowerCase(Locale.ROOT), Normalizer.Form.NFD);
        return ACENTOS.matcher(decomposto).replaceAll("");
    }
}
