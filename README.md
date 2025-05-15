## Live Football World Cup Scoreboard

This is my implementation of the test task. It's a simple library that allows adding new matches, updating
scores for them, finishing and retrieving the matches sorted by their score descending primarily, recency secondarily.

It is built with ```Java 17``` and uses ```Maven``` as a build system.

In my solution, I have assumed the following:

- When starting a new match, valid team names must be provided, meaning null or blank names are not allowed.
- Negative scores are not allowed.
- Same matches (with the same teams) for the same scoreboard are not allowed.
- Updating the score for non-existing match is not allowed (will throw an exception).
- Finishing non-existing match is not allowed.
- The ```Scoreboard``` is not thread-safe.
- A property of type ```MatchStatus``` is used to keep track of in-progress and finished matches.
- Retrieving matches will retrieve only in-progress matches.
- Once a match is finished, it's no longer possible to modify it's score.
- Finishing an already finished match is not allowed.
- An object of type ```Match``` is effectively immutable. It does not directly expose behaviour to modify its score,
but rather a method ```updateScore``` that performs checks for score values and match ```status```.
- A ```startOrder``` property is a simple unique and final counter for each match in the scoreboard to
determine the recency of the match.

### Build the library
- mvn clean install

