package io.gitlab.druzyna_a.knowledgebase.model;

import java.util.Arrays;
import java.util.Locale;

/**
 *
 * @author Damian Terlecki
 */
public final class IsoUtil {

    private IsoUtil() {
    }

    public static boolean isValidCountry(String s) {
        return Arrays.asList(Locale.getISOCountries()).stream().anyMatch(c -> c.equals(s));
    }
}
