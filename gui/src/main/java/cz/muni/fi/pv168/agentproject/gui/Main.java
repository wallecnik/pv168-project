package cz.muni.fi.pv168.agentproject.gui;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Creates the DataSource connection pool and runs the application
 *
 * @author Wallecnik
 * @version 1.0-SNAPSHOT
 */
public class Main {

    public static final Logger log = LoggerFactory.getLogger(Main.class);

    //TODO: refactor logging in the ALL modules to SLF4J

    public static void main(String[] args) {

        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(new File("src/main/resources/config.properties"))) {
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {

                log.info("Application starts");

                log.info("Opening database connection");
                BasicDataSource bds = new BasicDataSource();
                bds.setUrl(prop.getProperty("db.url") + prop.getProperty("db.dbname"));
                bds.setUsername(prop.getProperty("db.username"));
                bds.setPassword(prop.getProperty("db.password"));

                Gui.display(bds);

            }
        });

    }

}
