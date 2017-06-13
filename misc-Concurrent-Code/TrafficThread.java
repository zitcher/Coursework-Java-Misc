package portfolio;

import java.util.Map;




/**
 * The ways command finds all paths in a box.
 * @author Zachary Hoffman
 */
public class TrafficThread extends Thread {
  private static Map<String, Double> trafficChanges;

  /** Thread that gets new traffic values.
   * @param trafficChanges list to store traffic updates in.
  */
  public TrafficThread(Map<String, Double> trafficChanges) {
    this.trafficChanges = trafficChanges;

    try {
      TrafficQuery.reset();
    } catch (RuntimeException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
    * Method that adds traffic changes to the trafficChanges list.
  */
  @Override
  public void run() {
    try {
      //add new traffic
      Map<String, Double> changes = TrafficQuery.getChanges();
      Map<String, Double> routeTraffic = RouteCommand.getTraffic();
      synchronized (trafficChanges) {
        trafficChanges.putAll(changes);
      }

      synchronized (routeTraffic) {
        routeTraffic.putAll(changes);
      }
    } catch (RuntimeException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}
