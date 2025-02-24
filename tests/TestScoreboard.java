import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.lang.reflect.Field;
import java.util.Map;

import ScoreboardLib.api.Scoreboard;
import ScoreboardLib.internal.Match;

class TestScoreboard {

    static public Map<Integer,Match> helperGetListMatchesForBoard(Scoreboard board) {

        Map<Integer,Match> retrievedMatches = null;

        try {
            // Access private fields via reflection for testing purposes
            Field matches = Scoreboard.class.getDeclaredField("listMatches");
            matches.setAccessible(true);
            retrievedMatches = (Map<Integer,Match>) matches.get(board);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return retrievedMatches;
    }

    /**
    *
    * Test to check if a newly created board is properly initialized
    */
    @Nested
    class ConstructorTests {
    
        @Test
        void BoardCreated_ExpectsEmptyListMatches() {

            Scoreboard board = new Scoreboard();

            Map<Integer,Match> retrievedMatches;

            assertNotNull(board, "The Scoreboard object should not be null");
			retrievedMatches = TestScoreboard.helperGetListMatchesForBoard(board);

            assertTrue(retrievedMatches.keySet().isEmpty(),
                "The newly created Scoreboard contains some entries while it should not");
        }    
    }

    /*
    *
    * Test to check if a newly created board is properly initialized
    */
    @Nested
    class StartMatchTests {
    
        @Test
        void ValidTeamNames_MatchCreatedWithNullScore() {

            String nameTeam1 = "France";
            String nameTeam2 = "Argentina";
            String nameTeam3 = "Spain";
            String nameTeam4 = "England";

            Map<Integer,Match> listMatches;
            Integer match1Id;
            Integer match2Id;

            Scoreboard board = new Scoreboard();

            match1Id = board.startMatch(nameTeam1, nameTeam2);

            listMatches = TestScoreboard.helperGetListMatchesForBoard(board);
            assertTrue(1 == listMatches.size(),"Match1 was not successfully added");

            match2Id = board.startMatch(nameTeam3, nameTeam4);

            assertTrue(2 == listMatches.size(),"Match2 was not successfully added");
            assertTrue(match1Id != match2Id,"Match1 and Match2 have the same unique Id");
        }

        @Test
        void NullTeamNames_MatchNotCreatedAndExceptionThrown() {
            
            Scoreboard board = new Scoreboard();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
				board.startMatch(null,null);
	        });
	        assertEquals("Team names must be non null", exception.getMessage());
        }

        @Test
        void EmptyTeamNames_MatchNotCreatedAndExceptionThrown() {
            Scoreboard board = new Scoreboard();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
				board.startMatch("","     ");
	        });
	        assertEquals("Team names must be non empty", exception.getMessage());
        }

        @Test
        void SameTeamNames_MatchNotCreatedAndExceptionThrown() {
            Scoreboard board = new Scoreboard();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
				board.startMatch("France","France");
	        });
	        assertEquals("Invalid team names (the same for both teams)", exception.getMessage());
        }
    }

    @Nested
    class UpdateScoreTests {
    
        @Test
        @Disabled("not testable here, will be tested while generating matches summary")
        void ValidScoreProvidedForValidMatchId_ScoreUpdated() {
            
        }

        @Test
        void NullMatchId_ExceptionThrown() {
            Scoreboard board = new Scoreboard();

            Integer matchId = board.startMatch("France", "Mexico");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
				board.updateScore(null, 1, 2);
	        });
	        assertEquals("Trying to update a match with null ID", exception.getMessage());
        }

        @Test
        void NullScores_ExceptionThrown() {
            Scoreboard board = new Scoreboard();

            Integer matchId = board.startMatch("France", "Mexico");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
				board.updateScore(matchId, null, null);
	        });
	        assertEquals("Trying to update a match will null scores", exception.getMessage());
        }

        @Test
        void InvalidMatchId_ExceptionThrown() {
            Scoreboard board = new Scoreboard();

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
				board.updateScore(45,2,2);
	        });
	        assertEquals("Trying to update an invalid match", exception.getMessage());
        }

        @Test
        void NegativeScores_ExceptionThrown() {
            Scoreboard board = new Scoreboard();

            Integer matchId = board.startMatch("France", "Argentina");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
				board.updateScore(matchId,-2,-2);;
	        });
	        assertEquals("Trying to provide a negative score", exception.getMessage());
        }
    }

    @Nested
    class TerminateMatchTests {

        @Test
        void TerminateMatch_MatchTerminated() {

        }

                
        @Test
        void TryToTerminateMatchAlreadyTerminated_ExceptionThrown() {

            Scoreboard board = new Scoreboard();
            Integer matchId = board.startMatch("France", "Portugal");

            // Should pass
            board.terminateMatch(matchId);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
				board.terminateMatch(matchId);
	        });
	        assertEquals("Error: trying to provide a negative score", exception.getMessage());
        }


        void TryToTerminateInvalidMatch_ExceptionThrown() {

            Scoreboard board = new Scoreboard();

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
				board.terminateMatch(10);
	        });
	        assertEquals("Error: trying to provide a negative score", exception.getMessage());
        }
    }

    @Nested
    class GetMatchesSummaryTests {
    
        @Test
        void NoMatchTracked_EmptyString() {
            Scoreboard board = new Scoreboard();
            String summary = board.getMatchesSummary();

            assertTrue(0 == summary.compareTo(""),
                "The summary is supposed to empty when no match is being tracked");
        }

        @Test
        void DifferentScores_MatchesSortedPerNumberGoals() {
            String name1 = "England";
            String name2 = "Spain";
            String name3 = "France";
            String name4 = "Argentina";

            Integer scoreFrance = 4;
            Integer scoreArgentina = 2;
            Integer scoreEngland = 1;
            Integer scoreSpain = 2;

            Integer EngEspMatchId;
            Integer FraArgMatchId;

            String matchesSummary;
            
            String expectedResult = name3 + " " + scoreFrance.toString() + " - " +
                    name4 + " " + scoreArgentina.toString() + "\n" + 
                    name1 + " " + scoreEngland.toString() + " - " + 
                    name2 + " " + scoreSpain.toString();

            Scoreboard board = new Scoreboard();
            EngEspMatchId = board.startMatch(name1, name2);
            FraArgMatchId = board.startMatch(name3, name4);

            board.updateScore(EngEspMatchId, scoreEngland, scoreSpain);
            board.updateScore(FraArgMatchId, scoreFrance, scoreArgentina);

            matchesSummary = board.getMatchesSummary();

            assertTrue(0 == matchesSummary.compareTo(expectedResult), "Incorrect matches summary");
        }

        @Test
        void SameScores_MatchesSortedPerCreationDate() {
            String name1 = "England";
            String name2 = "Spain";
            String name3 = "France";
            String name4 = "Argentina";

            Integer scoreFrance = 1;
            Integer scoreArgentina = 2;
            Integer scoreEngland = 1;
            Integer scoreSpain = 2;

            Integer EngEspMatchId;
            Integer FraArgMatchId;

            String matchesSummary;
            
            String expectedResult = name1 + " " + scoreEngland.toString() + " - " +
                    name2 + " " + scoreSpain.toString() + "\n" + 
                    name3 + " " + scoreFrance.toString() + " - " + 
                    name4 + " " + scoreArgentina.toString();

            Scoreboard board = new Scoreboard();
            EngEspMatchId = board.startMatch(name1, name2);
            FraArgMatchId = board.startMatch(name3, name4);

            board.updateScore(EngEspMatchId, scoreEngland, scoreSpain);
            board.updateScore(FraArgMatchId, scoreFrance, scoreArgentina);

            matchesSummary = board.getMatchesSummary();

            assertTrue(0 == matchesSummary.compareTo(expectedResult), "Incorrect matches summary");
        }

        @Test
        void CombinationOfCases_MatchesProperlySorted() {
            String england = "England";
            String spain = "Spain";
            String france = "France";
            String argentina = "Argentina";
            String germany = "Germany";
            String brazil = "Brazil";

            Integer scoreFrance = 1;
            Integer scoreArgentina = 2;
            Integer scoreEngland = 1;
            Integer scoreSpain = 2;
            Integer scoreGermany = 5;
            Integer scoreBrazil = 2;

            Integer EngEspMatchId;
            Integer FraArgMatchId;
            Integer GerBraMatchId;

            String matchesSummary;
            
            String expectedResult = germany + " " + scoreGermany.toString() + " - " +
                    brazil + " " + scoreBrazil.toString() + "\n" + 
                    england + " " + scoreEngland.toString() + " - " +
                    spain + " " + scoreSpain.toString() + "\n" + 
                    france + " " + scoreFrance.toString() + " - " + 
                    argentina + " " + scoreArgentina.toString();

            Scoreboard board = new Scoreboard();
            EngEspMatchId = board.startMatch(england, spain);
            FraArgMatchId = board.startMatch(france, argentina);
            GerBraMatchId = board.startMatch(germany, brazil);

            board.updateScore(EngEspMatchId, scoreEngland, scoreSpain);
            board.updateScore(FraArgMatchId, scoreFrance, scoreArgentina);
            board.updateScore(GerBraMatchId, scoreGermany, scoreBrazil);

            matchesSummary = board.getMatchesSummary();

            assertTrue(0 == matchesSummary.compareTo(expectedResult), "Incorrect matches summary");
        }
    }
}
