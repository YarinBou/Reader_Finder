import java.util.*;

public class Aggregator {
    /**
     * aggregates the results from all the matchers and prints the results.
     *
     * @param matchMap - variable for all threads that contains the names and the lineOffset and charOffset for each appearance.
     */
    public Aggregator(HashMap<String, List<MatchOffset>> matchMap) {
        matchMap.forEach((key, value) -> {
            if (!value.isEmpty()) {
                System.out.println(key + " --> " + value);
            } else {
                System.out.println(key + " --> This first name is not found in the text.");
            }
        });
    }
}
