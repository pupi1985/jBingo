package view;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class NumberNames {

    private static final String BUNDLE_NAME = "numbers"; //$NON-NLS-1$

    private static ResourceBundle resourceBoundle;

    public static void loadResourceFromBasePath(String basePath) {
        File file = new File(basePath + "/resources"); //$NON-NLS-1$
        try {
            URL[] urls = { file.toURI().toURL() };
            ClassLoader loader = new URLClassLoader(urls);
            resourceBoundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault(), loader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getString(Integer key, Object... arguments) {
        try {
            return MessageFormat.format(resourceBoundle.getString(String.valueOf(key)), arguments);
        } catch (MissingResourceException e) {
            return null;
        }
    }
}
