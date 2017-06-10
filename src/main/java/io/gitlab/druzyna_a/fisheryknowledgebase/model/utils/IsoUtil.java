package io.gitlab.druzyna_a.fisheryknowledgebase.model.utils;

import com.neovisionaries.i18n.CountryCode;
import java.util.Arrays;
import java.util.Locale;

/**
 *
 * @author Damian Terlecki
 */
public final class IsoUtil {

    private IsoUtil() {
    }

    public static boolean isValidCountry(String countryCode) {
        return Arrays.asList(Locale.getISOCountries()).stream().anyMatch(c -> c.equals(countryCode));
    }
    
    public static int getCountryCode(String countryCode) {
        return CountryCode.getByCode(countryCode).getNumeric();
    }
    
}
