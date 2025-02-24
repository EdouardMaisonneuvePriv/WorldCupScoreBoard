/**
 * Represents a football match between two teams, tracking their scores and providing
 * functionality for sorting matches based on total goals and match ID.
 */
package ScoreboardLib.internal;

import java.util.concurrent.Semaphore;

public class Match implements Comparable<Match> {

    /** Semaphore for synchronizing access to the match counter. */
    private static Semaphore mutex = new Semaphore(1);
    
    /** Counter to generate unique match IDs. */
    private static int matchCounter = 1;
    
    /** Unique identifier for the match. */
    private final Integer matchId;
    
    /** Name of the home team. */
    private final String nameHomeTeam;
    
    /** Name of the visiting team. */
    private final String nameVisitorTeam;
    
    /** Score of the home team. */
    private Integer scoreHomeTeam;
    
    /** Score of the visiting team. */
    private Integer scoreVisitorTeam;

    /**
     * Constructs a new match with the given home and visitor team names.
     * Assigns a unique match ID using a synchronized counter.
     *
     * @param homeTeamName the name of the home team
     * @param visitorTeamName the name of the visiting team
     */
    public Match(String homeTeamName, String visitorTeamName) {
        this.mutex.acquireUninterruptibly();
        this.matchId = Match.matchCounter;
        Match.matchCounter++;
        this.mutex.release();
        
        this.nameHomeTeam = homeTeamName;
        this.nameVisitorTeam = visitorTeamName;
        this.scoreHomeTeam = 0;
        this.scoreVisitorTeam = 0;
    }

    /**
     * Sets the score for the home team.
     *
     * @param homeScore the new score of the home team
     */
    public void setHomeScore(Integer homeScore) {
        this.scoreHomeTeam = homeScore;
    }

    /**
     * Sets the score for the visiting team.
     *
     * @param visitorScore the new score of the visiting team
     */
    public void setVisitorScore(Integer visitorScore) {
        this.scoreVisitorTeam = visitorScore;
    }

    /**
     * Returns the unique match ID.
     *
     * @return the unique match ID
     */
    public Integer getMatchUniqueId() {
        return this.matchId;
    }

    /**
     * Returns the total number of goals scored in the match.
     *
     * @return the sum of home and visitor team scores
     */
    private Integer getTotalNumberGoals() {
        return this.scoreHomeTeam + this.scoreVisitorTeam;
    }

    /**
     * Returns a string representation of the match in the format:
     * "HomeTeam HomeScore - VisitorTeam VisitorScore".
     *
     * @return a string representation of the match
     */
    @Override
    public String toString() {
        return this.nameHomeTeam + " " + this.scoreHomeTeam + " - " +
                this.nameVisitorTeam + " " + this.scoreVisitorTeam;
    }

    /**
     * Compares this match to another match based on the total goals scored.
     * Matches with more goals are ranked higher. If two matches have the same total goals,
     * the match that started later (higher match ID) is ranked higher.
     *
     * @param otherMatch the other match to compare to
     * @return a negative integer, zero, or a positive integer as this match is less than,
     *         equal to, or greater than the specified match
     */
    @Override
    public int compareTo(Match otherMatch) {
        int goalComparison = Integer.compare(otherMatch.getTotalNumberGoals(), this.getTotalNumberGoals());
        if (goalComparison != 0) {
            return goalComparison;
        }
        return Integer.compare(otherMatch.matchId, this.matchId);
    }
}
