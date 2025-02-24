package ScoreboardLib.api;

import java.util.HashMap;
import java.util.Map;

import ScoreboardLib.internal.Match;

public class Scoreboard {

    private Map<Integer, Match> listMatches;

    public Scoreboard() {
        listMatches = new HashMap<Integer, Match>();
    }

    public Integer startMatch(String nameHomeTeam, String nameVisitorTeam) {

        Integer createdMatchId = 0;

        if (null != nameHomeTeam && null != nameVisitorTeam) {

            String trimmedNameHome = nameHomeTeam.trim();
            String trimmedNameVisitor = nameVisitorTeam.trim();

            if (!trimmedNameHome.isEmpty() && !trimmedNameVisitor.isEmpty()) {

                if (trimmedNameHome != trimmedNameVisitor) {
                    Match newMatch = new Match(trimmedNameHome, trimmedNameVisitor);
                    Integer idNewMatch = newMatch.getMatchUniqueId();
                    this.listMatches.put(idNewMatch,newMatch);
                    createdMatchId = idNewMatch;
                } else {
                    throw new IllegalArgumentException("Invalid team names (the same for both teams)");
                }
            } else {
                throw new IllegalArgumentException("Team names must be non empty");
            }
        } else {
            throw new IllegalArgumentException("Team names must be non null");
        }

        return createdMatchId;
    }

    public void updateScore(Integer matchId, Integer scoreHomeTeam, Integer scoreVisitorTeam) {

        if(null != matchId) {

            if (null != scoreHomeTeam && null != scoreVisitorTeam) {

                if(this.listMatches.containsKey(matchId)) {

                } else {
                    throw new IllegalArgumentException("Trying to update an invalid match");
                }
            } else {
                throw new IllegalArgumentException("Trying to update a match will null scores");
            }
        } else {
            throw new IllegalArgumentException("Trying to update a match with null ID");
        }

    }

    public void terminateMatch(Integer matchId) {

    }

    public String getMatchesSummary() {
        return "";
    }

}
