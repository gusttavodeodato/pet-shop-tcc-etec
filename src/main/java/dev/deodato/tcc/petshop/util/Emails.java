package dev.deodato.tcc.petshop.util;

import java.util.Locale;

public final class Emails {
    private Emails() {}

    public static String normalizar(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
