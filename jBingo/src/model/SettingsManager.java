package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import controller.serialization.RgbSerializer;
import model.exceptions.InvalidAmountOfGridColumnsException;
import model.exceptions.InvalidAmountOfNumbersException;
import model.exceptions.InvalidCurrentNumberColorException;
import model.exceptions.InvalidHighlightColorException;
import model.exceptions.InvalidHistoryFontSizesException;
import model.exceptions.InvalidHistoryLengthException;
import model.exceptions.InvalidHistoryNumbersGapException;
import model.exceptions.InvalidMaximumHistoryFontSizeException;
import model.exceptions.InvalidMinimumHistoryFontSizeException;
import model.exceptions.InvalidNumberNamesFontDataException;
import model.exceptions.InvalidNumbersFontDataException;
import model.exceptions.InvalidPickedNumberColorException;
import model.exceptions.InvalidUnpickedNumberColorException;
import model.exceptions.InvalidWaitingSecondsBetweenNumbersException;

public class SettingsManager {

    private static final String SETTINGS_FILE = "config.properties"; //$NON-NLS-1$

    private static final String SETTING_AMOUNT_OF_NUMBERS = "amountOfNumbers"; //$NON-NLS-1$
    private static final String SETTING_AMOUNT_OF_GRID_COLUMNS = "amountOfGridColumns"; //$NON-NLS-1$
    private static final String SETTING_WAITING_SECONDS_BETWEEN_NUMBERS = "waitingSecondsBetweenNumbers"; //$NON-NLS-1$
    private static final String SETTING_NUMBERS_FONT_DATA = "numbersFontData"; //$NON-NLS-1$
    private static final String SETTING_MAXIMUM_HISTORY_LENGTH = "maximumHistoryLength"; //$NON-NLS-1$
    private static final String SETTING_MINIMUM_HISTORY_FONT_SIZE = "minimumHistoryFontSize"; //$NON-NLS-1$
    private static final String SETTING_MAXIMUM_HISTORY_FONT_SIZE = "maximumHistoryFontSize"; //$NON-NLS-1$
    private static final String SETTING_HISTORY_NUMBERS_GAP = "historyNumbersGap"; //$NON-NLS-1$
    private static final String SETTING_CURRENT_NUMBER_COLOR = "currentNumberColor"; //$NON-NLS-1$
    private static final String SETTING_PICKED_NUMBER_COLOR = "pickedNumberColor"; //$NON-NLS-1$
    private static final String SETTING_UNPICKED_NUMBER_COLOR = "unpickedNumberColor"; //$NON-NLS-1$
    private static final String SETTING_HIGHLIGHT_COLOR = "highlightColor"; //$NON-NLS-1$
    private static final String SETTING_SHOW_NUMBER_NAMES = "showNumberNames"; //$NON-NLS-1$
    private static final String SETTING_NUMBER_NAMES_FONT_DATA = "numberNamesFontData"; //$NON-NLS-1$

    private String basePath;

    private int amountOfNumbers = 90;
    private int amountOfGridColumns = 10;
    private int waitingSecondsBetweenNumbers = 0;
    private FontData numbersFontData;
    private int maximumHistoryLength = 5;
    private int minimumHistoryFontSize = 15;
    private int maximumHistoryFontSize = 30;
    private int historyNumbersGap = 80;
    private RGB currentNumberColor;
    private RGB pickedNumberColor;
    private RGB unpickedNumberColor;
    private RGB highlightColor;
    private boolean showNumberNames = true;
    private FontData numberNamesFontData;

    public SettingsManager(String basePath) {
        this.basePath = basePath;
    }

    public void loadFromFile(Display display) {
        File file = new File(basePath + "/" + SETTINGS_FILE); //$NON-NLS-1$
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            try {
                amountOfNumbers = Integer.valueOf(properties.getProperty(SETTING_AMOUNT_OF_NUMBERS));
            } catch (Exception e) {
            }
            try {
                amountOfGridColumns = Integer.valueOf(properties.getProperty(SETTING_AMOUNT_OF_GRID_COLUMNS));
            } catch (Exception e) {
            }
            try {
                waitingSecondsBetweenNumbers = Integer.valueOf(properties.getProperty(SETTING_WAITING_SECONDS_BETWEEN_NUMBERS));
            } catch (Exception e) {
            }
            try {
                numbersFontData = new FontData(properties.getProperty(SETTING_NUMBERS_FONT_DATA));
            } catch (Exception e1) {
                try { // Make every possible effort to create a valid font
                    FontData systemFont = display.getSystemFont().getFontData()[0];
                    setNumbersFontDataFromValues(systemFont.getName(), systemFont.getHeight() * 4, SWT.BOLD);
                } catch (Exception e2) {
                    setNumbersFontDataFromValues("", 24, SWT.BOLD); //$NON-NLS-1$
                }
            }
            try {
                maximumHistoryLength = Integer.valueOf(properties.getProperty(SETTING_MAXIMUM_HISTORY_LENGTH));
            } catch (Exception e) {
            }
            try {
                minimumHistoryFontSize = Integer.valueOf(properties.getProperty(SETTING_MINIMUM_HISTORY_FONT_SIZE));
            } catch (Exception e) {
            }
            try {
                maximumHistoryFontSize = Integer.valueOf(properties.getProperty(SETTING_MAXIMUM_HISTORY_FONT_SIZE));
            } catch (Exception e) {
            }
            try {
                historyNumbersGap = Integer.valueOf(properties.getProperty(SETTING_HISTORY_NUMBERS_GAP));
            } catch (Exception e) {
            }
            try {
                currentNumberColor = RgbSerializer.stringToRGB(properties.getProperty(SETTING_CURRENT_NUMBER_COLOR));
            } catch (Exception e) {
                currentNumberColor = display.getSystemColor(SWT.COLOR_RED).getRGB();
            }
            try {
                pickedNumberColor = RgbSerializer.stringToRGB(properties.getProperty(SETTING_PICKED_NUMBER_COLOR));
            } catch (Exception e) {
                pickedNumberColor = display.getSystemColor(SWT.COLOR_INFO_FOREGROUND).getRGB();
            }
            try {
                unpickedNumberColor = RgbSerializer.stringToRGB(properties.getProperty(SETTING_UNPICKED_NUMBER_COLOR));
            } catch (Exception e) {
                unpickedNumberColor = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW).getRGB();
            }
            try {
                highlightColor = RgbSerializer.stringToRGB(properties.getProperty(SETTING_HIGHLIGHT_COLOR));
            } catch (Exception e) {
                highlightColor = new RGB(255, 251, 204);
            }
            try {
                showNumberNames = Boolean.valueOf(properties.getProperty(SETTING_SHOW_NUMBER_NAMES));
            } catch (Exception e) {
            }
            try {
                numberNamesFontData = new FontData(properties.getProperty(SETTING_NUMBER_NAMES_FONT_DATA));
            } catch (Exception e1) {
                try { // Make every possible effort to create a valid font
                    FontData systemFont = display.getSystemFont().getFontData()[0];
                    setNumberNamesFontDataFromValues(systemFont.getName(), systemFont.getHeight() * 2, SWT.BOLD);
                } catch (Exception e2) {
                    setNumberNamesFontDataFromValues("", 20, SWT.BOLD); //$NON-NLS-1$
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveToFile() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(basePath + "/" + SETTINGS_FILE)) { //$NON-NLS-1$
            Properties properties = new Properties();
            properties.setProperty(SETTING_AMOUNT_OF_NUMBERS, String.valueOf(amountOfNumbers));
            properties.setProperty(SETTING_AMOUNT_OF_GRID_COLUMNS, String.valueOf(amountOfGridColumns));
            properties.setProperty(SETTING_WAITING_SECONDS_BETWEEN_NUMBERS, String.valueOf(waitingSecondsBetweenNumbers));
            properties.setProperty(SETTING_NUMBERS_FONT_DATA, String.valueOf(numbersFontData));
            properties.setProperty(SETTING_MAXIMUM_HISTORY_LENGTH, String.valueOf(maximumHistoryLength));
            properties.setProperty(SETTING_MINIMUM_HISTORY_FONT_SIZE, String.valueOf(minimumHistoryFontSize));
            properties.setProperty(SETTING_MAXIMUM_HISTORY_FONT_SIZE, String.valueOf(maximumHistoryFontSize));
            properties.setProperty(SETTING_HISTORY_NUMBERS_GAP, String.valueOf(historyNumbersGap));
            properties.setProperty(SETTING_CURRENT_NUMBER_COLOR, RgbSerializer.rgbToString(currentNumberColor));
            properties.setProperty(SETTING_PICKED_NUMBER_COLOR, RgbSerializer.rgbToString(pickedNumberColor));
            properties.setProperty(SETTING_UNPICKED_NUMBER_COLOR, RgbSerializer.rgbToString(unpickedNumberColor));
            properties.setProperty(SETTING_HIGHLIGHT_COLOR, RgbSerializer.rgbToString(highlightColor));
            properties.setProperty(SETTING_SHOW_NUMBER_NAMES, String.valueOf(showNumberNames));
            properties.setProperty(SETTING_NUMBER_NAMES_FONT_DATA, String.valueOf(numberNamesFontData));
            properties.store(fileOutputStream, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMaximumHistoryLength() {
        return maximumHistoryLength;
    }

    public void setMaximumHistoryLength(int maximumHistoryLength) throws InvalidHistoryLengthException {
        if (maximumHistoryLength < 1) {
            throw new InvalidHistoryLengthException();
        }
        this.maximumHistoryLength = maximumHistoryLength;
    }

    public int getAmountOfNumbers() {
        return amountOfNumbers;
    }

    public void setAmountOfNumbers(int amountOfNumbers) throws InvalidAmountOfNumbersException {
        if (amountOfNumbers < 2) {
            throw new InvalidAmountOfNumbersException();
        }
        this.amountOfNumbers = amountOfNumbers;
    }

    public int getAmountOfGridColumns() {
        return amountOfGridColumns;
    }

    public void setAmountOfGridColumns(int amountOfGridColumns) throws InvalidAmountOfGridColumnsException {
        if (amountOfGridColumns < 1) {
            throw new InvalidAmountOfGridColumnsException();
        }
        this.amountOfGridColumns = amountOfGridColumns;
    }

    public int getWaitingSecondsBetweenNumbers() {
        return waitingSecondsBetweenNumbers;
    }

    public void setWaitingSecondsBetweenNumbers(int waitingSecondsBetweenNumbers) throws InvalidWaitingSecondsBetweenNumbersException {
        if (waitingSecondsBetweenNumbers < 0) {
            throw new InvalidWaitingSecondsBetweenNumbersException();
        }
        this.waitingSecondsBetweenNumbers = waitingSecondsBetweenNumbers;
    }

    public FontData getNumbersFontData() {
        return numbersFontData;
    }

    public void setNumbersFontDataFromValues(String fontName, int fontSize, int fontStyle) throws InvalidNumbersFontDataException {
        try {
            numbersFontData = new FontData(fontName, fontSize, fontStyle);
        } catch (Exception e) {
            throw new InvalidNumbersFontDataException();
        }
    }

    public int getMinimumHistoryFontSize() {
        return minimumHistoryFontSize;
    }

    private void setMinimumHistoryFontSize(int minimumHistoryFontSize) throws InvalidMinimumHistoryFontSizeException {
        try {
            new FontData(numbersFontData.getName(), minimumHistoryFontSize, numbersFontData.getStyle());
        } catch (Exception e) {
            throw new InvalidMinimumHistoryFontSizeException();
        }
        this.minimumHistoryFontSize = minimumHistoryFontSize;
    }

    public int getMaximumHistoryFontSize() {
        return maximumHistoryFontSize;
    }

    private void setMaximumHistoryFontSize(int maximumHistoryFontSize) throws InvalidMaximumHistoryFontSizeException {
        try {
            new FontData(numbersFontData.getName(), maximumHistoryFontSize, numbersFontData.getStyle());
        } catch (Exception e) {
            throw new InvalidMaximumHistoryFontSizeException();
        }
        this.maximumHistoryFontSize = maximumHistoryFontSize;
    }

    public void setHistoryFontSizes(int minimumHistoryFontSize, int maximumHistoryFontSize) throws InvalidMinimumHistoryFontSizeException,
            InvalidMaximumHistoryFontSizeException, InvalidHistoryFontSizesException {
        if (minimumHistoryFontSize <= maximumHistoryFontSize) {
            int originalMinimumHistoryFontSize = minimumHistoryFontSize;
            int originalMaximumHistoryFontSize = maximumHistoryFontSize;
            try {
                setMinimumHistoryFontSize(minimumHistoryFontSize);
                setMaximumHistoryFontSize(maximumHistoryFontSize);
            } catch (InvalidMinimumHistoryFontSizeException | InvalidMaximumHistoryFontSizeException e) {
                this.minimumHistoryFontSize = originalMinimumHistoryFontSize;
                this.maximumHistoryFontSize = originalMaximumHistoryFontSize;
                throw e;
            }
        } else {
            throw new InvalidHistoryFontSizesException();
        }
    }

    public int getHistoryNumbersGap() {
        return historyNumbersGap;
    }

    public void setHistoryNumbersGap(int historyNumbersGap) throws InvalidHistoryNumbersGapException {
        if (historyNumbersGap < 0) {
            throw new InvalidHistoryNumbersGapException();
        }
        this.historyNumbersGap = historyNumbersGap;
    }

    public RGB getCurrentNumberColor() {
        return currentNumberColor;
    }

    public void setCurrentNumberColor(RGB currentNumberColor) throws InvalidCurrentNumberColorException {
        if (currentNumberColor == null) {
            throw new InvalidCurrentNumberColorException();
        }
        this.currentNumberColor = currentNumberColor;
    }

    public RGB getPickedNumberColor() {
        return pickedNumberColor;
    }

    public void setPickedNumberColor(RGB pickedNumberColor) throws InvalidPickedNumberColorException {
        if (pickedNumberColor == null) {
            throw new InvalidPickedNumberColorException();
        }
        this.pickedNumberColor = pickedNumberColor;
    }

    public RGB getUnpickedNumberColor() {
        return unpickedNumberColor;
    }

    public void setUnpickedNumberColor(RGB unpickedNumberColor) throws InvalidUnpickedNumberColorException {
        if (unpickedNumberColor == null) {
            throw new InvalidUnpickedNumberColorException();
        }
        this.unpickedNumberColor = unpickedNumberColor;
    }

    public RGB getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(RGB highlightColor) throws InvalidHighlightColorException {
        if (highlightColor == null) {
            throw new InvalidHighlightColorException();
        }
        this.highlightColor = highlightColor;
    }

    public boolean getShowNumberNames() {
        return showNumberNames;
    }

    public void setShowNumberNames(boolean showNumberNames) {
        this.showNumberNames = showNumberNames;
    }

    public FontData getNumberNamesFontData() {
        return numberNamesFontData;
    }

    public void setNumberNamesFontDataFromValues(String fontName, int fontSize, int fontStyle) throws InvalidNumberNamesFontDataException {
        try {
            numberNamesFontData = new FontData(fontName, fontSize, fontStyle);
        } catch (Exception e) {
            throw new InvalidNumberNamesFontDataException();
        }
    }
}
