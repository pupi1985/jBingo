package view;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class ViewUtils {

    private static Listener numericalValidator;

    public static void centerShell(Shell shell) {
        Rectangle bounds = shell.getDisplay().getPrimaryMonitor().getBounds();
        Rectangle rect = shell.getBounds();
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;
        shell.setLocation(x, y);
    }

    public static void removeAllChildren(Composite composite) {
        for (Control control : composite.getChildren()) {
            control.dispose();
        }
    }

    public static Listener getNumericalValidator() {
        if (numericalValidator == null) {
            numericalValidator = new Listener() {
                @Override
                public void handleEvent(Event e) {
                    char[] chars = new char[e.text.length()];
                    e.text.getChars(0, chars.length, chars, 0);
                    for (int i = 0; i < chars.length; i++) {
                        if (chars[i] < '0' || chars[i] > '9') {
                            e.doit = false;
                            return;
                        }
                    }
                }
            };
        }
        return numericalValidator;
    }
}
