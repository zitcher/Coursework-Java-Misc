package portfolio;

/**
 * This interface outlines the functions necessary for a command to be
 * executed by a REPL.
 * @author ZacharyHoffman
 */
public interface ReplUsable {
  /** Executes the command.
   * @param command A string array containing the parsed verion of the command
   * where each element of the array is an argument of the command.
   * @throws RuntimeException when command fails.
   */
  void run(String[] command) throws RuntimeException;
}
