package dev.ivank;

import java.util.Objects;

public class Match {
    private final String homeTeam;
    private final String awayTeam;
    // I have used a simple counter instead of keeping track of system time via Instant
    private final long startOrder;
    private final Score score;
    private MatchStatus status;

    public Match(String homeTeam, String awayTeam) {
        this.checkTeamName(homeTeam);
        this.checkTeamName(awayTeam);
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.startOrder = MatchSequence.nextOrder();
        this.score = new Score();
        this.status = MatchStatus.IN_PROGRESS;
    }

    private void checkTeamName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Team name cannot be null or empty: %s".formatted(name));
        }
    }

    public void updateScore(int homeTeam, int awayTeam) {
        if (homeTeam < 0 || awayTeam < 0) {
            throw new IllegalArgumentException("Score cannot be smaller than 0:" +
                    this.homeTeam + " " + homeTeam + " - " +
                    this.awayTeam + " " + awayTeam);
        }
        if (this.isFinished()) {
            throw new IllegalStateException("The match has already been finished.");
        }
        this.score.homeTeam = homeTeam;
        this.score.awayTeam = awayTeam;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public long getStartOrder() {
        return startOrder;
    }

    public Score getScore() {
        return score;
    }

    public int getTotalScore() {
        return score.getHomeTeam() + score.getAwayTeam();
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void finish() {
        if (this.status == MatchStatus.FINISHED) {
            throw new IllegalStateException("Match %s is already finished.".formatted(this));
        }
        this.status = MatchStatus.FINISHED;
    }

    public boolean isFinished() {
        return this.status == MatchStatus.FINISHED;
    }

    public boolean isInProgress() {
        return this.status == MatchStatus.IN_PROGRESS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(homeTeam, match.homeTeam) && Objects.equals(awayTeam, match.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, awayTeam);
    }

    @Override
    public String toString() {
        return this.homeTeam + " " + this.score.getHomeTeam()
                + " - " + this.awayTeam + " " + this.score.getAwayTeam();
    }

    public static class Score {
        private int homeTeam;
        private int awayTeam;

        public int getHomeTeam() {
            return homeTeam;
        }

        public int getAwayTeam() {
            return awayTeam;
        }
    }
}
