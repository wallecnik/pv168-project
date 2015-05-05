package cz.muni.fi.pv168.agentproject.gui;

import cz.muni.fi.pv168.agentproject.db.*;

import javax.sql.DataSource;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class represents the main window of the application.
 *
 * @author Wallecnik
 * @author Dužinka
 * @version 1.0-SNAPSHOT
 */
public class Gui {
    //TODO: add logging

    private final JFrame parent;

    private JTabbedPane tabs;
    private JPanel missions;
    private JPanel assignments;
    private JPanel agents;
    private JTable agentsTable;
    private JTable missionsTable;
    private JButton assignButton;
    private JPanel guiMain;
    private JButton addAgentButton;
    private JButton addMissionButton;
    private JTable assignmentsAgentsTable;
    private JTable assignmentsMissionsTable;
    private JToolBar toolbar;
    private JLabel assigningAgent;
    private JLabel assigningMission;
    private JButton deleteAgentButton;
    private JButton deleteMissionButton;

    /**
     * Creates the main window, creates all listeners and models, properly initialize
     * the application and displays the main window
     *
     * @param dataSource configured DataSource to be used with managers
     */
    public static void display(DataSource dataSource) {
        JFrame frame = new JFrame("Gui");
        frame.setContentPane(new Gui(frame, dataSource).guiMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Assignment manager");
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Sets basic models for tables and registers listeners for buttons
     */
    private Gui(JFrame parent, DataSource dataSource) {

        this.parent = parent;

        setComponents(dataSource);

        addAgentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        AddAgent addAgent = new AddAgent(parent);
                        Agent retVal = addAgent.getAgent();
                        ((AgentTableModel) agentsTable.getModel()).addAgent(retVal);
                    }
                });
            }
        });

        deleteAgentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = agentsTable.getSelectedRow();
                ((AgentTableModel) agentsTable.getModel()).removeRow(index);
            }
        });

        addMissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        AddMission addMission = new AddMission(parent);
                        Mission newMission = addMission.getMission();
                        ((MissionTableModel) missionsTable.getModel()).addMission(newMission);
                    }
                });
            }
        });

        deleteMissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = missionsTable.getSelectedRow();
                ((MissionTableModel) missionsTable.getModel()).removeMission(index);
            }
        });

        JTableHeader missionsTableHeader = missionsTable.getTableHeader();
        missionsTableHeader.addMouseListener(new TableHeaderMouseListener(missionsTable));
    }

    /**
     * Sets models for tables
     */
    private void setComponents(DataSource dataSource) {
        AgentManager agentManager           = new AgentManagerImpl(dataSource);
        MissionManager missionManager       = new MissionManagerImpl(dataSource);
        AssignmentManager assignmentManager = new AssignmentManagerImpl(dataSource, agentManager, missionManager);

        agentsTable.setModel(new AgentTableModel(agentManager));
        missionsTable.setModel(new MissionTableModel(missionManager));

        assignmentsAgentsTable.setModel(new AgentTableModel(agentManager));
        assignmentsMissionsTable.setModel(new MissionTableModel(missionManager));
    }

    /**
     * Creates and attaches a menu to the main window. Wrote like this, because Intellij cannot
     * create menu in visual form designer
     */
    private static JMenuBar createMenu() {
        JMenuBar menubar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        final JMenu helpMenu = new JMenu("Help");

        menubar.add(fileMenu);
        menubar.add(helpMenu);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
        fileMenu.add(exitMenuItem);

        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(helpMenu, "Skvělá aplikace (c) Já", "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        helpMenu.add(aboutMenuItem);

        return menubar;
    }

}