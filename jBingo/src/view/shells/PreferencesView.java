package view.shells;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import controller.PreferencesController;
import model.Bingo;
import net.miginfocom.swt.MigLayout;
import view.Messages;
import view.ViewUtils;

public class PreferencesView {

    private Shell shell;
    private Shell parentShell;
    private Display display;

    private Text amountOfNumbersText;
    private Text amountOfGridColumnsText;
    private Text maximumHistoryLengthText;
    private Text minimumHistoryFontSizeText;
    private Text maximumHistoryFontSizeText;
    private Text waitingSecondsBetweenNumbersText;
    private Text historyNumbersGapText;
    private Label numbersFontValuesLabel;
    private Label currentNumberColorColoredLabel;
    private Label pickedNumberColorColoredLabel;
    private Label unpickedNumberColorColoredLabel;
    private Label highlightColorColoredLabel;

    private PreferencesController preferencesController;
    private Bingo bingo;

    public PreferencesView(Bingo bingo, PreferencesController preferencesController, Display display, Shell parentShell) {
        this.bingo = bingo;
        this.preferencesController = preferencesController;
        this.display = display;
        this.parentShell = parentShell;
        shell = new Shell();
        createContents();
    }

    public void open() {
        shell.layout();
        shell.pack();
        shell.open();
        ViewUtils.centerShell(shell);
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private void createContents() {
        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setLayout(new MigLayout("wrap 1")); //$NON-NLS-1$
        shell.setText(Messages.getString("PreferencesView.Title")); //$NON-NLS-1$

        Composite mainPanel = new Composite(shell, SWT.NONE);
        mainPanel.setLayout(new MigLayout("wrap 2, fill")); //$NON-NLS-1$

        Label amountOfNumbersLabel = new Label(mainPanel, SWT.NONE);
        amountOfNumbersLabel.setText(Messages.getString("PreferencesView.AmountOfNumbers")); //$NON-NLS-1$

        amountOfNumbersText = new Text(mainPanel, SWT.BORDER);
        amountOfNumbersText.setLayoutData("wmin 30pt"); //$NON-NLS-1$
        amountOfNumbersText.setText(String.valueOf(bingo.getSettingsManager().getAmountOfNumbers()));
        amountOfNumbersText.addListener(SWT.Verify, ViewUtils.getNumericalValidator());

        Label amountOfGridColumnsLabel = new Label(mainPanel, SWT.NONE);
        amountOfGridColumnsLabel.setText(Messages.getString("PreferencesView.AmountOfGridColumns")); //$NON-NLS-1$

        amountOfGridColumnsText = new Text(mainPanel, SWT.BORDER);
        amountOfGridColumnsText.setLayoutData("wmin 30pt"); //$NON-NLS-1$
        amountOfGridColumnsText.setText(String.valueOf(bingo.getSettingsManager().getAmountOfGridColumns()));
        amountOfGridColumnsText.addListener(SWT.Verify, ViewUtils.getNumericalValidator());

        Label waitingSecondsBetweenNumbersLabel = new Label(mainPanel, SWT.NONE);
        waitingSecondsBetweenNumbersLabel.setText(Messages.getString("PreferencesView.WaitingSecondsBetweenNumbers")); //$NON-NLS-1$

        waitingSecondsBetweenNumbersText = new Text(mainPanel, SWT.BORDER);
        waitingSecondsBetweenNumbersText.setLayoutData("wmin 30pt"); //$NON-NLS-1$
        waitingSecondsBetweenNumbersText.setText(String.valueOf(bingo.getSettingsManager().getWaitingSecondsBetweenNumbers()));
        waitingSecondsBetweenNumbersText.addListener(SWT.Verify, ViewUtils.getNumericalValidator());

        Label numbersFontLabel = new Label(mainPanel, SWT.NONE);
        numbersFontLabel.setText(Messages.getString("PreferencesView.NumbersFont")); //$NON-NLS-1$

        Button changeNumbersFontButton = new Button(mainPanel, SWT.PUSH);
        changeNumbersFontButton.setLayoutData("split 2"); //$NON-NLS-1$
        changeNumbersFontButton.setText(Messages.getString("PreferencesView.ChangeFont")); //$NON-NLS-1$
        changeNumbersFontButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                preferencesController.changeNumbersFontAction();
            }
        });

        numbersFontValuesLabel = new Label(mainPanel, SWT.NONE);
        numbersFontValuesLabel.setText(String.valueOf(bingo.getSettingsManager().getNumbersFontData()));

        Label maximumHistoryLengthLabel = new Label(mainPanel, SWT.NONE);
        maximumHistoryLengthLabel.setText(Messages.getString("PreferencesView.MaximumHistoryLength")); //$NON-NLS-1$

        maximumHistoryLengthText = new Text(mainPanel, SWT.BORDER);
        maximumHistoryLengthText.setLayoutData("wmin 30pt"); //$NON-NLS-1$
        maximumHistoryLengthText.setText(String.valueOf(bingo.getSettingsManager().getMaximumHistoryLength()));
        maximumHistoryLengthText.addListener(SWT.Verify, ViewUtils.getNumericalValidator());

        Label minimumHistoryFontSizeLabel = new Label(mainPanel, SWT.NONE);
        minimumHistoryFontSizeLabel.setText(Messages.getString("PreferencesView.MinimumHistoryFontSize")); //$NON-NLS-1$

        minimumHistoryFontSizeText = new Text(mainPanel, SWT.BORDER);
        minimumHistoryFontSizeText.setLayoutData("wmin 30pt"); //$NON-NLS-1$
        minimumHistoryFontSizeText.setText(String.valueOf(bingo.getSettingsManager().getMinimumHistoryFontSize()));
        minimumHistoryFontSizeText.addListener(SWT.Verify, ViewUtils.getNumericalValidator());

        Label maximumHistoryFontSizeLabel = new Label(mainPanel, SWT.NONE);
        maximumHistoryFontSizeLabel.setText(Messages.getString("PreferencesView.MaximumHistoryFontSize")); //$NON-NLS-1$

        maximumHistoryFontSizeText = new Text(mainPanel, SWT.BORDER);
        maximumHistoryFontSizeText.setLayoutData("wmin 30pt"); //$NON-NLS-1$
        maximumHistoryFontSizeText.setText(String.valueOf(bingo.getSettingsManager().getMaximumHistoryFontSize()));
        maximumHistoryFontSizeText.addListener(SWT.Verify, ViewUtils.getNumericalValidator());

        Label historyNumbersGapLabel = new Label(mainPanel, SWT.NONE);
        historyNumbersGapLabel.setText(Messages.getString("PreferencesView.HistoryNumbersGap")); //$NON-NLS-1$

        historyNumbersGapText = new Text(mainPanel, SWT.BORDER);
        historyNumbersGapText.setLayoutData("wmin 30pt"); //$NON-NLS-1$
        historyNumbersGapText.setText(String.valueOf(bingo.getSettingsManager().getHistoryNumbersGap()));
        historyNumbersGapText.addListener(SWT.Verify, ViewUtils.getNumericalValidator());

        Label numberColorsLabel = new Label(mainPanel, SWT.NONE);
        numberColorsLabel.setLayoutData("wrap"); //$NON-NLS-1$
        numberColorsLabel.setText(Messages.getString("PreferencesView.NumberColors")); //$NON-NLS-1$

        Label currentNumberColorLabel = new Label(mainPanel, SWT.NONE);
        currentNumberColorLabel.setLayoutData("gapleft 15pt"); //$NON-NLS-1$
        currentNumberColorLabel.setText(Messages.getString("PreferencesView.CurrentNumber")); //$NON-NLS-1$

        Button changeCurrentNumberColorButton = new Button(mainPanel, SWT.PUSH);
        changeCurrentNumberColorButton.setLayoutData("split 2"); //$NON-NLS-1$
        changeCurrentNumberColorButton.setText(Messages.getString("PreferencesView.ChangeColor")); //$NON-NLS-1$
        changeCurrentNumberColorButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                preferencesController.changeCurrentNumberColorAction();
            }
        });

        currentNumberColorColoredLabel = new Label(mainPanel, SWT.NONE);
        currentNumberColorColoredLabel.setLayoutData("wmin 30pt"); //$NON-NLS-1$
        currentNumberColorColoredLabel.setBackground(new Color(display, bingo.getSettingsManager().getCurrentNumberColor()));

        Label pickedNumberColorLabel = new Label(mainPanel, SWT.NONE);
        pickedNumberColorLabel.setLayoutData("gapleft 15pt"); //$NON-NLS-1$
        pickedNumberColorLabel.setText(Messages.getString("PreferencesView.PickedNumber")); //$NON-NLS-1$

        Button changePickedNumberColorButton = new Button(mainPanel, SWT.PUSH);
        changePickedNumberColorButton.setLayoutData("split 2"); //$NON-NLS-1$
        changePickedNumberColorButton.setText(Messages.getString("PreferencesView.ChangeColor")); //$NON-NLS-1$
        changePickedNumberColorButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                preferencesController.changePickedNumberColorAction();
            }
        });

        pickedNumberColorColoredLabel = new Label(mainPanel, SWT.NONE);
        pickedNumberColorColoredLabel.setLayoutData("wmin 30pt"); //$NON-NLS-1$
        pickedNumberColorColoredLabel.setBackground(new Color(display, bingo.getSettingsManager().getPickedNumberColor()));

        Label unpickedNumberColorLabel = new Label(mainPanel, SWT.NONE);
        unpickedNumberColorLabel.setLayoutData("gapleft 15pt"); //$NON-NLS-1$
        unpickedNumberColorLabel.setText(Messages.getString("PreferencesView.UnpickedNumber")); //$NON-NLS-1$

        Button changeUnpickedNumberColorButton = new Button(mainPanel, SWT.PUSH);
        changeUnpickedNumberColorButton.setLayoutData("split 2"); //$NON-NLS-1$
        changeUnpickedNumberColorButton.setText(Messages.getString("PreferencesView.ChangeColor")); //$NON-NLS-1$
        changeUnpickedNumberColorButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                preferencesController.changeUnpickedNumberColorAction();
            }
        });

        unpickedNumberColorColoredLabel = new Label(mainPanel, SWT.NONE);
        unpickedNumberColorColoredLabel.setLayoutData("wmin 30pt"); //$NON-NLS-1$
        unpickedNumberColorColoredLabel.setBackground(new Color(display, bingo.getSettingsManager().getUnpickedNumberColor()));

        Label highlightColorLabel = new Label(mainPanel, SWT.NONE);
        highlightColorLabel.setLayoutData("gapleft 15pt"); //$NON-NLS-1$
        highlightColorLabel.setText(Messages.getString("PreferencesView.Highlight")); //$NON-NLS-1$

        Button changeHighlightColorButton = new Button(mainPanel, SWT.PUSH);
        changeHighlightColorButton.setLayoutData("split 2"); //$NON-NLS-1$
        changeHighlightColorButton.setText(Messages.getString("PreferencesView.ChangeColor")); //$NON-NLS-1$
        changeHighlightColorButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                preferencesController.changeHighlightColorAction();
            }
        });

        highlightColorColoredLabel = new Label(mainPanel, SWT.NONE);
        highlightColorColoredLabel.setLayoutData("wmin 30pt"); //$NON-NLS-1$
        highlightColorColoredLabel.setBackground(new Color(display, bingo.getSettingsManager().getHighlightColor()));

        // Start bottom panel

        Label mainPanelSeparator = new Label(shell, SWT.HORIZONTAL | SWT.SEPARATOR);
        mainPanelSeparator.setLayoutData("grow"); //$NON-NLS-1$

        Composite bottomPanel = new Composite(shell, SWT.NONE);
        bottomPanel.setLayout(new MigLayout("fillx")); //$NON-NLS-1$
        bottomPanel.setLayoutData("growx, pushx"); //$NON-NLS-1$

        Composite spacer = new Composite(bottomPanel, SWT.NONE);
        spacer.setLayout(new MigLayout("insets 0")); //$NON-NLS-1$
        spacer.setLayoutData("growx, pushx"); //$NON-NLS-1$

        Button applyButton = new Button(bottomPanel, SWT.PUSH);
        applyButton.setLayoutData("sizegroup actionButtons, wmin 100"); //$NON-NLS-1$
        applyButton.setText(Messages.getString("Application.ButtonApply")); //$NON-NLS-1$
        applyButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                preferencesController.applyButtonAction();
            }
        });
        Button okButton = new Button(bottomPanel, SWT.PUSH);
        okButton.setLayoutData("sizegroup actionButtons"); //$NON-NLS-1$
        okButton.setText(Messages.getString("Application.ButtonOk")); //$NON-NLS-1$
        okButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                preferencesController.okButtonAction();
            }
        });
        Button cancelButton = new Button(bottomPanel, SWT.PUSH);
        cancelButton.setLayoutData("sizegroup actionButtons"); //$NON-NLS-1$
        cancelButton.setText(Messages.getString("Application.ButtonCancel")); //$NON-NLS-1$
        cancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                preferencesController.cancelButtonAction();
            }
        });
        shell.setDefaultButton(okButton);
    }

    public Text getAmountOfNumbersText() {
        return amountOfNumbersText;
    }

    public Text getAmountOfGridColumnsText() {
        return amountOfGridColumnsText;
    }

    public Text getWaitingSecondsBetweenNumbersText() {
        return waitingSecondsBetweenNumbersText;
    }

    public Label getNumbersFontValuesLabel() {
        return numbersFontValuesLabel;
    }

    public Text getMaximumHistoryLengthText() {
        return maximumHistoryLengthText;
    }

    public Text getMinimumHistoryFontSizeText() {
        return minimumHistoryFontSizeText;
    }

    public Text getMaximumHistoryFontSizeText() {
        return maximumHistoryFontSizeText;
    }

    public Text getHistoryNumbersGapText() {
        return historyNumbersGapText;
    }

    public Label getCurrentNumberColorColoredLabel() {
        return currentNumberColorColoredLabel;
    }

    public Label getPickedNumberColorColoredLabel() {
        return pickedNumberColorColoredLabel;
    }

    public Label getUnpickedNumberColorColoredLabel() {
        return unpickedNumberColorColoredLabel;
    }

    public Label getHighlightColorColoredLabel() {
        return highlightColorColoredLabel;
    }

    public Display getDisplay() {
        return display;
    }

    public Shell getShell() {
        return shell;
    }
}
