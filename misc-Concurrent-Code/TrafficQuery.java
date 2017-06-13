package portfolio;

import java.time.Instant;
import java.util.HashMap;

import java.net.URLConnection;

import java.net.URL;

import java.io.IOException;
import java.util.Scanner;
import edu.brown.cs.seiferthoffman.generics.util.StringParser;

/**
 * Stores and retrieves actors.
 *
 * @author Zachary Hoffman
 */
public final class TrafficQuery {
  private static final String URL = "http://localhost:8080?last=";
  private static String prevTime;
  private static String prevUpdate;

  /**
   * Its a final class, nothing needs to be initialized.
   */
  private TrafficQuery() {

  }

  /**
   * Reset.
   *
   * @throws RuntimeException
   *           if fail to get query.
   */
  public static synchronized void reset() throws RuntimeException {
    query("0");
    prevUpdate = String.valueOf(Instant.now().getEpochSecond());
  }

  /**
   * Get the hashmap.
   *
   * @return the time.
   * @throws RuntimeException
   *           if fail to get query.
   */
  public static synchronized HashMap<String, Double> query()
      throws RuntimeException {
    return query(prevTime);
  }

  /**
   *
   * @param time
   *          the time.
   * @return the hashmap.
   * @throws RuntimeException
   *           if fail to get query.
   */
  private static synchronized HashMap<String, Double> query(String time)
      throws RuntimeException {

    try {
      URL trafficUrl = new URL(URL + time);
      prevTime = String.valueOf(Instant.now().getEpochSecond());
      prevUpdate = String.valueOf(Instant.now().getEpochSecond());

      URLConnection con = trafficUrl.openConnection();

      try (Scanner scanner = new Scanner(con.getInputStream())) {
        String responseBody = scanner.useDelimiter("\\A").next();
        return StringParser.gsonArrayToHash(responseBody);
      }
    } catch (IOException | RuntimeException e) {
      throw new RuntimeException("Failed to find traffic server.");
    }
  }

  /**
   * Gets traffic changes.
   *
   * @return A string jsonArray representing the changes in the traffic.
   * @throws RuntimeException
   *           if fail to get query.
   */
  public static synchronized HashMap<String, Double> getChanges()
      throws RuntimeException {
    try {
      URL trafficUrl = new URL(URL + prevUpdate);
      prevUpdate = String.valueOf(Instant.now().getEpochSecond());

      URLConnection con = trafficUrl.openConnection();

      try (Scanner scanner = new Scanner(con.getInputStream())) {
        String responseBody = scanner.useDelimiter("\\A").next();
        return StringParser.gsonArrayToHash(responseBody);
      }

      // System.out.println(response.toString());
    } catch (IOException | RuntimeException e) {
      throw new RuntimeException("Failed to find traffic server.");
    }
  }

  /**
   * See traffic changes but do not update time.
   *
   * @return A string jsonArray representing the changes in the traffic.
   * @throws RuntimeException
   *           if fail to get query.
   */
  public static synchronized HashMap<String, Double> seeChanges()
      throws RuntimeException {
    try {
      URL trafficUrl = new URL(URL + prevUpdate);

      URLConnection con = trafficUrl.openConnection();

      try (Scanner scanner = new Scanner(con.getInputStream())) {
        String responseBody = scanner.useDelimiter("\\A").next();
        return StringParser.gsonArrayToHash(responseBody);
      }

      // System.out.println(response.toString());
    } catch (IOException | RuntimeException e) {
      throw new RuntimeException("Failed to find traffic server.");
    }
  }
}
