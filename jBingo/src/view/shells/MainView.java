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
    private Button pickNumberButton;

    private List<Label> numberLabels;
    private List<Label> historicalNumberLabels;

    private Font numberLabelFont;

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
        numberLabelFont = new Font(display, bingo.getSettingsManager().getNumbersFontData());

        mainPanel = new Composite(shell, SWT.NONE);
        mainPanel.setLayout(new MigLayout(
                String.format("wrap %d, fill, gap 10", bingo.getSettingsManager().getAmountOfGridColumns()))); //$NON-NLS-1$
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

        historicalNumbersPanel = new Composite(bottomPanel, SWT.NONE);
        historicalNumbersPanel.setLayout(new MigLayout(
                String.format("fill, hidemode 3, gap %d", bingo.getSettingsManager().getHistoryNumbersGap()))); //$NON-NLS-1$
        historicalNumbersPanel.setLayoutData("align center"); //$NON-NLS-1$

        createHistoricalNumberControls();

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
    }

    private void createHistoricalNumberControls() {
        Color pickedNumberColor = new Color(display, bingo.getSettingsManager().getPickedNumberColor());
        FontDescriptor fontDescriptor = FontDescriptor.createFrom(bingo.getSettingsManager().getNumbersFontData());
        int maximumHistoryLength = bingo.getSettingsManager().getMaximumHistoryLength();
        int minimumHistoryFontSize = bingo.getSettingsManager().getMinimumHistoryFontSize();
        double fontSizeIncrement = (bingo.getSettingsManager().getMaximumHistoryFontSize() - minimumHistoryFontSize)
                / (double) (maximumHistoryLength - 1);
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
        int numberAmount = bingo.getNumberBag().getNumberAmount();
        for (int i = 0; i < numberAmount; i++) {
            Composite mainLabelComposite = new Composite(mainPanel, SWT.BORDER);
            mainLabelComposite.setLayout(new MigLayout("fill, insets 0")); //$NON-NLS-1$
            mainLabelComposite.setLayoutData("grow, push, sizegroup mainLabelComposite"); //$NON-NLS-1$
            Label label = new Label(mainLabelComposite, SWT.NONE);
            label.setLayoutData("align center, gap 0"); //$NON-NLS-1$
            label.setText(String.valueOf(i + 1));
            label.setFont(numberLabelFont);
            label.setForeground(fontColor);
            numberLabels.add(label);
        }
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

    public void reset() {
        pickNumberButtonReset(true);
        pickNumberSecondsLeft = 0;
        setupGrid();
        setupHistoricalNumbers();
    }

    private void pickNumberButtonReset(boolean enabled) {
        display.timerExec(-1, pickNumberTimer);
        pickNumberButton.setText(Messages.getString("MainView.NextNumber")); //$NON-NLS-1$
        pickNumberButton.setEnabled(enabled);
        pickNumberButton.getParent().layout();
    }

    public void pickNumber(int number) {
        Label label = getMainNumberLabelFromNumber(number);
        List<Integer> historicalNumbers = bingo.getNumberBag().getPickedNumbers();
        Color currentNumberFontColor = new Color(display, bingo.getSettingsManager().getCurrentNumberColor());
        label.setForeground(currentNumberFontColor);
        try {
            int previousNumber = historicalNumbers.get(historicalNumbers.size() - 2);
            Label previousNumberLabel = getMainNumberLabelFromNumber(previousNumber);
            Color previousNumberFontColor = new Color(display, bingo.getSettingsManager().getPickedNumberColor());
            previousNumberLabel.setForeground(previousNumberFontColor);
        } catch (IndexOutOfBoundsException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
