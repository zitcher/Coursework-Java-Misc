package portfolio;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * LevenshteinDist checks generates a list of words that are a given
 * edit distance from the provided word.
 * @author Zachary Hoffman
 */
public class LevenshteinDist {
  private HashSet<String> wordHash;
  private List<String> letters;

  /**
   * Gives the LevenshteinDist the hash of valid words to reference for LED.
   * @param wordHash The hash of valid words.
   */
  public LevenshteinDist(HashSet<String> wordHash) {
    this.wordHash = wordHash;
    letters = Arrays.asList("abcdefghijklmnopqrstuvwxyz".split(""));
  }

  /**
   * Finds all words within a given edit distance from the given.
   * @param word The word to find similar words to.
   * @param dist The number of edits to search for a word within.
   * @return A list of words within the edit distance.
   */
  public List<String> genLedList(String word, int dist) {
    //find words, hash to stop duplicates
    HashSet<String> wordSet = new HashSet<String>();
    genLedListHelper(word, wordSet, 0, dist, 0);
    return new ArrayList<>(wordSet);
  }

  private void genLedListHelper(String word,
                                HashSet<String> wordSet,
                                int curDist,
                                int maxDist,
                                int pos) {
    /*
      Exits the function because:
      If there is no string to edit, there are no similar words.
      If we are past our edit distance there are no similar words.
      If we are past the word length we can make edits.
    */

    //if the word we are looking at is in our wordHash, we've found a
    // word within the distance!
    if (wordHash.contains(word)) {
      wordSet.add(word);
    }

    if (curDist >= maxDist || word.length() <= 0 || pos > word.length()) {
      return;
    } else {
      //recur through the edits!
      //insert every leter or replace the current letter
      for (String letter : letters) {
        //insert
        genLedListHelper(
            word.substring(0, pos) + letter + word.substring(pos),
            wordSet,
            curDist + 1,
            maxDist,
            pos + 1
        );

        //replace
        //makes sure we aren't removing the last letter
        if (pos < word.length()) {
          genLedListHelper(
              word.substring(0, pos) + letter + word.substring(pos + 1),
              wordSet,
              curDist + 1,
              maxDist,
              pos + 1
          );
        }
      }

      //remove the current letter
      if (pos < word.length()) {
        genLedListHelper(
            word.substring(0, pos) + word.substring(pos + 1),
            wordSet,
            curDist + 1,
            maxDist,
            pos + 1
        );
      } else {
        genLedListHelper(
            word.substring(0, pos),
            wordSet,
            curDist + 1,
            maxDist,
            pos + 1
        );
      }

      //don't do anything to the current letter
      genLedListHelper(
          word,
          wordSet,
          curDist,
          maxDist,
          pos + 1
      );

    }
  }
}
