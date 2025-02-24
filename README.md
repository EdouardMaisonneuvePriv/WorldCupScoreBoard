# WorldCupScoreBoard
Amazing (and fancy) score board library for the World Cup 2026

## Requirements analysis:

### Requirement 1. Start a new match, assuming initial score 0 – 0 and adding it the scoreboard. This should capture following parameters:a. Home team b. Away team

**Written assumptions**
+ The Scoreboard is a representation of a Match between 2 Teams. The Scoreboard allows the vizualisation of the scores of several ongoing matches.
+ The user adds a match to the scoreboard at the beginning of this mach (score 0-0).

**Deduced assumptions**
+ There's no particular rules when it comes to the team names. However, The library will only make sure that the team names are not empty. The user should not provide any names with non-aplhanumeric characters, as the behaviour of this library in such cases is not be validated.
+ The Scoreboard will verify that the matches are between 2 different teams (Teams' names must be different).
+ a match which just got added must have a null score.
+ Based on the povided examples, several matches might be ongoing at the same time. This library will not ensure that several matches
involving the same teams are not happening at the same time. Similarly, this library will not ensure that a same team will play 2 different
matches at the same time. The reponsibility of the data consistency lies on the side of the library user
+ There's no expectations as to wether this library will be used in a single-threaded or multi-threaded environment. In order to be on the safe side and anticipate users' needs, this library will be thread-safe. As the starting moment of each match is important for the way the Scoreboard is being displayed, it is important that the matches' creation is protected via mutex/semaphore (when it comes to the priority, it'll be "first thread arrived, first thread served" - FIFO).
+ Updating the score requires a way to identify each match individually (see below). As a consequence, adding a function returns an unique match identifier. It is the responsibility of the users to store the identifier of each match they created, so they can update the score later.
+ The scoreboard can contain at most 2147483647 simultaneously (the match unique ID is a 32 positive int). Of course, this number of matches is also limited by the machine's memory. If the machine doesn't have enough resources, a java.lang.OutOfMemoryError will be thrown.

### Requirement 2. Update score. This should receive a pair of absolute scores: home team score and away team score.

**Written assumptions**
+ a score update consists in a pair of absolute scores (home team score and away).

**Deduced assumptions**
+ the score update, as it is currently described, is incomplete: if there are several matches displayed on the Scoreboard, using only the new home/visitor scores as the parameters is not enough. The score update function should also take an additional parameter, allowing to identify which match is going to be updated.
+ If the users provide an invalid match ID, an exception will be thrown.
+ the score update will be a Pair<int,int>. The first int is the new score of the Home team, the second one is the new score of the Visitor team.\
+ each score will be a positive integer. If a negative integer is provided, an exception will be thrown.
+ In theory, the scores should only go up. The library will not enforce such behaviour, thus it is the responsiblity of the library's user to make sure the score updates are making sense.
+ the maximal value of the score will be 2147483647 (32 bits).
+ Similarly to the creation, as we assume that the library will be used in a multi-threaded environment, the score update should be done in within a mutex/semaphore critical section.

### Requirement 3. Finish match currently in progress. This removes a match from the scoreboard.

**Written assumptions**
+ Finishing a match removes it from the Scoreboard.

**Deduced assumptions**
+ Same as for the score update, the match termination requires a valid match ID.
+ Trying to update the score of a not-ongoing match (with the ID of a finished match, or invalid match ID) will result in an exception being thrown.
+ Similarly to the creation & scores updates, as we assume that the library will be used in a multi-threaded environment, the match termination should be done in within a mutex/semaphore critical section.

### Requirement 4. Get a summary of matches in progress ordered by their total score. The matches with the same total score will be returned ordered by the most recently started match in the scoreboard.

**Written assumptions**
+ the matches should be sorted by descending cumulated scores, and for matches having the same cumulated score, the match created first will be placed lower
+ The format of each match is as follows: nameHomeTeam scoreHomeTeam - nameVisitorTeam scoreVisitorTeam
+ the format of the whole summary is a follows match1Summary**\n**match2Summary**\n**...**\n**matchNSummary

**Deduced assumptions**
+ The summary will be returned as a string.
+ If there's no ongoing match on the scoreboard, the returned string will be empty.

## Project documentation:

### Requirements

- OpenJDK 23 or newer
- JUnit 5 (JUnit Platform Console Standalone JAR for running unit tests)
