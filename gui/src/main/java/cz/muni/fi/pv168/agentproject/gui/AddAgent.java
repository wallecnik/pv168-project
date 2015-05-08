package cz.muni.fi.pv168.agentproject.gui;

import cz.muni.fi.pv168.agentproject.db.Agent;
import cz.muni.fi.pv168.agentproject.db.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;
import java.util.regex.Pattern;

/**
 * This class is simple dialog window for creating a new Agent and inserting it into the database.
 * After this window is properly closed (with the Button), the reference to this object can be used
 * to retrieve the newly created Agent. Otherwise the created Agent is lost.
 *
 * @author Wallecnik
 * @version 1.0-SNAPSHOT
 */
public class AddAgent {

    private JDialog dialog;
    private JFrame parent;

    private Agent agent = null;

    private JTextField nameText;
    private JComboBox dayText;
    private JComboBox monthText;
    private JComboBox yearText;
    private JButton addAgentButton;
    private JPanel addAgentMain;
    private JButton cancelAgentButton;

    private static final Logger log = LoggerFactory.getLogger(AddAgent.class);

    /**
     * Constructor for objects of this class. Sets the layout and basic listeners
     * for buttons and displays the dialog window
     *
     * After this method the displayDialog() should be called in order to display
     * the dialog window.
     *
     * @param parent Parent frame of the new window
     */
    public AddAgent(JFrame parent) {
        this.parent = parent;

        populateComboBoxes(dayText, monthText, yearText);

        addAgentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (verifyAndAlert()) {
                    ZonedDateTime zonedDateTime = null;
                    try {
                        zonedDateTime = ZonedDateTime.of((Integer) yearText.getSelectedItem(), (Integer) monthText.getSelectedItem(), (Integer) dayText.getSelectedItem(), 0, 0, 0, 0, ZoneId.of("CET"));
                        agent = new Agent(null, nameText.getText(), zonedDateTime.toInstant());
                        dialog.dispose();
                    } catch (DateTimeException e) {
                        alert(Gui.getStrings().getString("gui.alert.agents.date.not_possible"));
                    }
                }
            }
        });
        cancelAgentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        displayDialog();
        log.debug("AddAgent performed.");
    }

    /**
     * Displays this window
     */
    private void displayDialog() {
        dialog = new JDialog(parent, true);
        dialog.setContentPane(addAgentMain);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(parent);
        dialog.setTitle(Gui.getStrings().getString("gui.form.agents.main.title"));
        dialog.setPreferredSize(new Dimension(300, 300));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        log.debug("AddMission dialog window displayed");
    }

    /**
     * Verifies the values associated in fields if they can be used for creating an Agent
     */
    private boolean verifyAndAlert() {
        if (nameText.getText() == null) {
            alert(Gui.getStrings().getString("gui.alert.agents.name.null"));
            log.debug("Inserted null agent name.");
            return false;
        }
        if (nameText.getText().equals("")) {
            alert(Gui.getStrings().getString("gui.alert.agents.name.empty"));
            log.debug("Inserted empty agent name.");
            return false;
        }
        if (nameText.getText().length() > Constants.AGENT_NAME_MAX_LENGTH) {
            alert(Gui.getStrings().getString("gui.alert.agents.name.long"));
            log.debug("Inserted too long agent name.");
            return false;
        }
        if (!Pattern.matches(Constants.AGENT_NAME_REGEX, nameText.getText())) {
            alert(Gui.getStrings().getString("gui.alert.agents.name.illegal"));
            log.debug("Inserted invalid agent name.");
            return false;
        }
        if((Integer)dayText.getSelectedItem() <= 0 || (Integer)dayText.getSelectedItem() > 31 || (Integer)monthText.getSelectedItem() <= 0 || (Integer)monthText.getSelectedItem() > 12) {
            alert(Gui.getStrings().getString("gui.alert.agents.date.not_possible"));
            log.debug("Inserted invalid agent date.");
            return false;
        }
        ZonedDateTime born = null;
        try {
            born = ZonedDateTime.of((Integer) yearText.getSelectedItem(), (Integer) monthText.getSelectedItem(), (Integer) dayText.getSelectedItem(), 0, 0, 0, 0, ZoneId.systemDefault());
        } catch (DateTimeException e) {
            alert(Gui.getStrings().getString("gui.alert.agents.date.not_possible"));
            log.debug("Inserted invalid agent date.");
            return false;
        }
        if (born.compareTo(ZonedDateTime.now()) > 0) {
            alert(Gui.getStrings().getString("gui.alert.agents.date.not_born_yet"));
            log.debug("Inserted agent that has not been born yet.");
            return false;
        }
        if (born.plusYears(100).compareTo(ZonedDateTime.now()) < 0) {
            alert(Gui.getStrings().getString("gui.alert.agents.date.too_old"));
            log.debug("Inserted too old agent.");
            return false;
        }
        return true;
    }

    /**
     * Creates dialog window with one message in parameter
     */
    private void alert(String message) {
        JOptionPane.showMessageDialog(parent, message, Gui.getStrings().getString("gui.alert.header.title"), JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Returns the created agent or null if the agent was not created yet.
     *
     * @return a new Agent or null
     */
    public Agent getAgent() {
        return agent;
    }

    private void populateComboBoxes(JComboBox dayText, JComboBox monthText, JComboBox yearText) {
        for (int day = 1; day <= 31; day++) {
            dayText.addItem(day);
        }

        for (int month = 1; month <= 12; month++) {
            monthText.addItem(month);
        }

        Instant dateNow = Instant.now();
        LocalDateTime ldt = LocalDateTime.ofInstant(dateNow, ZoneId.systemDefault());
        int yearNow = ldt.getYear();

        for (int year = yearNow - 100; year <= yearNow; year++) {
            yearText.addItem(year);
        }
        log.debug("Agent date comboboxes populated.");
    }
}
