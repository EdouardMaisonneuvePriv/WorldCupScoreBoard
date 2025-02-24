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

/**
 * Unit tests for the Scoreboard class in the ScoreboardLib package.
 * <p>
 * This class contains nested test classes for testing the various functionalities of the Scoreboard class, 
 * including match creation, score updating, match termination, and match summary retrieval.
 * It uses JUnit 5 for testing and includes assertions to validate the behavior of the Scoreboard class methods.
 */
class TestScoreboard {

    /**
     * Helper method to retrieve the private list of matches from a Scoreboard object.
     * <p>
     * This method uses reflection to access the private "listMatches" field of the Scoreboard class.
     * It is used for testing purposes to validate the state of the list of matches.
     *
     * @param board The Scoreboard object to retrieve the matches list from.
     * @return A map containing the matches associated with their unique IDs.
     */
    static public Map<Integer, Match> helperGetListMatchesForBoard(Scoreboard board) {
        Map<Integer, Match> retrievedMatches = null;
        try {
            // Access private fields via reflection for testing purposes
            Field matches = Scoreboard.class.getDeclaredField("listMatches");
            matches.setAccessible(true);
            retrievedMatches = (Map<Integer, Match>) matches.get(board);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return retrievedMatches;
    }

    /**
     * Test class to verify the constructor behavior of the Scoreboard.
     */
    @Nested
    class ConstructorTests {

        /**
         * Test to check if a newly created Scoreboard is properly initialized with an empty list of matches.
         */
        @Test
        void BoardCreated_ExpectsEmptyListMatches() {
            Scoreboard board = new Scoreboard();
            Map<Integer, Match> retrievedMatches;
            
            // Making sure that the object is created
            assertNotNull(board, "The Scoreboard object should not be null");
            retrievedMatches = TestScoreboard.helperGetListMatchesForBoard(board);

            // Making sure that it doesn't contain any match
            assertTrue(retrievedMatches.keySet().isEmpty(),
                "The newly created Scoreboard contains some entries while it should not");
        }
    }

    /**
     * Test class to verify the behavior of starting a match on the Scoreboard.
     */
    @Nested
    class StartMatchTests {

        /**
         * Test to check if two valid teams can start a match with null scores.
         */
        @Test
        void ValidTeamNames_MatchCreatedWithNullScore() {
            String nameTeam1 = "France";
            String nameTeam2 = "Argentina";
            String nameTeam3 = "Spain";
            String nameTeam4 = "England";

            Map<Integer, Match> listMatches;
            Integer match1Id;
            Integer match2Id;

            Scoreboard board = new Scoreboard();

            match1Id = board.startMatch(nameTeam1, nameTeam2);
            listMatches = TestScoreboard.helperGetListMatchesForBoard(board);
            // Making sure that the match has been added to the Scoreboard
            assertTrue(1 == listMatches.size(), "Match1 was not successfully added");

            match2Id = board.startMatch(nameTeam3, nameTeam4);

            // Making sure that the second match has been added to the Scoreboard
            assertTrue(2 == listMatches.size(), "Match2 was not successfully added");
            // Making sure that the 2 matches have different unique MatchId
            assertTrue(match1Id != match2Id, "Match1 and Match2 have the same unique Id");
            // We do not check that the data of the matches have not been modified during
            // the process, as it would require to violate severely the encapsulation
            // of the Match class.
        }

        /**
         * Test to check that starting a match with null team names throws an IllegalArgumentException.
         */
        @Test
        void NullTeamNames_MatchNotCreatedAndExceptionThrown() {
            Scoreboard board = new Scoreboard();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                board.startMatch(null, null);
            });
            assertEquals("Team names must be non-null", exception.getMessage());
        }

        /**
         * Test to check that starting a match with empty team names throws an IllegalArgumentException.
         */
        @Test
        void EmptyTeamNames_MatchNotCreatedAndExceptionThrown() {
            Scoreboard board = new Scoreboard();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                board.startMatch("", "     ");
            });
            assertEquals("Team names must be non-empty", exception.getMessage());
        }

        /**
         * Test to check that starting a match with the same team names throws an IllegalArgumentException.
         */
        @Test
        void SameTeamNames_MatchNotCreatedAndExceptionThrown() {
            Scoreboard board = new Scoreboard();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                board.startMatch("France", "France");
            });
            assertEquals("Invalid team names (the same for both teams)", exception.getMessage());
        }
    }

    /**
     * Test class to verify the behavior of updating scores for a match.
     */
    @Nested
    class UpdateScoreTests {

        /**
         * Test that the score is updated correctly when valid scores are provided for a valid match ID.
         * <p>
         * This test is disabled because it is difficult to test in isolation. It will be covered when generating
         * a match summary.
         */
        @Test
        @Disabled("not testable here, will be tested while generating matches summary")
        void ValidScoreProvidedForValidMatchId_ScoreUpdated() {
        }

        /**
         * Test to check that updating the score with a null match ID throws an IllegalArgumentException.
         */
        @Test
        void NullMatchId_ExceptionThrown() {
            Scoreboard board = new Scoreboard();
            Integer matchId = board.startMatch("France", "Mexico");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                board.updateScore(null, 1, 2);
            });
            assertEquals("Trying to update a match with null ID", exception.getMessage());
        }

        /**
         * Test to check that updating the score with null scores throws an IllegalArgumentException.
         */
        @Test
        void NullScores_ExceptionThrown() {
            Scoreboard board = new Scoreboard();
            Integer matchId = board.startMatch("France", "Mexico");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                board.updateScore(matchId, null, null);
            });
            assertEquals("Trying to update a match with null scores", exception.getMessage());
        }

        /**
         * Test to check that updating the score with an invalid match ID throws an IllegalArgumentException.
         */
        @Test
        void InvalidMatchId_ExceptionThrown() {
            Scoreboard board = new Scoreboard();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                board.updateScore(45, 2, 2);
            });
            assertEquals("Trying to update an invalid match", exception.getMessage());
        }

        /**
         * Test to check that updating the score with negative scores throws an IllegalArgumentException.
         */
        @Test
        void NegativeScores_ExceptionThrown() {
            Scoreboard board = new Scoreboard();
            Integer matchId = board.startMatch("France", "Argentina");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                board.updateScore(matchId, -2, -2);
            });
            assertEquals("Trying to provide a negative score", exception.getMessage());
        }
    }

    /**
     * Test class to verify the behavior of terminating a match.
     */
    @Nested
    class TerminateMatchTests {

        /**
         * Test to check that a match can be terminated correctly.
         */
        @Test
        void TerminateMatch_MatchTerminated() {

            Scoreboard board = new Scoreboard();
            Integer matchId = board.startMatch("Croatia","Nigeria");
            Map<Integer, Match> retrievedMatches;

            board.terminateMatch(matchId);

            retrievedMatches = TestScoreboard.helperGetListMatchesForBoard(board);

            // Making sure that it doesn't contain any match
            assertTrue(retrievedMatches.keySet().isEmpty(),
                "The Scoreboard contains some entries while it should not");
        }

        /**
         * Test to check that trying to terminate a match that is already terminated throws an IllegalArgumentException.
         */
        @Test
        void TryToTerminateMatchAlreadyTerminated_ExceptionThrown() {
            Scoreboard board = new Scoreboard();
            Integer matchId = board.startMatch("France", "Portugal");

            // Should pass
            board.terminateMatch(matchId);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                board.terminateMatch(matchId);
            });
            assertEquals("Trying to terminate a match with invalid ID", exception.getMessage());
        }

        /**
         * Test to check that trying to terminate an invalid match throws an IllegalArgumentException.
         */
        @Test
        void TryToTerminateInvalidMatch_ExceptionThrown() {
            Scoreboard board = new Scoreboard();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                board.terminateMatch(10);
            });
            assertEquals("Trying to terminate a match with invalid ID", exception.getMessage());
        }

        /**
         * Test to check that trying to terminate a match with a null ID throws an IllegalArgumentException.
         */
        @Test
        void TryToTerminateMatchWithNullID_ExceptionThrown() {
            Scoreboard board = new Scoreboard();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                board.terminateMatch(null);
            });
            assertEquals("Trying to terminate a match with null ID", exception.getMessage());
        }
    }

    /**
     * Test class to verify the behavior of generating match summaries.
     */
    @Nested
    class GetMatchesSummaryTests {

        /**
         * Test to check that the match summary is empty when no match is being tracked.
         */
        @Test
        void NoMatchTracked_EmptyString() {
            Scoreboard board = new Scoreboard();
            String summary = board.getMatchesSummary();
            assertTrue(0 == summary.compareTo(""),
                "The summary is supposed to be empty when no match is being tracked");
        }

        /**
         * Test to check that matches are sorted by the total number of goals when generating a summary.
         */
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
            String expectedResult = name3 + " " + scoreFrance + " - " + name4 + " " + scoreArgentina + "\n" +
                    name1 + " " + scoreEngland + " - " + name2 + " " + scoreSpain;

            Scoreboard board = new Scoreboard();
            EngEspMatchId = board.startMatch(name1, name2);
            FraArgMatchId = board.startMatch(name3, name4);

            board.updateScore(EngEspMatchId, scoreEngland, scoreSpain);
            board.updateScore(FraArgMatchId, scoreFrance, scoreArgentina);

            matchesSummary = board.getMatchesSummary();

            assertTrue(matchesSummary.equals(expectedResult), "Incorrect matches summary");
        }

        /**
         * Test to check that matches with the same scores are sorted by their creation date in the summary.
         */
        @Test
        void SameScores_MatchesSortedPerCreationDate() {
            String england = "England";
            String spain = "Spain";
            String france = "France";
            String argentina = "Argentina";
            Integer scoreEngland = 1;
            Integer scoreSpain = 2;
            Integer scoreFrance = 1;
            Integer scoreArgentina = 2;

            Integer EngEspMatchId;
            Integer FraArgMatchId;

            String matchesSummary;
            String expectedResult = france + " " + scoreFrance + " - " + argentina + " " + scoreArgentina + "\n" +
                    england + " " + scoreEngland + " - " + spain + " " + scoreSpain;

            Scoreboard board = new Scoreboard();
            EngEspMatchId = board.startMatch(england, spain);
            FraArgMatchId = board.startMatch(france, argentina);

            board.updateScore(EngEspMatchId, scoreEngland, scoreSpain);
            board.updateScore(FraArgMatchId, scoreFrance, scoreArgentina);

            matchesSummary = board.getMatchesSummary();

            assertTrue(matchesSummary.equals(expectedResult), "Incorrect matches summary");
        }

        /**
         * Test to check the combination of multiple cases for sorting matches in the summary.
         */
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
            String expectedResult = germany + " " + scoreGermany + " - " + brazil + " " + scoreBrazil + "\n" +
                    france + " " + scoreFrance + " - " + argentina + " " + scoreArgentina + "\n" +
                    england + " " + scoreEngland + " - " + spain + " " + scoreSpain;

            Scoreboard board = new Scoreboard();
            EngEspMatchId = board.startMatch(england, spain);
            FraArgMatchId = board.startMatch(france, argentina);
            GerBraMatchId = board.startMatch(germany, brazil);

            board.updateScore(EngEspMatchId, scoreEngland, scoreSpain);
            board.updateScore(FraArgMatchId, scoreFrance, scoreArgentina);
            board.updateScore(GerBraMatchId, scoreGermany, scoreBrazil);

            matchesSummary = board.getMatchesSummary();

            assertTrue(matchesSummary.equals(expectedResult), "Incorrect matches summary");
        }
    }
}
