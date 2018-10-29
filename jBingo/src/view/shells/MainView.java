package view.shells;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ArmEvent;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import controller.MainViewController;
import model.Bingo;
import net.miginfocom.swt.MigLayout;
import view.Messages;
import view.ViewUtils;

public class MainView {

    private Shell shell;
    private Display display;

    private Composite mainPanel;
    private Composite historicalNumbersPanel;
    private Composite numberNamesPanel;

    private Button pickNumberButton;
    private Label numberNameLabel;

    private List<Label> numberLabels;
    private List<Label> historicalNumberLabels;

    private Color nonHighlightedBackgroundColor;

    private Runnable pickNumberTimer;
    private int pickNumberSecondsLeft;

    private MenuItem fullScreenMenuItem;
    private MenuItem historyMenuItem;

    private MainViewController mainViewController;
    private Bingo bingo;

    public MainView(Bingo bingo, Display display, MainViewController mainViewController) {
        this.bingo = bingo;
        this.mainViewController = mainViewController;
        this.display = display;
        shell = new Shell();
        addGlobalShortcuts();
        createContents();
        shell.pack();
        ViewUtils.centerShell(shell);
    }

    private void setFullScreen(boolean fullScreen) {
        shell.setFullScreen(fullScreen);
        // Just in case there is any issue setting the full screen
        fullScreenMenuItem.setSelection(shell.getFullScreen());
    }

    private void addGlobalShortcuts() {
        Listener globalKeyListener = new Listener() {
            public void handleEvent(Event e) {
                switch (e.keyCode) {
                    case SWT.ESC:
                        if (shell.getFullScreen()) {
                            setFullScreen(false);
                        }
                        break;
                    case 'f':
                        if (e.stateMask == SWT.CTRL) {
                            setFullScreen(!shell.getFullScreen());
                        }
                        break;
                }
            }
        };
        display.addFilter(SWT.KeyDown, globalKeyListener);
    }

    public void open() {
        shell.setText(Messages.getString("Application.Title")); //$NON-NLS-1$
        shell.layout();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    public void createContents() {
        ViewUtils.removeAllChildren(shell);
        shell.setLayout(new MigLayout("wrap 1")); //$NON-NLS-1$

        mainPanel = new Composite(shell, SWT.NONE);
        mainPanel.setLayout(new MigLayout(String.format("wrap %d, fill, gap 10", bingo.getSettingsManager().getAmountOfGridColumns()))); // $NON-NLS-1$
        mainPanel.setLayoutData("grow, push"); //$NON-NLS-1$

        Label mainPanelSeparator = new Label(shell, SWT.HORIZONTAL | SWT.SEPARATOR);
        mainPanelSeparator.setLayoutData("grow"); //$NON-NLS-1$

        Composite bottomPanel = new Composite(shell, SWT.NONE);
        bottomPanel.setLayout(new MigLayout("fill")); //$NON-NLS-1$
        bottomPanel.setLayoutData("growx, pushx"); //$NON-NLS-1$

        numberLabels = new ArrayList<>();
        historicalNumberLabels = new ArrayList<>();

        pickNumberButton = new Button(bottomPanel, SWT.PUSH);
        pickNumberButton.setLayoutData("growy, pushy, sizegroup bottomButton, hmin 100"); //$NON-NLS-1$
        pickNumberButton.setText(Messages.getString("MainView.NextNumber")); //$NON-NLS-1$

        Composite middleBottomPanel = new Composite(bottomPanel, SWT.NONE);
        middleBottomPanel.setLayout(new MigLayout("fill")); //$NON-NLS-1$
        middleBottomPanel.setLayoutData("growx, pushx"); //$NON-NLS-1$

        historicalNumbersPanel = new Composite(middleBottomPanel, SWT.NONE);
        historicalNumbersPanel.setLayout(new MigLayout(String.format("fill, hidemode 3, gap %d", bingo.getSettingsManager().getHistoryNumbersGap()))); // $NON-NLS-1$
        historicalNumbersPanel.setLayoutData("align center, wrap"); //$NON-NLS-1$

        createHistoricalNumberControls();

        numberNamesPanel = new Composite(middleBottomPanel, SWT.NONE);
        numberNamesPanel.setLayout(new MigLayout("fill")); // $NON-NLS-1$
        numberNamesPanel.setLayoutData("align center, hidemode 3"); //$NON-NLS-1$

        numberNameLabel = new Label(numberNamesPanel, SWT.NONE);
        Font numberNamesLabelFont = new Font(display, bingo.getSettingsManager().getNumberNamesFontData());
        numberNameLabel.setFont(numberNamesLabelFont);

        Button resetButton = new Button(bottomPanel, SWT.PUSH);
        resetButton.setLayoutData("growy, pushy, align right, sizegroup bottomButton"); //$NON-NLS-1$
        resetButton.setText(Messages.getString("MainView.Reset")); //$NON-NLS-1$

        createMenu();

        pickNumberTimer = new PickNumberTimer();
        pickNumberButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mainViewController.pickNumberButtonAction();
                pickNumberSecondsLeft = bingo.getSettingsManager().getWaitingSecondsBetweenNumbers();
                pickNumberTimer.run();
            }
        });

        resetButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mainViewController.resetButtonAction();
            }
        });
        setupGrid();
        setupHistoricalNumbers();
        setupNumberNamesPanel();
    }

    private void createHistoricalNumberControls() {
        Color pickedNumberColor = new Color(display, bingo.getSettingsManager().getPickedNumberColor());
        FontDescriptor fontDescriptor = FontDescriptor.createFrom(bingo.getSettingsManager().getNumbersFontData());
        int maximumHistoryLength = bingo.getSettingsManager().getMaximumHistoryLength();
        int minimumHistoryFontSize = bingo.getSettingsManager().getMinimumHistoryFontSize();
        double fontSizeIncrement = (bingo.getSettingsManager().getMaximumHistoryFontSize() - minimumHistoryFontSize) / (double) (maximumHistoryLength - 1);
        for (int i = 0; i < maximumHistoryLength; i++) {
            int fontSize = minimumHistoryFontSize + (int) Math.round(i * fontSizeIncrement);
            Label label = new Label(historicalNumbersPanel, SWT.NONE);
            label.setForeground(pickedNumberColor);
            Font font = fontDescriptor.setHeight(fontSize).createFont(display);
            label.setFont(font);
            historicalNumberLabels.add(label);
        }
        Color currentNumberColor = new Color(display, bingo.getSettingsManager().getCurrentNumberColor());
        historicalNumberLabels.get(historicalNumberLabels.size() - 1).setForeground(currentNumberColor);
    }

    private void createMenu() {
        Menu menuBar = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menuBar);

        MenuItem optionsMenuItem = new MenuItem(menuBar, SWT.CASCADE);
        optionsMenuItem.setText(Messages.getString("MainView.MenuOptions")); //$NON-NLS-1$

        Menu optionsMenu = new Menu(shell, SWT.DROP_DOWN);
        optionsMenuItem.setMenu(optionsMenu);

        fullScreenMenuItem = new MenuItem(optionsMenu, SWT.CHECK);
        fullScreenMenuItem.setText(Messages.getString("MainView.MenuFullScreen")); //$NON-NLS-1$

        MenuItem preferencesMenuItem = new MenuItem(optionsMenu, SWT.PUSH);
        preferencesMenuItem.setText(Messages.getString("MainView.MenuPreferences")); //$NON-NLS-1$

        historyMenuItem = new MenuItem(optionsMenu, SWT.PUSH);
        historyMenuItem.setText(Messages.getString("MainView.MenuHistory")); //$NON-NLS-1$

        MenuItem exitMenuItem = new MenuItem(optionsMenu, SWT.PUSH);
        exitMenuItem.setText(Messages.getString("MainView.MenuExit")); //$NON-NLS-1$

        optionsMenuItem.addArmListener(new ArmListener() {
            @Override
            public void widgetArmed(ArmEvent event) {
                historyMenuItem.setEnabled(!bingo.getNumberBag().getPickedNumbers().isEmpty());
            }
        });
        fullScreenMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setFullScreen(!shell.getFullScreen());
            }
        });
        preferencesMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mainViewController.preferencesButtonAction();
            }
        });
        historyMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mainViewController.historyButtonAction();
            }
        });
        exitMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mainViewController.exitButtonAction();
            }
        });

    }

    private void setupGrid() {
        numberLabels.clear();
        mainPanel.setVisible(false);

        ViewUtils.removeAllChildren(mainPanel);

        Color fontColor = new Color(display, bingo.getSettingsManager().getUnpickedNumberColor());
        Font numberLabelFont = new Font(display, bingo.getSettingsManager().getNumbersFontData());

        int numberAmount = bingo.getNumberBag().getNumberAmount();
        for (int i = 0; i < numberAmount; i++) {
            Composite mainLabelComposite = new Composite(mainPanel, SWT.BORDER);
            mainLabelComposite.setLayout(new MigLayout("fill, insets 0")); //$NON-NLS-1$
            mainLabelComposite.setLayoutData("grow, push, sizegroup mainLabelComposite"); //$NON-NLS-1$
            Label label = new Label(mainLabelComposite, SWT.CENTER);
            label.setLayoutData("align center, gap 0, growx"); //$NON-NLS-1$
            label.setText(String.valueOf(i + 1));
            label.setFont(numberLabelFont);
            label.setForeground(fontColor);
            numberLabels.add(label);
        }
        nonHighlightedBackgroundColor = numberLabels.get(0).getParent().getBackground();
        mainPanel.layout();
        mainPanel.setVisible(true);
    }

    private void setupHistoricalNumbers() {
        for (Label label : historicalNumberLabels) {
            label.setVisible(false);
            label.setText(""); //$NON-NLS-1$
            historicalNumbersPanel.getParent().layout();
        }
        historicalNumberLabels.get(bingo.getSettingsManager().getMaximumHistoryLength() - 1).setVisible(true);
        historicalNumbersPanel.getParent().layout();
    }

    private void setupNumberNamesPanel() {
        numberNamesPanel.setVisible(bingo.getSettingsManager().getShowNumberNames());
    }

    public void reset() {
        pickNumberButtonReset(true);
        pickNumberSecondsLeft = 0;
        setupGrid();
        setupHistoricalNumbers();
        setupNumberNamesPanel();
    }

    private void pickNumberButtonReset(boolean enabled) {
        display.timerExec(-1, pickNumberTimer);
        pickNumberButton.setText(Messages.getString("MainView.NextNumber")); //$NON-NLS-1$
        pickNumberButton.setEnabled(enabled);
        pickNumberButton.getParent().layout();
    }

    public void pickNumber(int number, String numberName) {
        Label label = getMainNumberLabelFromNumber(number);
        List<Integer> historicalNumbers = bingo.getNumberBag().getPickedNumbers();

        Color currentNumberFontColor = new Color(display, bingo.getSettingsManager().getCurrentNumberColor());
        label.setForeground(currentNumberFontColor);

        Color highlightColor = new Color(display, bingo.getSettingsManager().getHighlightColor());
        new LabelHighlighter(this.display, label, highlightColor).run();

        try {
            int previousNumber = historicalNumbers.get(historicalNumbers.size() - 2);
            Label previousNumberLabel = getMainNumberLabelFromNumber(previousNumber);
            previousNumberLabel.setBackground(this.nonHighlightedBackgroundColor);
            previousNumberLabel.getParent().setBackground(this.nonHighlightedBackgroundColor);
            Color previousNumberFontColor = new Color(display, bingo.getSettingsManager().getPickedNumberColor());
            previousNumberLabel.setForeground(previousNumberFontColor);
        } catch (IndexOutOfBoundsException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.numberNameLabel.setText(numberName == null ? "" : numberName);

        updateHistoricalNumbers(number);
    }

    private void updateHistoricalNumbers(int lastNumber) {
        int maximumHistoryLength = bingo.getSettingsManager().getMaximumHistoryLength();
        for (int i = 0; i < maximumHistoryLength - 1; i++) {
            Label currentLabel = historicalNumberLabels.get(i);
            Label nextLabel = historicalNumberLabels.get(i + 1);
            currentLabel.setText(nextLabel.getText());
            if (!currentLabel.getText().isEmpty() && !currentLabel.isVisible()) {
                currentLabel.setVisible(true);
            }
            historicalNumbersPanel.getParent().layout();
        }
        historicalNumberLabels.get(maximumHistoryLength - 1).setText(String.valueOf(lastNumber));
        historicalNumbersPanel.getParent().layout();
    }

    private Label getMainNumberLabelFromNumber(int number) {
        return numberLabels.get(number - 1);
    }

    public void pickedLastNumber() {
        pickNumberButtonReset(false);
    }

    public Display getDisplay() {
        return display;
    }

    public Shell getShell() {
        return shell;
    }

    private class PickNumberTimer implements Runnable {
        @Override
        public void run() {
            if (!pickNumberButton.isDisposed() && bingo.getNumberBag().hasMorePendingNumbers()) {
                if (pickNumberSecondsLeft > 0) {
                    if (pickNumberSecondsLeft == bingo.getSettingsManager().getWaitingSecondsBetweenNumbers()) {
                        pickNumberButton.setEnabled(false);
                    }
                    pickNumberButton.setText(String.valueOf(pickNumberSecondsLeft));
                    pickNumberSecondsLeft--;
                    display.timerExec(1000, this);
                } else {
                    pickNumberButtonReset(true);
                }
            }
        }
    }

    private class LabelHighlighter implements Runnable {

        private Display display;
        private Label label;
        private RGB highlightColor;
        private Color initialColor;
        private int alpha = 255;

        public LabelHighlighter(Display display, Label label, Color highlightColor) {
            this.display = display;
            this.label = label;
            this.highlightColor = highlightColor.getRGB();
            this.initialColor = label.getBackground();
        }

        @Override
        public void run() {
            if (label.isDisposed()) {
                return;
            }

            if (alpha < 0) {
                label.setBackground(this.initialColor);
                label.getParent().setBackground(this.initialColor);

                return;
            }

            Color newColor = new Color(display, highlightColor, alpha);
            label.setBackground(newColor);
            label.getParent().setBackground(newColor);

            alpha -= 5;
            display.timerExec(30, this);
        }
    }
}
