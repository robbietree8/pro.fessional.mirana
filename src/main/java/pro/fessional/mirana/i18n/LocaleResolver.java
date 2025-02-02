package pro.fessional.mirana.i18n;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Supports the underscore(en_US) and the kebab(en-US) naming
 *
 * @author trydofor
 * @since 2019-07-01
 */
public class LocaleResolver {
    protected LocaleResolver() {
    }

    @NotNull
    public static Locale locale(@NotNull String tag) {
        if (tag.indexOf('_') >= 0) {
            tag = tag.replace('_', '-');
        }


        Locale locale = Locale.forLanguageTag(tag);
        if (locale.getLanguage().isEmpty()) {
            return Locale.getDefault();
        }

        return locale;
    }
}
