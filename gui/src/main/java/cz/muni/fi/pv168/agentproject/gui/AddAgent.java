package cz.muni.fi.pv168.agentproject.gui;

import cz.muni.fi.pv168.agentproject.db.Agent;
import cz.muni.fi.pv168.agentproject.db.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    //TODO: add logging
    // TODO: Instant formatting

    private JDialog dialog;
    private JFrame parent;

    private Agent agent = null;

    private JTextField nameText;
    private JSpinner dayText;
    private JSpinner monthText;
    private JSpinner yearText;
    private JButton addAgentButton;
    private JPanel addAgentMain;
    private JButton cancelAgentButton;

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

        addAgentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (verifyAndAlert()) {
                    ZonedDateTime zonedDateTime = null;
                    try {
                        ZonedDateTime.of((Integer) yearText.getValue(), (Integer) monthText.getValue(), (Integer) dayText.getValue(), 0, 0, 0, 0, ZoneId.of("CET"));
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
        dialog.setVisible(true);
    }

    /**
     * Verifies the values associated in fields if they can be used for creating an Agent
     */
    private boolean verifyAndAlert() {
        if (nameText.getText() == null) {
            alert(Gui.getStrings().getString("gui.alert.agents.name.null"));
            return false;
        }
        if (nameText.getText().equals("")) {
            alert(Gui.getStrings().getString("gui.alert.agents.name.empty"));
            return false;
        }
        if (nameText.getText().length() > Constants.AGENT_NAME_MAX_LENGTH) {
            alert(Gui.getStrings().getString("gui.alert.agents.name.long"));
            return false;
        }
        if (!Pattern.matches(Constants.AGENT_NAME_REGEX, nameText.getText())) {
            alert(Gui.getStrings().getString("gui.alert.agents.name.illegal"));
            return false;
        }
        if((Integer)dayText.getValue() <= 0 || (Integer)dayText.getValue() > 31 || (Integer)monthText.getValue() <= 0 || (Integer)monthText.getValue() > 12) {
            alert(Gui.getStrings().getString("gui.alert.agents.date.not_possible"));
            return false;
        }
        ZonedDateTime born = null;
        try {
            born = ZonedDateTime.of((Integer) yearText.getValue(), (Integer) monthText.getValue(), (Integer) dayText.getValue(), 0, 0, 0, 0, ZoneId.systemDefault());
        } catch (DateTimeException e) {
            alert(Gui.getStrings().getString("gui.alert.agents.date.not_possible"));
            return false;
        }
        if (born.compareTo(ZonedDateTime.now()) > 0) {
            alert(Gui.getStrings().getString("gui.alert.agents.date.not_born_yet"));
            return false;
        }
        if (born.plusYears(100).compareTo(ZonedDateTime.now()) < 0) {
            alert(Gui.getStrings().getString("gui.alert.agents.date.too_old"));
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
}
