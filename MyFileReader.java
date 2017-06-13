package portfolio;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;

import java.util.ArrayList;
import java.util.List;

/**
 * MyFileReader reads a file with the BufferedReader and returns of list of its
 * lines.
 * @author Zachary Hoffman
 */
public final class MyFileReader {

  // ensures no use of default constructor
  private MyFileReader() {

  }

  /**
   * Reads a UTF8 encoded file and returns a list of its constents.
   * @param filePath String representing the path to the file to read.
   * @return An ordered list of strings in which each string corresponds to
   * a line in the file.
   * @throws IOException If BufferedReader fails to read csv.
   */
  public static List<String> readFile(String filePath) throws IOException {
    //fill this with stars read off the file
    List<String> contents = new ArrayList<String>();

    //reads the file!
    BufferedReader reader = null;
    try {
      File file = new File(filePath);
      reader = new BufferedReader(new InputStreamReader(
                                    new FileInputStream(file), "UTF8"));
      while (true) {
        String line = reader.readLine();

        //if hit the end of the file retrun list of star objects
        if (line == null) {
          return contents;
        //Otherwise add line to the list.
        } else {
          contents.add(line);
        }
      }
    } catch (IOException e) {
      throw new IOException("ERROR: "
                            + "FileReader failed to read or find file.");
    } finally {
      try {
        reader.close();
      } catch (IOException | NullPointerException e) {
        throw new IOException("ERROR: "
                              + "BufferedReader failed to close.");
      }
    }
  }

}
