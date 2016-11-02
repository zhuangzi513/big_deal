package common;

import java.sql.*;

public abstract class TableRecord {
    public abstract void bindToSQLStatement(PreparedStatement insertStatement) throws SQLException;
};
