package dev.ivank;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the state of ongoing and finished football matches.
 * Provides functionality to start new matches, update scores, finish matches,
 * and retrieve a summary of the currently in-progress matches.
 * @apiNote Not thread-safe.
 */
public class ScoreboardImpl implements Scoreboard {
    private final Map<String, Match> matches;

    /**
     * Constructs an empty Scoreboard.
     */
    public ScoreboardImpl() {
        this.matches = new HashMap<>();
    }

    public String startMatch(String homeTeam, String awayTeam) {
        final String key = generateKey(homeTeam, awayTeam);
        if (matches.containsKey(key)) {
            throw new IllegalArgumentException("Match %s has already been started.".formatted(key));
        }
        final Match match = new Match(homeTeam, awayTeam);
        matches.put(key, match);
        return key;
    }

    public String updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore) {
        final String key = generateKey(homeTeam, awayTeam);
        return this.updateScore(key, homeTeamScore, awayTeamScore);
    }

    /**
     * Updates the score of an existing match using its unique key.
     *
     * @param key           The unique key of the match.
     * @param homeTeamScore The new score of the home team.
     * @param awayTeamScore The new score of the away team.
     * @return The key of the updated match.
     * @throws IllegalArgumentException If no match exists for the given key or if the match is finished.
     */
    public String updateScore(String key, int homeTeamScore, int awayTeamScore) {
        final Match match = this.getMatch(key);
        match.updateScore(homeTeamScore, awayTeamScore);
        return key;
    }

    public Match finishMatch(String homeTeam, String awayTeam) {
        final String key = generateKey(homeTeam, awayTeam);
        return finishMatch(key);
    }

    /**
     * Finishes an ongoing match using its unique key.
     * The finished match is removed from the list of in-progress matches.
     *
     * @param key The unique key of the match to finish.
     * @return The {@link Match} object that was finished.
     * @throws IllegalArgumentException If no match exists for the given key or if the match is already finished.
     */
    public Match finishMatch(String key) {
        final Match match = this.getMatch(key);
        match.finish();
        return match;
    }

    public List<Match> getSummary() {
        return matches
                .values()
                .stream()
                .filter(Match::isInProgress)
                .sorted(
                        Comparator
                                .comparingInt(Match::getTotalScore)
                                .thenComparing(Match::getStartOrder)
                                .reversed()
                )
                .toList();
    }

    /**
     * Retrieves an ongoing match based on its unique key.
     *
     * @param key The unique key of the match.
     * @return The {@link Match} object associated with the key.
     * @throws IllegalArgumentException If no match exists for the given key.
     */
    private Match getMatch(String key) {
        final Match match = matches.get(key);
        if (match == null) {
            throw new IllegalArgumentException("No match %s was found.".formatted(key));
        }
        return match;
    }

    /**
     * Generates a unique key for a match based on the home and away teams.
     * The key is formed by concatenating the trimmed lowercase versions of the team names
     * with a hyphen in between (e.g., "home-away").
     *
     * @param homeTeam The name of the home team.
     * @param awayTeam The name of the away team.
     * @return A unique key for the match.
     */
    private String generateKey(String homeTeam, String awayTeam) {
        if (homeTeam == null || awayTeam == null) {
            throw new IllegalArgumentException("Team names cannot be null: %s - %s".formatted(homeTeam, awayTeam));
        }
        return homeTeam.trim().toLowerCase() + "-" + awayTeam.trim().toLowerCase();
    }
}
