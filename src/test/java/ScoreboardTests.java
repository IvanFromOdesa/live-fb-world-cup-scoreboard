import dev.ivank.Match;
import dev.ivank.Scoreboard;
import dev.ivank.ScoreboardImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScoreboardTests {
    private Scoreboard scoreboard;

    @BeforeEach
    public void setup() {
        scoreboard = new ScoreboardImpl();
    }

    @Test
    void shouldStartMatchSuccessfully() {
        String key = scoreboard.startMatch("Mexico", "Canada");
        assertEquals("mexico-canada", key);
    }

    @Test
    void shouldThrow_whenGivenEmptyTeamNames() {
        assertThrows(
                IllegalArgumentException.class,
                () -> scoreboard.startMatch("", "")
        );
    }

    @Test
    void shouldThrow_whenGivenNullTeamNames() {
        assertThrows(
                IllegalArgumentException.class,
                () -> scoreboard.startMatch(null, null)
        );
    }

    @Test
    void shouldThrow_whenMatchAlreadyStarted() {
        scoreboard.startMatch("Mexico", "Canada");
        assertThrows(
                IllegalArgumentException.class,
                () -> scoreboard.startMatch("Mexico", "Canada")
        );
    }

    @Test
    void shouldUpdateScoreSuccessfully_whenGivenValidScore() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 1, 2);
        final Match match = scoreboard.getSummary().get(0);
        assertEquals(1, match.getScore().getHomeTeam());
        assertEquals(2, match.getScore().getAwayTeam());
        scoreboard.updateScore("Mexico", "Canada", 2, 3);
        assertEquals(2, match.getScore().getHomeTeam());
        assertEquals(3, match.getScore().getAwayTeam());
    }

    @Test
    void shouldUpdateScoreSuccessfullyToZeros() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 0, 0);
        final Match match = scoreboard.getSummary().get(0);
        assertEquals(0, match.getScore().getHomeTeam());
        assertEquals(0, match.getScore().getAwayTeam());
    }

    @Test
    void shouldThrow_whenUpdatingScoreWithNegativeValues() {
        scoreboard.startMatch("Mexico", "Canada");
        assertThrows(
                IllegalArgumentException.class,
                () -> scoreboard.updateScore("Mexico", "Canada", -1, 3)
        );
    }

    @Test
    void shouldThrow_whenUpdatingScoreWithNullTeamNames() {
        scoreboard.startMatch("Mexico", "Canada");
        assertThrows(
                IllegalArgumentException.class,
                () -> scoreboard.updateScore(null, null, 1, 3)
        );
    }

    @Test
    void shouldThrow_whenUpdatingScoreForNonExistingMatch() {
        assertThrows(
                IllegalArgumentException.class,
                () -> scoreboard.updateScore("Mexico", "Canada", 1, 1)
        );
    }

    @Test
    void shouldThrow_whenUpdatingScoreForFinishedMatch() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.finishMatch("Mexico", "Canada");
        assertThrows(
                IllegalStateException.class,
                () -> scoreboard.updateScore("Mexico", "Canada", 1, 1)
        );
    }

    @Test
    void shouldFinishMatchSuccessfully() {
        scoreboard.startMatch("Mexico", "Canada");
        final Match match = scoreboard.finishMatch("Mexico", "Canada");
        assertTrue(match.isFinished());
    }

    @Test
    void shouldThrow_whenFinishingNonExistingMatch() {
        assertThrows(
                IllegalArgumentException.class,
                () -> scoreboard.finishMatch("Mexico", "Canada")
        );
    }

    @Test
    void shouldThrow_whenFinishingAlreadyFinishedMatch() {
        scoreboard.startMatch("Norway", "Sweden");
        final Match match = scoreboard.finishMatch("Norway", "Sweden");
        assertThrows(
                IllegalStateException.class,
                match::finish
        );
    }

    @Test
    void shouldGetSummarySortedByScoreAndStartOrder() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 0, 5);

        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.updateScore("Spain", "Brazil", 10, 2);

        scoreboard.startMatch("Germany", "France");
        scoreboard.updateScore("Germany", "France", 2, 2);

        scoreboard.startMatch("Netherlands", "Belgium");
        scoreboard.updateScore("Netherlands", "Belgium", 1, 3);

        final List<Match> summary = scoreboard.getSummary();

        // Expected order:
        // Spain - Brazil (12),
        // Mexico - Canada (5),
        // Netherlands - Belgium (4)
        // Germany - France (4)
        assertEquals("Spain", summary.get(0).getHomeTeam());
        assertEquals("Mexico", summary.get(1).getHomeTeam());
        assertEquals("Netherlands", summary.get(2).getHomeTeam());
        assertEquals("Germany", summary.get(3).getHomeTeam());
    }

    @Test
    void shouldGetSummaryOnlyForInProgressMatches() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("France", "Portugal");
        scoreboard.finishMatch("Mexico", "Canada");

        final List<Match> summary = scoreboard.getSummary();
        assertEquals(1, summary.size());
        assertEquals("France", summary.get(0).getHomeTeam());
    }
}
