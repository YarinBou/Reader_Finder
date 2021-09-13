import java.util.HashMap;
import java.util.List;

public class Matcher implements Runnable {
    private final List<String> lines;
    public final HashMap<String, List<MatchOffset>> matchMap;
    final int lineOffset;

    /**
     * gets a text string as input and searches for matches of a given set of
     * strings. The result is a map from a word to its location(s) in the text
     *
     * @param lines      - list Of the current lines.
     * @param lineOffset - Current section number.
     * @param matchMap   - variable for all threads that contains the names and the lineOffset and charOffset for each appearance.
     */
    public Matcher(List<String> lines, int lineOffset, HashMap<String, List<MatchOffset>> matchMap) {
        this.lines = lines;
        this.lineOffset = lineOffset;
        this.matchMap = matchMap;
    }

    @Override
    public void run() {
        int charOffset = 0;
        for (String line : lines) {
            getMatchesInLine(line, lineOffset, charOffset);
            charOffset += line.length();
        }
    }

    /**
     * @param line       - list Of the current lines.
     * @param lineOffset -  Current section number.
     * @param charOffset - the char number respective to the current section
     */
    private void getMatchesInLine(String line, int lineOffset, int charOffset) {
        String[] wordsArr = line.split("[/[^a-zA-Z]+/]");
        for (String s : wordsArr) {
            if (matchMap.containsKey(s)) {
                MatchOffset temp = new MatchOffset(lineOffset, charOffset);
                synchronized (matchMap) {
                    matchMap.get(s).add(temp);
                }
            }
            // the +1 is for each " " that we removed from the line.
            charOffset += s.length() + 1;
        }
    }
}
