package controller.serialization;

import java.util.regex.Pattern;

import org.eclipse.swt.graphics.RGB;

/** Serializes RGB classes to a plain text representation */
public class RgbSerializer {

    private static char colorValueSeparator = '|';

    public static String rgbToString(RGB rgb) throws Exception {
        if (rgb == null) {
            throw new Exception("Unable to serialize null object"); //$NON-NLS-1$
        }
        return new StringBuilder()
                .append(rgb.red).append(colorValueSeparator)
                .append(rgb.green).append(colorValueSeparator)
                .append(rgb.blue)
                .toString();
    }

    public static RGB stringToRGB(String rgbString) throws Exception {
        try {
            String[] colorStringValues = rgbString.split(Pattern.quote(String.valueOf(colorValueSeparator)));
            return new RGB(
                    Integer.valueOf(colorStringValues[0]),
                    Integer.valueOf(colorStringValues[1]),
                    Integer.valueOf(colorStringValues[2]));
        } catch (Exception e) {
            throw new Exception("Unable to deserialize object"); //$NON-NLS-1$
        }
    }
}
