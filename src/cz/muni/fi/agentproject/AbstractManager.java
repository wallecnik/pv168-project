package cz.muni.fi.agentproject;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Wallecnik on 24.03.15.
 */
public abstract class AbstractManager {

    /**
     * Retrieves a newly generated key from given ResultSet or throws an exception
     * if more keys or no key were found
     */
    protected Long getKeyFromRS(ResultSet resultSet, Object causeInstance) throws SQLException {

        Long newId;

        if (resultSet.first()) {
            if (resultSet.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retrieving failed when trying to insert agent " + causeInstance
                        + " - wrong key fields count: " + resultSet.getMetaData().getColumnCount());
            }

            newId = resultSet.getLong(1);

            if (! resultSet.isLast()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retrieving failed when trying to insert agent " + causeInstance
                        + " - more keys found");
            }
        }
        else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retrieving failed when trying to insert agent " + causeInstance
                    + " - no key found");
        }

        return newId;
    }

}
