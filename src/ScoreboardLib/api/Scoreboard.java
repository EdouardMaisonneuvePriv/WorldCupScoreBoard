package ScoreboardLib.api;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import ScoreboardLib.internal.Match;
import java.util.concurrent.Semaphore;

/**
 * The {@code Scoreboard} class manages ongoing football matches.
 * It allows starting, updating, terminating matches, and retrieving match summaries.
 * Thread safety is ensured using a semaphore for synchronization.
 */
public class Scoreboard {

    /** A semaphore used for synchronizing access to the scoreboard's match data. */
    private Semaphore mutex;

    /** A map storing matches, indexed by their unique match ID. */
    private Map<Integer, Match> listMatches;

    /**
     * Constructs a new {@code Scoreboard} instance.
     */
    public Scoreboard() {
        listMatches = new HashMap<>();
        this.mutex = new Semaphore(1);
    }

    /**
     * Starts a new match with the given home and visitor team names.
     *
     * @param nameHomeTeam  the name of the home team
     * @param nameVisitorTeam the name of the visiting team
     * @return the unique match ID of the created match
     * @throws IllegalArgumentException if team names are null, empty, or identical
     */
    public Integer startMatch(String nameHomeTeam, String nameVisitorTeam) {
        if (nameHomeTeam == null || nameVisitorTeam == null) {
            throw new IllegalArgumentException("Team names must be non-null");
        }
        
        // Removing all the spaces at the beginning and end of the 
        // names of the 2 teams.
        String trimmedNameHome = nameHomeTeam.trim();
        String trimmedNameVisitor = nameVisitorTeam.trim();
        
        if (trimmedNameHome.isEmpty() || trimmedNameVisitor.isEmpty()) {
            throw new IllegalArgumentException("Team names must be non-empty");
        }
        
        if (trimmedNameHome.equals(trimmedNameVisitor)) {
            throw new IllegalArgumentException("Invalid team names (the same for both teams)");
        }
        
        Match newMatch = new Match(trimmedNameHome, trimmedNameVisitor);
        Integer idNewMatch = newMatch.getMatchUniqueId();
        
        // We edit the list of the matches within a critical section
        // to avoid any race conditions.
        this.mutex.acquireUninterruptibly();
        this.listMatches.put(idNewMatch, newMatch);
        this.mutex.release();
        
        return idNewMatch;
    }

    /**
     * Updates the score of an ongoing match.
     *
     * @param matchId the unique ID of the match
     * @param scoreHomeTeam the updated score for the home team
     * @param scoreVisitorTeam the updated score for the visiting team
     * @throws IllegalArgumentException if the match ID is null, invalid, or scores are negative
     */
    public void updateScore(Integer matchId, Integer scoreHomeTeam, Integer scoreVisitorTeam) {
        if (matchId == null) {
            throw new IllegalArgumentException("Trying to update a match with null ID");
        }
        if (scoreHomeTeam == null || scoreVisitorTeam == null) {
            throw new IllegalArgumentException("Trying to update a match with null scores");
        }
        if (!this.listMatches.containsKey(matchId)) {
            throw new IllegalArgumentException("Trying to update an invalid match");
        }
        if (scoreHomeTeam < 0 || scoreVisitorTeam < 0) {
            throw new IllegalArgumentException("Trying to provide a negative score");
        }
        
        // As we can potentially be in a multi-threaded environment,
        // the list of the matches (and all the matches it contains)
        // are updated within a semaphore-protected critical section
        this.mutex.acquireUninterruptibly();
        Match match = this.listMatches.get(matchId);
        match.setHomeScore(scoreHomeTeam);
        match.setVisitorScore(scoreVisitorTeam);
        this.mutex.release();
    }

    /**
     * Terminates a match and removes it from the scoreboard.
     *
     * @param matchId the unique ID of the match to be terminated
     * @throws IllegalArgumentException if the match ID is null or invalid
     */
    public void terminateMatch(Integer matchId) {
        if (matchId == null) {
            throw new IllegalArgumentException("Trying to terminate a match with null ID");
        }
        if (!this.listMatches.containsKey(matchId)) {
            throw new IllegalArgumentException("Trying to terminate a match with invalid ID");
        }
        
        // As we can potentially be in a multi-threaded environment,
        // the list of the matches (and all the matches it contains)
        // are updated within a semaphore-protected critical section
        this.mutex.acquireUninterruptibly();
        this.listMatches.remove(matchId);
        this.mutex.release();
    }

    /**
     * Retrieves a summary of all ongoing matches, sorted by total score in descending order.
     * If multiple matches have the same score, they are sorted by most recent start time.
     *
     * @return a formatted string containing match summaries, separated by new lines
     */
    public String getMatchesSummary() {
        // Taking a snapshot of the list of the matches and their results
        this.mutex.acquireUninterruptibly();
        List<Match> matches = new ArrayList<>(this.listMatches.values());
        this.mutex.release();

        // the sorting relies on the Match.compareTo method, which compares Matches
        // per total number of scored goals, and then by starting order, if they have
        // the same number of goals.
        Collections.sort(matches);
        return matches.stream().map(Match::toString).collect(Collectors.joining("\n"));
    }
}
