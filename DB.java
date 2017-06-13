package portfolio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;

/**
 * Provides database connections.
 * @author Zachary Hoffman
 */
public final class DB {
  private Connection conn;
  private Map<String, PreparedStatement> prepHash = new HashMap<>();

  /** Represents a connection to a database.
   * @param db Path to database.
   * @throws ClassNotFoundException If fails to find jdbc.sqlite class
   * @throws SQLException If fails to find database.
   */
  public DB(String db) throws ClassNotFoundException, SQLException {
    // Set up a connection and store it in a field
    Class.forName("org.sqlite.JDBC");
    String url = "jdbc:sqlite:" + db;

    // stop conn from creating a file if does not exists
    SQLiteConfig config = new SQLiteConfig();
    config.resetOpenMode(SQLiteOpenMode.CREATE);

    //connect to the file
    conn = DriverManager.getConnection(url, config.toProperties());
    try (Statement stat = conn.createStatement();) {
      stat.executeUpdate("PRAGMA foreign_keys = ON;");
    }
  }

  /**
   * Closes and cleans up any resources.
   * @throws SQLException If fails to close items.
   */
  public void close() throws SQLException {
    Exception foundExeption = null;
    for (PreparedStatement prep : prepHash.values()) {
      try {
        prep.close();
      } catch (Exception e) {
        foundExeption = e;
      }
    }
    // Close the connection
    try {
      conn.close();
    } catch (SQLException e) {
      throw new SQLException("Failed to close database: " + e);
    }


    // we do this so we know we tried to close every connection
    // unfortunately we only recieve the final connection error
    if (foundExeption != null) {
      throw new SQLException("Failed to close database prepareStatement: "
                             + foundExeption);
    }
  }

  /** Executes the given sql query and replaces ?'s with the strings in args.
   * The ? are used for hashing and efficiency.
   * @param command The sql query to execute.
   * @param args The args of the command.
   * @return A list of the results of the sql query.
   * @throws SQLException If command fails to execute.
   */
  public List<String> stringCommand(String command,
                                    String[] args) throws SQLException {
    // checks if we have statement stored in cache, otherwise create it and
    // cache it.
    PreparedStatement prep;
    if (prepHash.containsKey(command)) {
      prep = prepHash.get(command);
    } else {
      prep = conn.prepareStatement(command);
      prepHash.put(command, prep);
    }
    // Fills command with given args
    try {
      for (int i = 1; i <= args.length; i++) {
        prep.setString(i, args[i - 1]);
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new SQLException("Failed to fill command" + e);
    }

    //executes command and returns results
    List<String> toReturn = new ArrayList<String>();
    try (ResultSet rs = prep.executeQuery()) {
      while (rs.next()) {
        toReturn.add(rs.getString(1));
      }
    } catch (SQLException e) {
      throw new SQLException("Failed to execute query: " + e);
    }
    return toReturn;
  }

  /** Execute the given sql query on the database.
   * @param command The sql query to execute.
   * @throws SQLException If command fails to execute.
   */
  public void stringCommandNoReturn(String command) throws SQLException {
    try (PreparedStatement prep =  conn.prepareStatement(command)) {
      //executes command
      try {
        prep.execute();
      } catch (SQLException e) {
        throw new SQLException("Failed to execute query: " + e);
      }
    }
  }
}
