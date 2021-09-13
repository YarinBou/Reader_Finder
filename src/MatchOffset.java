
public class MatchOffset {
    int lineOffset;
    int charOffset;

    /**
     * Empty constructor
     */
    public MatchOffset() {
        lineOffset = 0;
        charOffset = 0;
    }

    /**
     * Constructor
     *
     * @param lineNum - Current section number.
     * @param charNum - the char number respective to the current section
     */
    public MatchOffset(int lineNum, int charNum) {
        this.lineOffset = lineNum;
        this.charOffset = charNum;
    }

    /**
     * @return a string representation of the MatchOffset object.
     */
    public String toString() {
        return "[lineOffset: " + lineOffset + " charOffset: " + charOffset + "]";
    }
}
