package dk.aau.cs.ds306e18.tournament;

public class MatchResult {

    private int blueScore = 0;
    private int orangeScore = 0;

    public Conclusion getConclusion() {
        if (blueScore == orangeScore) return Conclusion.DRAW;
        if (blueScore > orangeScore) return Conclusion.BLUE_WINS;
        return Conclusion.ORANGE_WINS;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public void setBlueScore(int blueScore) {
        this.blueScore = blueScore;
    }

    public int getOrangeScore() {
        return orangeScore;
    }

    public void setOrangeScore(int orangeScore) {
        this.orangeScore = orangeScore;
    }
}
