the current working algo is on part 2 simplified
to do:
implement corral deadlock checking

things done:
implement freeze deadlock checking
internal representation of game states
a* algorithm
simple deadlock checking (precalculated)
verifying moves

bugs:
deadlock checking whenever the goal is on the edge
(luckily i don't think this will come up as all cases provided have walls surrounding the edge)

key problems:
it's bad
it can't solve any of the set problems (takes too long)
note: i know that it works cuz i made incredibly easy testlevels and it was able to solve them, but if u need to move more than a couple blocks (8-9ish moves), the algo just fails
