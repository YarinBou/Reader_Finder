import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String names = "James,John,Robert,Michael,William,David,Richard,Charles,Joseph,Thomas,Christopher," +
                "Daniel,Paul,Mark,Donald,George,Kenneth,Steven,Edward,Brian,Ronald,Anthony,Kevin,Jason," +
                "Matthew,Gary,Timothy,Jose,Larry,Jeffrey,Frank,Scott,Eric,Stephen,Andrew,Raymond,Gregory," +
                "Joshua,Jerry,Dennis,Walter,Patrick,Peter,Harold,Douglas,Henry,Carl,Arthur,Ryan,Roger";
        String[] commonEnglishNames = names.split(",");
        HashMap<String, List<MatchOffset>> matchMap = new HashMap<>();
        // initialization matchMap keys and values.
        for (String name : commonEnglishNames) {
            matchMap.put(name, Collections.synchronizedList(new ArrayList<>()));
        }
        final int part = 1000;
        String path = "C:\\Users\\yarin\\IdeaProjects\\BigID_home_task\\Data\\Big";
        // reads a large text file in parts and sends each part (as string) to a matcher.
        ReadAndSend(path, matchMap, part);
        new Aggregator(matchMap);
//        findOptimalPartSize(path,commonEnglishNames);
    }

    /**
     * reads a large text file in parts (e.g. 1000 lines in each part) and
     * sends each part (as string) to a matcher.
     *
     * @param path     = big.txt file path.
     * @param matchMap = variable for all threads that contains the names and the lineOffset and charOffset for each appearance.
     * @param part     = represents the size of each section.
     */
    private static void ReadAndSend(String path, HashMap<String, List<MatchOffset>> matchMap, int part) {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        List<String> listOfLines = new ArrayList<>();
        BufferedReader reader;
        String tempLine;
        int lineCounter = 0;
        int lineOffset = 0;
        try {
            reader = new BufferedReader(new FileReader(path));
            while ((tempLine = reader.readLine()) != null) {
                if (lineCounter % part == part - 1) {
                    //Read a full section == 'part' var.
                    cachedPool.execute(new Matcher(listOfLines, lineOffset, matchMap));
                    listOfLines = new ArrayList<>();
                    lineOffset += part;
                } else {
                    listOfLines.add(tempLine);
                }
                lineCounter++;
            }
            //When we have reached the end of the text we will add the rest of the text.
            if (listOfLines.size() != 0) {
                cachedPool.execute(new Matcher(listOfLines, lineOffset, matchMap));
            }
            reader.close();
            cachedPool.shutdown();
            //Wait for termination
            cachedPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void findOptimalPartSize(String path, String[] commonEnglishNames) {
        final int[] partsArr = {500, 1000, 1500, 2000, 2500, 5000, 10000, 15000};
        for (int num : partsArr) {
            HashMap<String, List<MatchOffset>> matchMap = new HashMap<>();
            // initialization matchMap keys and values.
            for (String name : commonEnglishNames) {
                matchMap.put(name, Collections.synchronizedList(new ArrayList<>()));
            }
            long startTimePart = System.currentTimeMillis();
            ReadAndSend(path, matchMap, num);
            long elapsedTimePart = (System.currentTimeMillis() - startTimePart);
            System.out.println("for Part in size: " + num + " lines -> Execution time: " + elapsedTimePart + "ms");
        }
    }
}
