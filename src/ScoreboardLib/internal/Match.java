package ScoreboardLib.internal;

public class Match implements Comparable<Match> {

    private static int matchCounter = 1;
    private final Integer matchId;
    private final String nameHomeTeam;
    private final String nameVisitorTeam;
    private Integer scoreHomeTeam;
    private Integer scoreVisitorTeam;

    public Match(String homeTeamName, String visitorTeamName) {

        this.matchId = Match.matchCounter;
        this.nameHomeTeam = homeTeamName;
        this.nameVisitorTeam = visitorTeamName;
        this.scoreHomeTeam = 0;
        this.scoreVisitorTeam = 0;

        Match.matchCounter++;
    }

    public Integer getMatchUniqueId() {
        return this.matchId;
    }

    private Integer getTotalNumberGoals() {
        return this.scoreVisitorTeam + this.scoreVisitorTeam;
    }

    @Override
    public String toString() {
        return this.nameHomeTeam + " " + this.scoreHomeTeam.toString() + " - " +
                this.nameVisitorTeam + " " + this.scoreVisitorTeam;
    }

    @Override
    public int compareTo(Match arg0) {

        int result = 0;
        Integer ownNumberGoals = this.getTotalNumberGoals();
        Integer otherMatchNumberGoals = arg0.getTotalNumberGoals();
        
        if (ownNumberGoals != otherMatchNumberGoals) {
            result = ownNumberGoals - otherMatchNumberGoals;
        } else {
            result = this.matchId - arg0.matchId;
        }
        return result;
    }
}
