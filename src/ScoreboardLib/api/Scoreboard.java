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
        return 0;
    }

    public void updateScore(Integer matchId, Integer scoreHomeTeam, Integer scoreVisitorTeam) {

    }

    public void terminateMatch(Integer matchId) {

    }

    public String getMatchesSummary() {
        return "";
    }

}
