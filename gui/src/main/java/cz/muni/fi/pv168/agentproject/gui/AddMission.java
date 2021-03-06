package cz.muni.fi.pv168.agentproject.gui;

import cz.muni.fi.pv168.agentproject.db.Constants;
import cz.muni.fi.pv168.agentproject.db.Mission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger log = LoggerFactory.getLogger(AddMission.class);

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
        log.debug("AddMission performed");
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
        log.debug("AddMission dialog window displayed");
    }

    /**
     * Checks whether the input is valid or not.
     * @param completedButton   Group of true/false radio buttons
     * @return                  true if everything went as expected
     */
    private boolean verifyAndAlert(ButtonGroup completedButton) {
        if (goalField.getText() == null) {
            alert(Gui.getStrings().getString("gui.alert.missions.goal.null"));
            log.debug("Inserted null mission goal.");
            return false;
        }
        if (goalField.getText().equals("")) {
            alert(Gui.getStrings().getString("gui.alert.missions.goal.empty"));
            log.debug("Inserted empty mission goal.");
            return false;
        }
        if (goalField.getText().length() > Constants.MISSION_GOAL_MAX_LENGTH) {
            alert(Gui.getStrings().getString("gui.alert.missions.goal.long"));
            log.debug("Inserted too long mission goal.");
            return false;
        }
        if ((int) requiredAgentsField.getValue() <= 0) {
            alert(Gui.getStrings().getString("gui.alert.missions.required_agents"));
            log.debug("Inserted negative number of mission's required agents.");
            return false;
        }

        Enumeration<AbstractButton> radioButtons = completedButton.getElements();
        while (radioButtons.hasMoreElements()) {
            if(radioButtons.nextElement().isSelected()) {
                log.debug("Inserted correct mission data.");
                return true;
            }
        }
        alert(Gui.getStrings().getString("gui.alert.missions.completed"));
        log.debug("Didn't specify mission true/false.");
        return false;
    }

    /**
     * Warning dialog window. Thrown when input was wrong.
     * @param message
     */
    private void alert(String message) {
        JOptionPane.showMessageDialog(parent, message, Gui.getStrings().getString("gui.alert.header.title"), JOptionPane.WARNING_MESSAGE);
    }

    /**
     * @return List of all missions
     */
    public Mission getMissions() {
        return mission;
    }
}
