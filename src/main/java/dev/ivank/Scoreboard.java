package dev.ivank;

import java.util.List;

public interface Scoreboard {

    /**
     * Starts a new match between the given home and away teams.
     *
     * @param homeTeam The name of the home team.
     * @param awayTeam The name of the away team.
     * @return A unique key representing the started match.
     * @throws IllegalArgumentException If a match between the given teams has already been started.
     */
    String startMatch(String homeTeam, String awayTeam);

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
    String updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore);

    /**
     * Finishes an ongoing match between the given home and away teams.
     * The finished match is removed from the list of in-progress matches.
     *
     * @param homeTeam The name of the home team.
     * @param awayTeam The name of the away team.
     * @return The {@link Match} object that was finished.
     * @throws IllegalArgumentException If no match exists for the given teams or if the match is already finished.
     */
    Match finishMatch(String homeTeam, String awayTeam);

    /**
     * Retrieves a summary of all currently in-progress matches.
     * The matches are ordered first by the total score (highest first),
     * and then by the order in which they were started (most recently started first).
     *
     * @return A list of {@link Match} objects representing the in-progress matches,
     * sorted according to the specified criteria.
     */
    List<Match> getSummary();
}
