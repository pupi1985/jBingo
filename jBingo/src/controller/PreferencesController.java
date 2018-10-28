package controller;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import model.Bingo;
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
import view.Messages;
import view.shells.MainView;
import view.shells.PreferencesView;

public class PreferencesController {

    private Bingo bingo;
    private PreferencesView preferencesView;
    private MainView mainView;

    private FontData selectedNumbersFontData;
    private RGB selectedCurrentNumberColor;
    private RGB selectedPickedNumberColor;
    private RGB selectedUnpickedNumberColor;
    private RGB selectedHighlightColor;
    private FontData selectedNumberNamesFontData;

    public PreferencesController(Bingo bingo, Display display, MainView mainView) {
        this.bingo = bingo;
        this.mainView = mainView;
        selectedNumbersFontData = bingo.getSettingsManager().getNumbersFontData();
        selectedCurrentNumberColor = bingo.getSettingsManager().getCurrentNumberColor();
        selectedPickedNumberColor = bingo.getSettingsManager().getPickedNumberColor();
        selectedUnpickedNumberColor = bingo.getSettingsManager().getUnpickedNumberColor();
        selectedHighlightColor = bingo.getSettingsManager().getHighlightColor();
        selectedNumberNamesFontData = bingo.getSettingsManager().getNumberNamesFontData();
        preferencesView = new PreferencesView(bingo, this, display, mainView.getShell());
        updateSampleFontLabelWithFontData(selectedNumbersFontData, preferencesView.getNumbersFontValuesLabel());
        updateSampleFontLabelWithFontData(selectedNumberNamesFontData, preferencesView.getNumberNamesFontValuesLabel());
        preferencesView.open();
    }

    private void updateSampleFontLabelWithFontData(FontData fontData, Label label) {
        int fontSize = label.getFont().getFontData()[0].getHeight();
        String fontSampleTextLabel = Messages.getString("PreferencesView.FontSampleText", fontData.getName(), fontData.getHeight()); //$NON-NLS-1$
        label.setText(fontSampleTextLabel);
        label.setFont(new Font(preferencesView.getDisplay(), fontData.getName(), fontSize, fontData.getStyle()));
    }

    /**
     * Same as applyButtonAction() but also closes the shell when the operation
     * succeeds
     */
    public void okButtonAction() {
        if (applyButtonAction()) {
            preferencesView.getShell().close();
        }
    }

    /**
     * Sets all values without closing the shell. If there is an exception while
     * setting any of them they are all reset to their original values. Returns TRUE
     * when the changes are applied and FALSE otherwise
     */
    public boolean applyButtonAction() {
        boolean success = false;
        int originalAmountOfNumbers = bingo.getSettingsManager().getAmountOfNumbers();
        int originalAmountOfGridColumns = bingo.getSettingsManager().getAmountOfGridColumns();
        int originalWaitingSecondsBetweenNumbers = bingo.getSettingsManager().getWaitingSecondsBetweenNumbers();
        FontData originalNumbersFontData = bingo.getSettingsManager().getNumbersFontData();
        int originalMaximumHistoryLength = bingo.getSettingsManager().getMaximumHistoryLength();
        int originalMinimumHistoryFontSize = bingo.getSettingsManager().getMinimumHistoryFontSize();
        int originalMaximumHistoryFontSize = bingo.getSettingsManager().getMaximumHistoryFontSize();
        int originalHistoryNumbersGap = bingo.getSettingsManager().getHistoryNumbersGap();
        RGB originalCurrentNumberColor = bingo.getSettingsManager().getCurrentNumberColor();
        RGB originalPickedNumberColor = bingo.getSettingsManager().getPickedNumberColor();
        RGB originalUnpickedNumberColor = bingo.getSettingsManager().getUnpickedNumberColor();
        RGB originalHighlightColor = bingo.getSettingsManager().getHighlightColor();
        boolean originalShowNumberNames = bingo.getSettingsManager().getShowNumberNames();
        FontData originalNumberNamesFontData = bingo.getSettingsManager().getNumberNamesFontData();

        try {
            bingo.getSettingsManager().setAmountOfNumbers(Integer.valueOf(preferencesView.getAmountOfNumbersText().getText()));
            bingo.getSettingsManager().setAmountOfGridColumns(Integer.valueOf(preferencesView.getAmountOfGridColumnsText().getText()));
            bingo.getSettingsManager().setWaitingSecondsBetweenNumbers(Integer.valueOf(preferencesView.getWaitingSecondsBetweenNumbersText().getText()));
            bingo.getSettingsManager().setNumbersFontDataFromValues(
                    selectedNumbersFontData.getName(),
                    selectedNumbersFontData.getHeight(),
                    selectedNumbersFontData.getStyle());
            bingo.getSettingsManager().setMaximumHistoryLength(Integer.valueOf(preferencesView.getMaximumHistoryLengthText().getText()));
            bingo.getSettingsManager().setHistoryFontSizes(
                    Integer.valueOf(preferencesView.getMinimumHistoryFontSizeText().getText()),
                    Integer.valueOf(preferencesView.getMaximumHistoryFontSizeText().getText()));
            bingo.getSettingsManager().setHistoryNumbersGap(Integer.valueOf(preferencesView.getHistoryNumbersGapText().getText()));
            bingo.getSettingsManager().setCurrentNumberColor(selectedCurrentNumberColor);
            bingo.getSettingsManager().setPickedNumberColor(selectedPickedNumberColor);
            bingo.getSettingsManager().setUnpickedNumberColor(selectedUnpickedNumberColor);
            bingo.getSettingsManager().setHighlightColor(selectedHighlightColor);
            bingo.getSettingsManager().setShowNumberNames(preferencesView.getShowNumberNamesCheckbox().getSelection());
            bingo.getSettingsManager().setNumberNamesFontDataFromValues(
                    selectedNumberNamesFontData.getName(),
                    selectedNumberNamesFontData.getHeight(),
                    selectedNumberNamesFontData.getStyle());
            success = true;
        } catch (NumberFormatException
                | InvalidHistoryLengthException
                | InvalidAmountOfNumbersException
                | InvalidWaitingSecondsBetweenNumbersException
                | InvalidNumbersFontDataException
                | InvalidMinimumHistoryFontSizeException
                | InvalidMaximumHistoryFontSizeException
                | InvalidHistoryFontSizesException
                | InvalidHistoryNumbersGapException
                | InvalidAmountOfGridColumnsException
                | InvalidCurrentNumberColorException
                | InvalidPickedNumberColorException
                | InvalidUnpickedNumberColorException
                | InvalidHighlightColorException
                | InvalidNumberNamesFontDataException e1) {
            try {
                MessageBox messageBox = new MessageBox(preferencesView.getShell(), SWT.ICON_ERROR | SWT.OK);
                messageBox.setText(Messages.getString("Application.Error")); //$NON-NLS-1$
                messageBox.setMessage(e1.getMessage());
                messageBox.open();

                bingo.getSettingsManager().setAmountOfNumbers(originalAmountOfNumbers);
                bingo.getSettingsManager().setAmountOfGridColumns(originalAmountOfGridColumns);
                bingo.getSettingsManager().setWaitingSecondsBetweenNumbers(originalWaitingSecondsBetweenNumbers);
                bingo.getSettingsManager().setNumbersFontDataFromValues(originalNumbersFontData.getName(), originalNumbersFontData.getHeight(), originalNumbersFontData.getStyle());
                bingo.getSettingsManager().setMaximumHistoryLength(originalMaximumHistoryLength);
                bingo.getSettingsManager().setHistoryFontSizes(originalMinimumHistoryFontSize, originalMaximumHistoryFontSize);
                bingo.getSettingsManager().setHistoryNumbersGap(originalHistoryNumbersGap);
                bingo.getSettingsManager().setCurrentNumberColor(originalCurrentNumberColor);
                bingo.getSettingsManager().setPickedNumberColor(originalPickedNumberColor);
                bingo.getSettingsManager().setUnpickedNumberColor(originalUnpickedNumberColor);
                bingo.getSettingsManager().setHighlightColor(originalHighlightColor);
                bingo.getSettingsManager().setShowNumberNames(originalShowNumberNames);
                bingo.getSettingsManager().setNumberNamesFontDataFromValues(originalNumberNamesFontData.getName(), originalNumberNamesFontData.getHeight(), originalNumberNamesFontData.getStyle());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (success) { // Apply changes to the view and model
            if (bingo.getGameStarted()) {
                MessageBox messageBox = new MessageBox(preferencesView.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText(Messages.getString("PreferencesView.ResetConfirmationTitle")); //$NON-NLS-1$
                messageBox.setMessage(Messages.getString("PreferencesView.ResetConfirmationText")); //$NON-NLS-1$
                int answer = messageBox.open();
                if (answer == SWT.YES) {
                    recreateComponents();
                } else {
                    success = false; // Do not close the window if the user didn't confirm the operation
                }
            } else {
                recreateComponents();
            }
            bingo.getSettingsManager().saveToFile();
        }
        return success;
    }

    private void recreateComponents() {
        mainView.createContents();
        bingo.reset();
        mainView.getShell().layout(true, true);
        mainView.getShell().setFocus();
    }

    public void cancelButtonAction() {
        preferencesView.getShell().dispose();
    }

    public void changeNumbersFontAction() {
        FontData fontData = this.openChangeFontDialog(preferencesView.getShell(), selectedNumbersFontData);
        if (fontData != null) {
            selectedNumbersFontData = fontData;
            updateSampleFontLabelWithFontData(fontData, preferencesView.getNumbersFontValuesLabel());
            preferencesView.getShell().layout(true, true);
        }
        preferencesView.getShell().pack();
    }

    private FontData openChangeFontDialog(Shell shell, FontData inputFont) {
        FontDialog fontDialog = new FontDialog(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        fontDialog.setEffectsVisible(false);
        fontDialog.setFontList(new FontData[] { inputFont });
        return fontDialog.open();
    }

    private Color displayColorDialog(RGB selectedColor) {
        ColorDialog colorDialog = new ColorDialog(preferencesView.getShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        colorDialog.setRGB(selectedColor);
        RGB rgbColor = colorDialog.open();
        return rgbColor == null ? null : new Color(preferencesView.getDisplay(), rgbColor);
    }

    public void changeCurrentNumberColorAction() {
        Color color = displayColorDialog(selectedCurrentNumberColor);
        if (color != null) {
            selectedCurrentNumberColor = color.getRGB();
            preferencesView.getCurrentNumberColorColoredLabel().setBackground(color);
        }
    }

    public void changePickedNumberColorAction() {
        Color color = displayColorDialog(selectedPickedNumberColor);
        if (color != null) {
            selectedPickedNumberColor = color.getRGB();
            preferencesView.getPickedNumberColorColoredLabel().setBackground(color);
        }
    }

    public void changeUnpickedNumberColorAction() {
        Color color = displayColorDialog(selectedUnpickedNumberColor);
        if (color != null) {
            selectedUnpickedNumberColor = color.getRGB();
            preferencesView.getUnpickedNumberColorColoredLabel().setBackground(color);
        }
    }

    public void changeHighlightColorAction() {
        Color color = displayColorDialog(selectedHighlightColor);
        if (color != null) {
            selectedHighlightColor = color.getRGB();
            preferencesView.getHighlightColorColoredLabel().setBackground(color);
        }
    }

    public void changeNumberNamesFontAction() {
        FontData fontData = this.openChangeFontDialog(preferencesView.getShell(), selectedNumberNamesFontData);
        if (fontData != null) {
            selectedNumberNamesFontData = fontData;
            updateSampleFontLabelWithFontData(fontData, preferencesView.getNumberNamesFontValuesLabel());
            preferencesView.getShell().layout(true, true);
        }
        preferencesView.getShell().pack();
    }
}
