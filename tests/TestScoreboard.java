import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Nested;

class TestScoreboard {

    /**
    *
    * Test to check if a newly created board is properly initialized
    */
    @Nested
    class ConstructorTests {
    
        @Test
        void BoardCreated_ExpectsEmptyListMatches() {
            fail("Test not implemented yet");
        }    
    }

    /**
    *
    * Test to check if a newly created board is properly initialized
    */
    @Nested
    class StartMatchTests {
    
        @Test
        void ValidTeamNames_MatchCreatedWithNullScore() {
            fail("Test not implemented yet");
        }

        @Test
        void EmptyTeamNames_MatchNotCreatedAndExceptionThrown() {
            fail("Test not implemented yet");
        }

        @Test
        void SameTeamNames_MatchNotCreatedAndExceptionThrown() {
            fail("Test not implemented yet");
        }
    }

    @Nested
    class UpdateScoreTests {
    
        @Test
        void ValidScoreProvidedForValidMatchId_ScoreUpdated() {
            fail("Test not implemented yet");
        }

        @Test
        void InvalidMatchId_ExceptionThrown() {
            fail("Test not implemented yet");
        }

        @Test
        void NegativeScores_ExceptionThrown() {
            fail("Test not implemented yet");
        }
    }

        @Nested
    class GetMatchesSummaryTests {
    
        @Test
        void NoMatchTracked_EmptyString() {
            fail("Test not implemented yet");
        }

        @Test
        void DifferentScores_MatchesSortedPerNumberGoals() {
            fail("Test not implemented yet");
        }

        @Test
        void SameScores_MatchesSortedPerCreationDate() {
            fail("Test not implemented yet");
        }

        @Test
        void CombinationOfCases_MatchesProperlySorted() {
            fail("Test not implemented yet");
        }
    }
}
