package cz.muni.fi.pv168.agentproject.gui;

import cz.muni.fi.pv168.agentproject.db.DbHelper;
import org.apache.commons.dbcp2.BasicDataSource;

import java.awt.*;

/**
 * Creates the DataSource connection pool and runs the application
 *
 * @author Wallecnik
 * @version 1.0-SNAPSHOT
 */
public class Main {
    //TODO: add logging

    //TODO: refactor logging in the ALL modules to SLF4J

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {

                BasicDataSource bds = new BasicDataSource();
                bds.setUrl(DbHelper.DB_URL);
                bds.setUsername(DbHelper.USERNAME);
                bds.setPassword(DbHelper.PASSWORD);

                Gui.display(bds);

            }
        });

    }

}
