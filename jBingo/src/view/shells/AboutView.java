package view.shells;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

import controller.AboutController;
import net.miginfocom.swt.MigLayout;
import view.Messages;
import view.ViewUtils;

public class AboutView {

    private Shell shell;
    private Shell parentShell;
    private Display display;

    private AboutController aboutController;

    public AboutView(AboutController aboutController, Display display, Shell parentShell) {
        this.aboutController = aboutController;
        this.display = display;
        this.parentShell = parentShell;
        createContents();
        shell.pack();
    }

    public void open() {
        shell.layout();
        shell.open();
        ViewUtils.centerShell(shell);
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private Font createDerivedSystemFont(double sizeMultiplier, int style) {
        Font systemFont = display.getSystemFont();
        try {
            String fontName = systemFont.getFontData()[0].getName();
            int fontSize = (int) Math.floor(systemFont.getFontData()[0].getHeight() * sizeMultiplier);
            FontData valueLabelFontData = new FontData(fontName, fontSize, style);

            return new Font(display, valueLabelFontData);
        } catch (Exception e) {
            return systemFont;
        }
    }

    private void createContents() {
        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
        shell.setLayout(new MigLayout("wrap 1")); //$NON-NLS-1$
        shell.setText(Messages.getString("AboutView.Title")); //$NON-NLS-1$

        Composite mainPanel = new Composite(shell, SWT.NONE);
        mainPanel.setLayout(new MigLayout("wrap 2, fill, gapy 10", "[right]rel[]")); //$NON-NLS-1$
        mainPanel.setLayoutData("grow, push"); //$NON-NLS-1$

        Font appNameLabelFont = createDerivedSystemFont(2.5, SWT.BOLD);
        Font valueLabelFont = createDerivedSystemFont(1.25, SWT.NONE);
        Font labelLabelFont = createDerivedSystemFont(1.25, SWT.BOLD);

        Label appNameLabel = new Label(mainPanel, SWT.NONE);
        appNameLabel.setLayoutData("skip 1");
        appNameLabel.setText("jBingo"); //$NON-NLS-1$
        appNameLabel.setFont(appNameLabelFont);

        Label versionLabelLabel = new Label(mainPanel, SWT.NONE);
        versionLabelLabel.setText(Messages.getString("AboutView.Version")); //$NON-NLS-1$
        versionLabelLabel.setFont(valueLabelFont);

        Label versionValueLabel = new Label(mainPanel, SWT.NONE);
        versionValueLabel.setText("1.0.0"); //$NON-NLS-1$
        versionValueLabel.setFont(labelLabelFont);

        Label dateLabelLabel = new Label(mainPanel, SWT.NONE);
        dateLabelLabel.setText(Messages.getString("AboutView.Date")); //$NON-NLS-1$
        dateLabelLabel.setFont(valueLabelFont);

        Label dateValueLabel = new Label(mainPanel, SWT.NONE);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2018-11-06");
            String dateString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
            dateValueLabel.setText(dateString); //$NON-NLS-1$
        } catch (Exception e) {
        }
        dateValueLabel.setFont(labelLabelFont);

        Label developedByLabelLabel = new Label(mainPanel, SWT.NONE);
        developedByLabelLabel.setText(Messages.getString("AboutView.DevelopedBy")); //$NON-NLS-1$
        developedByLabelLabel.setFont(valueLabelFont);

        Label developedByValueLabel = new Label(mainPanel, SWT.NONE);
        developedByValueLabel.setText("Gabriel Zanetti"); //$NON-NLS-1$
        developedByValueLabel.setFont(labelLabelFont);

        Label urlByLabelLabel = new Label(mainPanel, SWT.NONE);
        urlByLabelLabel.setText(Messages.getString("AboutView.Url")); //$NON-NLS-1$
        urlByLabelLabel.setFont(valueLabelFont);

        String url = "https://github.com/pupi1985/jbingo";
        Link urlValueLink = new Link(mainPanel, SWT.NONE);
        urlValueLink.setText(String.format("<a href=\"%s\">github.com/pupi1985/jbingo</a>", url)); //$NON-NLS-1$
        urlValueLink.setFont(labelLabelFont);
        urlValueLink.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Program.launch(url);
            }
        });

        Composite bottomPanel = new Composite(shell, SWT.NONE);
        bottomPanel.setLayout(new MigLayout("fill, gapx 8, gapy 14")); //$NON-NLS-1$
        bottomPanel.setLayoutData("span 2, grow, push"); //$NON-NLS-1$

        Button okButton = new Button(bottomPanel, SWT.PUSH);
        okButton.setLayoutData("wmin 100, align center"); //$NON-NLS-1$
        okButton.setText(Messages.getString("Application.ButtonOk")); //$NON-NLS-1$
        okButton.setFocus();
        okButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                aboutController.okButtonAction();
            }
        });
        shell.setDefaultButton(okButton);
    }

    public Display getDisplay() {
        return display;
    }

    public Shell getShell() {
        return shell;
    }
}
