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

import model.Bingo;
import model.exceptions.InvalidAmountOfGridColumnsException;
import model.exceptions.InvalidAmountOfNumbersException;
import model.exceptions.InvalidCurrentNumberColorException;
import model.exceptions.InvalidHistoryFontSizesException;
import model.exceptions.InvalidHistoryLengthException;
import model.exceptions.InvalidHistoryNumbersGapException;
import model.exceptions.InvalidMaximumHistoryFontSizeException;
import model.exceptions.InvalidMinimumHistoryFontSizeException;
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

    public PreferencesController(Bingo bingo, Display display, MainView mainView) {
        this.bingo = bingo;
        this.mainView = mainView;
        selectedNumbersFontData = bingo.getSettingsManager().getNumbersFontData();
        selectedCurrentNumberColor = bingo.getSettingsManager().getCurrentNumberColor();
        selectedPickedNumberColor = bingo.getSettingsManager().getPickedNumberColor();
        selectedUnpickedNumberColor = bingo.getSettingsManager().getUnpickedNumberColor();
        preferencesView = new PreferencesView(bingo, this, display, mainView.getShell());
        updateNumbersFontValuesLabelFromFontData(selectedNumbersFontData);
        preferencesView.open();
    }

    private void updateNumbersFontValuesLabelFromFontData(FontData fontData) {
        int fontSize = preferencesView.getNumbersFontValuesLabel().getFont().getFontData()[0].getHeight();
        Label numbersFontValuesLabel = preferencesView.getNumbersFontValuesLabel();
        String numbersFontValuesLabelText = Messages.getString("PreferencesView.NumbersFontSampleText", //$NON-NLS-1$
                fontData.getName(), fontData.getHeight());
        numbersFontValuesLabel.setText(numbersFontValuesLabelText);
        numbersFontValuesLabel
                .setFont(new Font(preferencesView.getDisplay(), fontData.getName(), fontSize, fontData.getStyle()));
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
     * setting any of them they are all reset to their original values. Retruns TRUE
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
        try {
            bingo.getSettingsManager()
                    .setAmountOfNumbers(Integer.valueOf(preferencesView.getAmountOfNumbersText().getText()));
            bingo.getSettingsManager()
                    .setAmountOfGridColumns(Integer.valueOf(preferencesView.getAmountOfGridColumnsText().getText()));
            bingo.getSettingsManager().setWaitingSecondsBetweenNumbers(
                    Integer.valueOf(preferencesView.getWaitingSecondsBetweenNumbersText().getText()));
            bingo.getSettingsManager().setNumbersFontDataFromValues(selectedNumbersFontData.getName(),
                    selectedNumbersFontData.getHeight(), selectedNumbersFontData.getStyle());
            bingo.getSettingsManager()
                    .setMaximumHistoryLength(Integer.valueOf(preferencesView.getMaximumHistoryLengthText().getText()));
            bingo.getSettingsManager().setHistoryFontSizes(
                    Integer.valueOf(preferencesView.getMinimumHistoryFontSizeText().getText()),
                    Integer.valueOf(preferencesView.getMaximumHistoryFontSizeText().getText()));
            bingo.getSettingsManager()
                    .setHistoryNumbersGap(Integer.valueOf(preferencesView.getHistoryNumbersGapText().getText()));
            bingo.getSettingsManager().setCurrentNumberColor(selectedCurrentNumberColor);
            bingo.getSettingsManager().setPickedNumberColor(selectedPickedNumberColor);
            bingo.getSettingsManager().setUnpickedNumberColor(selectedUnpickedNumberColor);
            success = true;
        } catch (NumberFormatException | InvalidHistoryLengthException | InvalidAmountOfNumbersException
                | InvalidWaitingSecondsBetweenNumbersException | InvalidNumbersFontDataException
                | InvalidMinimumHistoryFontSizeException
                | InvalidMaximumHistoryFontSizeException | InvalidHistoryFontSizesException
                | InvalidHistoryNumbersGapException
                | InvalidAmountOfGridColumnsException | InvalidCurrentNumberColorException
                | InvalidPickedNumberColorException
                | InvalidUnpickedNumberColorException e1) {
            try {
                bingo.getSettingsManager().setAmountOfNumbers(originalAmountOfNumbers);
                bingo.getSettingsManager().setAmountOfGridColumns(originalAmountOfGridColumns);
                bingo.getSettingsManager().setWaitingSecondsBetweenNumbers(originalWaitingSecondsBetweenNumbers);
                bingo.getSettingsManager().setNumbersFontDataFromValues(originalNumbersFontData.getName(),
                        originalNumbersFontData.getHeight(), originalNumbersFontData.getStyle());
                bingo.getSettingsManager().setMaximumHistoryLength(originalMaximumHistoryLength);
                bingo.getSettingsManager().setHistoryFontSizes(originalMinimumHistoryFontSize,
                        originalMaximumHistoryFontSize);
                bingo.getSettingsManager().setHistoryNumbersGap(originalHistoryNumbersGap);
                bingo.getSettingsManager().setCurrentNumberColor(originalCurrentNumberColor);
                bingo.getSettingsManager().setPickedNumberColor(originalPickedNumberColor);
                bingo.getSettingsManager().setUnpickedNumberColor(originalUnpickedNumberColor);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (success) { // Apply changes to the view and model
            if (bingo.getGameStarted()) {
                MessageBox messageBox = new MessageBox(preferencesView.getShell(),
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO);
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
        FontDialog fontDialog = new FontDialog(preferencesView.getShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        fontDialog.setEffectsVisible(false);
        fontDialog.setFontList(new FontData[] { selectedNumbersFontData });
        FontData fontData = fontDialog.open();
        if (fontData != null) {
            selectedNumbersFontData = fontData;
            updateNumbersFontValuesLabelFromFontData(fontData);
            preferencesView.getShell().layout(true, true);
        }
        preferencesView.getShell().pack();
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
}
