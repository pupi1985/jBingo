package view;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

    private static final String EXTERNAL_BUNDLE_NAME = "messages"; //$NON-NLS-1$
    private static final String INTERNAL_BUNDLE_NAME = "resources." + EXTERNAL_BUNDLE_NAME; //$NON-NLS-1$
    private static ResourceBundle resourceBoundle;

    static {
        File file = new File(""); //$NON-NLS-1$
        try {
            URL[] urls = { file.toURI().toURL() };
            ClassLoader loader = new URLClassLoader(urls);
            resourceBoundle = ResourceBundle.getBundle(EXTERNAL_BUNDLE_NAME, Locale.getDefault(), loader);
        } catch (Exception e) {
            resourceBoundle = ResourceBundle.getBundle(INTERNAL_BUNDLE_NAME);
        }
    }

    private static String getString(String key) {
        try {
            return resourceBoundle.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    public static String getString(String key, Object... arguments) {
        return MessageFormat.format(getString(key), arguments);
    }
}
