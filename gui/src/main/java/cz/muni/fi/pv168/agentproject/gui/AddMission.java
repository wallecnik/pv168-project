package cz.muni.fi.pv168.agentproject.gui;

import javax.swing.*;

/**
 * Created by Wallecnik on 04.05.15.
 */
public class AddMission {
    private JTextField textField5;
    private JRadioButton trueRadioButton;
    private JRadioButton falseRadioButton;
    private JButton sendButton1;
    private JSpinner spinner1;
    private JPanel addMissionMain;

    public static void main() {
        JFrame frame = new JFrame("AddMission");
        frame.setContentPane(new AddMission().addMissionMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
