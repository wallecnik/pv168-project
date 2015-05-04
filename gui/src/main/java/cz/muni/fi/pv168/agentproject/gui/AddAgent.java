package cz.muni.fi.pv168.agentproject.gui;

import cz.muni.fi.pv168.agentproject.db.Agent;
import cz.muni.fi.pv168.agentproject.db.Constants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            public void actionPerformed(ActionEvent e) {
                if (verifyAndAlert()) {
                    agent = new Agent(null, nameText.getText(), ZonedDateTime.of((Integer) yearText.getValue(), (Integer) monthText.getValue(), (Integer) dayText.getValue(), 0, 0, 0, 0, ZoneId.of("CET")).toInstant());
                    dialog.dispose();
                }
            }
        });
        cancelAgentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
    }

    /**
     * Displays this window
     */
    private void displayDialog() {
        dialog = new JDialog(parent, "AddAgent", true);
        dialog.setContentPane(addAgentMain);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(parent);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * Verifies the values associated in fields if they can be used for creating an Agent
     */
    private boolean verifyAndAlert() {
        if (nameText.getText() == null) {
            alert("agent name is null");
            return false;
        }
        if (nameText.getText().equals("")) {
            alert("agent name is empty");
            return false;
        }
        if (nameText.getText().length() > Constants.AGENT_NAME_MAX_LENGTH) {
            alert("agent name is too long");
            return false;
        }
        if (!Pattern.matches(Constants.AGENT_NAME_REGEX, nameText.getText())) {
            alert("agent name contains illegal characters");
            return false;
        }
        if((Integer)dayText.getValue() <= 0 || (Integer)dayText.getValue() > 31 || (Integer)monthText.getValue() <= 0 || (Integer)monthText.getValue() > 12) {
            alert("day or month is illegal");
            return false;
        }
        ZonedDateTime born = ZonedDateTime.of((Integer) yearText.getValue(), (Integer) monthText.getValue(), (Integer) dayText.getValue(), 0, 0, 0, 0, ZoneId.systemDefault());
        if (born.compareTo(ZonedDateTime.now()) > 0) {
            alert("Agent not born yet");
            return false;
        }
        if (born.plusYears(100).compareTo(ZonedDateTime.now()) < 0) {
            alert("Agent is too old");
            return false;
        }
        return true;
    }

    /**
     * Creates dialog window with one message in parameter
     */
    private void alert(String message) {
        JOptionPane.showMessageDialog(parent, message);
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
