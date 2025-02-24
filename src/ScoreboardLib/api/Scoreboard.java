package ScoreboardLib.api;

import java.util.HashMap;
import java.util.Map;

import ScoreboardLib.internal.Match;

public class Scoreboard {

    private Map<Integer, Match> listMatches;

    public Scoreboard() {
        listMatches = new HashMap<Integer, Match>();
    }

}
