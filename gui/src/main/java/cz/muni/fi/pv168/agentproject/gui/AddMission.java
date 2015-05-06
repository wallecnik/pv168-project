package cz.muni.fi.pv168.agentproject.gui;

import cz.muni.fi.pv168.agentproject.db.Constants;
import cz.muni.fi.pv168.agentproject.db.Mission;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

/**
 * Created by Wallecnik on 04.05.15.
 * @author Dužinka
 */
public class AddMission {
    private JDialog         dialog;
    private JFrame          parent;

    private JPanel          addMissionMain;
    private JTextField      goalField;
    private JRadioButton    completedTrue;
    private JRadioButton    completedFalse;
    private JSpinner        requiredAgentsField;
    private JButton         sendMissionButton;
    private JButton         cancelMissionButton;

    private Mission         mission = null;

    public AddMission(JFrame parent) {
        this.parent = parent;

        ButtonGroup completedButton = new ButtonGroup();
        completedButton.add(completedTrue);
        completedButton.add(completedFalse);

        sendMissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (verifyAndAlert(completedButton)) {
                    boolean completed;
                    completed = completedButton.getElements().nextElement().isSelected();
                    mission = new Mission(null, goalField.getText(), (int) requiredAgentsField.getValue(), completed);
                    dialog.dispose();
                }
            }
        });

        cancelMissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        displayDialog();
    }

    /**
     * Displays dialog window for adding a mission
     */
    private void displayDialog() {
        dialog = new JDialog(parent, true);
        dialog.setContentPane(addMissionMain);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(parent);
        dialog.setTitle(Gui.getStrings().getString("gui.form.missions.main.title"));
        dialog.setPreferredSize(new Dimension(300, 300));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    /**
     * Checks whether the input is valid or not.
     * @param completedButton   Group of true/false radio buttons
     * @return                  true if everything went as expected
     */
    private boolean verifyAndAlert(ButtonGroup completedButton) {
        if (goalField.getText() == null) {
            alert("Mission goal cannot be null!");
            return false;
        }
        if (goalField.getText().equals("")) {
            alert("Mission goal cannot be empty!");
            return false;
        }
        if (goalField.getText().length() > Constants.MISSION_GOAL_MAX_LENGTH) {
            alert("Mission goal is too lengthy!");
            return false;
        }
        if ((int) requiredAgentsField.getValue() <= 0) {
            alert("There must be at least one agent for each mission!");
            return false;
        }

        Enumeration<AbstractButton> radioButtons = completedButton.getElements();
        while (radioButtons.hasMoreElements()) {
            if(radioButtons.nextElement().isSelected()) {
                return true;
            }
        }
        alert("You must choose whether the mission is completed or not!");
        return false;
    }

    /**
     * Warning dialog window. Thrown when input was wrong.
     * @param message
     */
    private void alert(String message) {
        JOptionPane.showMessageDialog(parent, message, "Input error", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * @return List of all missions
     */
    public Mission getMissions() {
        return mission;
    }
}
