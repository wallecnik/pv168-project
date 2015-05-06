package cz.muni.fi.pv168.agentproject.gui;

import cz.muni.fi.pv168.agentproject.db.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

/**
 * This class represents the main window of the application.
 *
 * @author Wallecnik
 * @author Dužinka
 * @version 1.0-SNAPSHOT
 */
public class Gui {

    private static final Logger log = LoggerFactory.getLogger(Gui.class);

    // TODO: Add menu

    private static JFrame parent;
    private static ResourceBundle strings = ResourceBundle.getBundle("strings/strings");

    private JPanel assignments;
    private JButton assignButton;
    private JPanel guiMain;
    private JButton addAgentButton;
    private JButton addMissionButton;
    private JTable agentsTable;
    private JTable missionsTable;
    private JLabel assigningAgent;
    private JLabel assigningMission;
    private JButton deleteAgentButton;
    private JButton deleteMissionButton;
    private JButton showAgentAssButton;
    private JButton showMissionAssButton;
    private JButton cancelAssignmentButton;

    private AssignmentTableManager assignmentTableManager;

    /**
     * Creates the main window, creates all listeners and models, properly initialize
     * the application and displays the main window
     *
     * @param dataSource configured DataSource to be used with managers
     */
    public static void display(DataSource dataSource) {
        JFrame frame = new JFrame();
        frame.setContentPane(new Gui(frame, dataSource).guiMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(getStrings().getString("gui.main.title"));
        frame.setPreferredSize(new Dimension(1024, 768));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        log.debug("Main window displayed");
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
                        Mission newMission = addMission.getMissions();
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

        // TODO: Unable to catch ANY exception here! Not sure why.
        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indexAgent = agentsTable.getSelectedRow();
                int indexMission = missionsTable.getSelectedRow();

                // Check if both agent and mission are selected
                if (indexAgent >= 0 && indexMission >= 0) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            Agent agent = ((AgentTableModel) agentsTable.getModel()).getAgent(indexAgent);
                            Mission mission = ((MissionTableModel) missionsTable.getModel()).getMission(indexMission);

                            try {
                                assignmentTableManager.addAssignment(agent, mission);
                            } catch (IllegalArgumentException iae) {
                                System.out.println("Caught");
                                JOptionPane.showMessageDialog(parent, iae.getMessage(),
                                        "Add assignment", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    });
                }
            }
        });

        cancelAssignmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indexAgent = agentsTable.getSelectedRow();
                int indexMission = missionsTable.getSelectedRow();

                // Check if both agent and mission are selected
                if (indexAgent >= 0 && indexMission >= 0) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            Agent agent = ((AgentTableModel) agentsTable.getModel()).getAgent(indexAgent);
                            Mission mission = ((MissionTableModel) missionsTable.getModel()).getMission(indexMission);

                            try {
                                assignmentTableManager.removeAssignment(agent, mission);
                            } catch (NoSuchElementException nsee) {
                                JOptionPane.showMessageDialog(parent, nsee.getMessage(),
                                        "Delete assignment", JOptionPane.WARNING_MESSAGE);
                            }

                            // Don't allow zombie selection
                            agentsTable.clearSelection();
                            missionsTable.clearSelection();
                        }
                    });
                }
            }
        });

        showAgentAssButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indexAgent = agentsTable.getSelectedRow();
                if (indexAgent >= 0) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            Agent agent = ((AgentTableModel) agentsTable.getModel()).getAgent(indexAgent);
                            List<Mission> missions = assignmentTableManager.showMissionsForAgent(agent);
                            int indexMission;

                            // Refresh both tables selection
                            missionsTable.clearSelection();
                            agentsTable.setRowSelectionInterval(indexAgent, indexAgent);
                            if (missions != null) {

                                // For each mission in assignment, find its row index in mission table and add it to selection
                                for (Mission mission : missions) {
                                    indexMission = ((MissionTableModel) missionsTable.getModel()).getMissionIndex(mission);
                                    missionsTable.addRowSelectionInterval(indexMission, indexMission);
                                }
                            }
                        }
                    });
                }
            }
        });

        showMissionAssButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indexMission = missionsTable.getSelectedRow();
                if (indexMission >= 0) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            Mission mission = ((MissionTableModel) missionsTable.getModel()).getMission(indexMission);
                            List<Agent> agents = assignmentTableManager.showAgentsForMission(mission);
                            int indexAgent;

                            // Refresh both tables selection
                            agentsTable.clearSelection();
                            missionsTable.setRowSelectionInterval(indexMission, indexMission);
                            if (agents != null) {

                                // For each agent in assignment, find its row index in agent table and add it to selection
                                for (Agent agent : agents) {
                                    indexAgent = ((AgentTableModel) agentsTable.getModel()).getAgentIndex(agent);
                                    agentsTable.addRowSelectionInterval(indexAgent, indexAgent);
                                }
                            }
                        }
                    });
                }
            }
        });

        log.debug("listeners initialized");

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
        assignmentTableManager = new AssignmentTableManager(assignmentManager);

        agentsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        missionsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        agentsTable.setDefaultRenderer(Color.class, new AssignmentCellRenderer());
        missionsTable.setDefaultRenderer(Color.class, new AssignmentCellRenderer());

        agentsTable.setCellSelectionEnabled(true);
        agentsTable.setRowSelectionAllowed(true);
        agentsTable.setColumnSelectionAllowed(false);
        missionsTable.setCellSelectionEnabled(true);
        missionsTable.setRowSelectionAllowed(true);
        missionsTable.setColumnSelectionAllowed(false);

        agentsTable.setSelectionBackground(Color.GREEN);
        missionsTable.setSelectionBackground(Color.GREEN);

        // Enables sorting by clicking on the header column
        JTableHeader missionsTableHeader = missionsTable.getTableHeader();
        missionsTableHeader.addMouseListener(new TableHeaderMouseListener(missionsTable));

        log.debug("components set");
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

    /**
     * Creates dialog window with one message in parameter
     *
     * @param message Message to be shown
     */
    public static void alert(String message) {
        log.warn("input error: " + message);
        JOptionPane.showMessageDialog(parent, message, "Input error", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Returns bundle with locale strings
     *
     * @return ResourceBundle with strings
     */
    public static ResourceBundle getStrings() {
        return strings;
    }
}