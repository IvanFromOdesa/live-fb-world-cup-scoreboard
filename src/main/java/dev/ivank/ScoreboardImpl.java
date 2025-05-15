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
public class Scoreboard {
    private final Map<String, Match> matches;

    /**
     * Constructs an empty Scoreboard.
     */
    public Scoreboard() {
        this.matches = new HashMap<>();
    }

    /**
     * Starts a new match between the given home and away teams.
     *
     * @param homeTeam The name of the home team.
     * @param awayTeam The name of the away team.
     * @return A unique key representing the started match.
     * @throws IllegalArgumentException If a match between the given teams has already been started.
     */
    public String startMatch(String homeTeam, String awayTeam) {
        final String key = generateKey(homeTeam, awayTeam);
        if (matches.containsKey(key)) {
            throw new IllegalArgumentException("Match %s has already been started.".formatted(key));
        }
        final Match match = new Match(homeTeam, awayTeam);
        matches.put(key, match);
        return key;
    }

    /**
     * Updates the score of an existing match.
     *
     * @param homeTeam      The name of the home team.
     * @param awayTeam      The name of the away team.
     * @param homeTeamScore The new score of the home team.
     * @param awayTeamScore The new score of the away team.
     * @return The key of the updated match.
     * @throws IllegalArgumentException If no match exists for the given teams or if the match is finished.
     */
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

    /**
     * Finishes an ongoing match between the given home and away teams.
     * The finished match is removed from the list of in-progress matches.
     *
     * @param homeTeam The name of the home team.
     * @param awayTeam The name of the away team.
     * @return The {@link Match} object that was finished.
     * @throws IllegalArgumentException If no match exists for the given teams or if the match is already finished.
     */
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

    /**
     * Retrieves a summary of all currently in-progress matches.
     * The matches are ordered first by the total score (highest first),
     * and then by the order in which they were started (most recently started first).
     *
     * @return A list of {@link Match} objects representing the in-progress matches,
     * sorted according to the specified criteria.
     */
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
     * @throws IllegalArgumentException If no match exists for the given key or if the match is finished.
     */
    private Match getMatch(String key) {
        final Match match = matches.get(key);
        if (match == null || match.isFinished()) {
            throw new IllegalArgumentException("No match %s was found.".formatted(key));
        }
        return match;
    }

    /**
     * Generates a unique key for a match based on the home and away teams.
     * The key is formed by concatenating the lowercase versions of the team names
     * with a hyphen in between (e.g., "home-away").
     *
     * @param homeTeam The name of the home team.
     * @param awayTeam The name of the away team.
     * @return A unique key for the match.
     */
    private String generateKey(String homeTeam, String awayTeam) {
        return homeTeam.toLowerCase() + "-" + awayTeam.toLowerCase();
    }
}
