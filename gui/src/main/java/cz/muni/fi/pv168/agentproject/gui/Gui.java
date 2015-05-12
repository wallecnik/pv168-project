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

    private static JFrame parent;
    private static ResourceBundle strings = ResourceBundle.getBundle("strings/strings");

    private JPanel assignments;
    private JButton assignButton;
    private JPanel guiMain;
    private JButton addAgentButton;
    private JButton addMissionButton;
    private JTable agentsTable;
    private JTable missionsTable;
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
        frame.setJMenuBar(createMenu());
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

                            assignmentTableManager.addAssignment(agent, mission);
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

                            assignmentTableManager.removeAssignment(agent, mission);

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
                    Agent agent = ((AgentTableModel) agentsTable.getModel()).getAgent(indexAgent);
                    assignmentTableManager.showMissionsForAgent(agent, indexAgent);
                }
            }
        });

        showMissionAssButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indexMission = missionsTable.getSelectedRow();
                if (indexMission >= 0) {
                    Mission mission = ((MissionTableModel) missionsTable.getModel()).getMission(indexMission);
                    assignmentTableManager.showAgentsForMission(mission, indexMission);
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
        assignmentTableManager = new AssignmentTableManager(assignmentManager, this);

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
        JMenu fileMenu = new JMenu(strings.getString("alert.file"));
        final JMenu aboutMenu = new JMenu(strings.getString("alert.about"));

        menubar.add(fileMenu);
        menubar.add(aboutMenu);

        JMenuItem exitMenuItem = new JMenuItem(strings.getString("alert.exit"));
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
        fileMenu.add(exitMenuItem);

        JMenuItem aboutMenuItem = new JMenuItem(strings.getString("alert.about"));
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(parent, strings.getString("alert.created.by"),
                        strings.getString("alert.about"), JOptionPane.INFORMATION_MESSAGE);
            }
        });
        aboutMenu.add(aboutMenuItem);

        log.debug("Menu created.");
        return menubar;
    }

    /**
     * Creates dialog window with one message in parameter
     *
     * @param message Message to be shown
     */
    public static void alert(String message) {
        log.warn("input error: " + message);
        JOptionPane.showMessageDialog(parent, message, strings.getString("gui.alert"), JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Returns bundle with locale strings
     *
     * @return ResourceBundle with strings
     */
    public static ResourceBundle getStrings() {
        return strings;
    }

    public JTable getAgentsTable() {
        return agentsTable;
    }

    public JTable getMissionsTable() {
        return missionsTable;
    }
}