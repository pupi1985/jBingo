package main;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;

import org.eclipse.swt.widgets.Display;

import controller.MainViewController;
import model.Bingo;
import model.SettingsManager;
import view.Messages;
import view.NumberNames;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    private String getBasePath() throws UnsupportedEncodingException {
        File jarPath = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return URLDecoder.decode(jarPath.getParent(), "UTF-8"); //$NON-NLS-1$
    }

    private void loadSwtJar(String basePath) {
        String osName = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
        String osArch = System.getProperty("os.arch").toLowerCase(); //$NON-NLS-1$

        String swtFileNameOsPart = osName.contains("win") //$NON-NLS-1$
                ? "win32" //$NON-NLS-1$
                : osName.contains("mac") //$NON-NLS-1$
                        ? "macosx" //$NON-NLS-1$
                        : osName.contains("linux") || osName.contains("nix") ? "gtk-linux" //$NON-NLS-1$
                                : ""; // throw new RuntimeException("Unknown OS name: "+osName)

        String swtFileNameArchPart = osArch.contains("64") ? "x86_64" : "x86"; //$NON-NLS-1$
        String swtFileName = "swt-4.9-" + swtFileNameOsPart + "-" + swtFileNameArchPart + ".jar"; //$NON-NLS-1$

        try {
            Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addUrlMethod.setAccessible(true);

            URLClassLoader classLoader = (URLClassLoader) getClass().getClassLoader();
            URL swtFileUrl = new File(basePath + "/lib/" + swtFileName).toURI().toURL(); //$NON-NLS-1$
            addUrlMethod.invoke(classLoader, swtFileUrl);
        } catch (Exception e) {
            throw new RuntimeException("Unable to add the SWT jar to the class path: " + swtFileName, e); //$NON-NLS-1$
        }
    }

    public Main() {
        try {
            String basePath = getBasePath();
            loadSwtJar(basePath);
            Messages.loadResourceFromBasePath(basePath);
            NumberNames.loadResourceFromBasePath(basePath);
            Display display = Display.getDefault();
            SettingsManager settingsManager = new SettingsManager(basePath);
            settingsManager.loadFromFile(display);
            Bingo bingo = new Bingo(settingsManager);
            new MainViewController(bingo, display);
            settingsManager.saveToFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
