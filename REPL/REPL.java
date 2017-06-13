package portfolio;

import java.util.HashMap;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * This class runs the REPL and can execute commands the REPL holds.
 *
 * @author ZacharyHoffman
 */
public final class REPL {
  private HashMap<String, ReplUsable> commands;
  /** Initializes a REPL.
   * @param commands HashMap with pairs of string a ReplUsable command.
   * The ReplUsable command will be run when the string is inputted into
   * the REPL.
   */
  REPL(HashMap<String, ReplUsable> commands) {
    this.commands = commands;
  }

  /** Runs the REPL.
   */
  public void runREPL() {
    //BufferedReader reads the csv
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(System.in, "UTF8"));

      while (true) {
        String line = reader.readLine();
        //hit an EOF then end the REPL
        if (line == null) {
          //close the reader
          reader.close();
          break;
        }

        // parses the command with regex to a list of strings
        // if command starts with quote, end command at the next quote
        String[] parsedLine = line.split(
                                " (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        //gets command from commandlist
        ReplUsable command = commands.get(parsedLine[0]);

        //if command isn't in the hash print command not found.
        if (command == null) {
          System.out.println("ERROR: " + parsedLine[0] + " not found.");
        } else {
          //if the command was found, run the command.
          try {
            command.run(parsedLine);
          } catch (RuntimeException e) {
            //e.printStackTrace();
            System.out.println("ERROR: " + e.getMessage());
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("ERROR: BufferedReader failed to read input.");
    } finally {
      try {
        reader.close();
      } catch (IOException | NullPointerException e) {
        throw new RuntimeException(
          "ERROR: BufferedReader failed to read input.");
      }
    }
  }

  /** Runs a command supported by the REPL.
   * @param commandToRun The name of a command to run. The REPL runs the command
   * whose key in this REPLS hashmap is paired with the command.
   */
  public void runCommand(String commandToRun) {
    // parses the command with regex to a list of strings
    // if command starts with quote, end command at the next quote
    String[] parsedLine = commandToRun.split(
                            " (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

    //gets command from commandlist
    ReplUsable command = commands.get(parsedLine[0]);

    //if command isn't in the hash print command not found.
    if (command == null) {
      System.out.println("ERROR: " + parsedLine[0] + " not found.");
    } else {
      //if the command was found, run the command.
      try {
        command.run(parsedLine);
      } catch (RuntimeException e) {
        System.out.println("ERROR: " + e.getMessage());
      }

    }
  }
}
