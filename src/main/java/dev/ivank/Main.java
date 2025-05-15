package dev.ivank;

public class Main {
    public static void main(String[] args) {
        final Scoreboard scoreboard = new ScoreboardImpl();
        addMatch(scoreboard, "Mexico", "Canada", 0, 5);
        addMatch(scoreboard, "Spain", "Brazil", 10, 2);
        addMatch(scoreboard, "Germany", "France", 2, 2);
        addMatch(scoreboard, "Uruguay", "Italy", 6, 6);
        addMatch(scoreboard, "Argentina", "Australia", 3, 1);

        scoreboard.finishMatch("Mexico", "Canada");

        scoreboard.getSummary().forEach(System.out::println);
    }

    private static void addMatch(Scoreboard scoreboard, String homeTeam, String awayTeam, int hts, int ats) {
        scoreboard.startMatch(homeTeam, awayTeam);
        scoreboard.updateScore(homeTeam, awayTeam, hts, ats);
    }
}