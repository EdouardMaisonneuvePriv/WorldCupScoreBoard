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
}
