package cz.muni.fi.pv168.agentproject.db;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class SetUpDb {

    public static void main (String[] args) {

        BasicDataSource bds = new BasicDataSource();
        bds.setUrl(DbHelper.DB_URL);
        bds.setUsername(DbHelper.USERNAME);
        bds.setPassword(DbHelper.PASSWORD);
        try (Connection connection = bds.getConnection()) {
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_ASSIGNMENT)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_AGENT)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_MISSION)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_CREATE_TABLE_AGENT)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_CREATE_TABLE_MISSION)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_CREATE_TABLE_ASSIGNMENT)
                    .executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
