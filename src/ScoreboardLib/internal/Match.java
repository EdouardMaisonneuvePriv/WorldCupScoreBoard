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

    public void setHomeScore(Integer homeScore) {
        this.scoreHomeTeam = homeScore;
    }

    public void setVisitorScore(Integer visitorScore) {
        this.scoreVisitorTeam = visitorScore;
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

        System.out.println("Comparing: " + this + " vs " + arg0);

        // Sort by total goals (descending)
        int goalComparison = Integer.compare(arg0.getTotalNumberGoals(), this.getTotalNumberGoals());
        
        if (goalComparison != 0) {
            System.out.println("using goals to compare");
            return goalComparison; // Sort by highest goals first
        }

        // Sort by match ID (descending) to prioritize the most recently started match
        System.out.println("using matchId to compare");
        result = Integer.compare(arg0.matchId, this.matchId);
        System.out.println(result + " -> " + this + " is smaller than " + arg0);
        return result;
    }

}
