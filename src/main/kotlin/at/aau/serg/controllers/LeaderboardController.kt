package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    @GetMapping
    fun getLeaderboard(@RequestParam(required = false) rank: Int?): List<GameResult> {

        // liest Parameter aus der URL --> @Req.Par.(required = false )
        // Wenn in der URL /leaderboard?rank=5 --> rank= 5
        // Int? --> also rank kann null sein

        // Leaerboard sortier
        val leaderboardSorted = gameResultService.getGameResults() // --> holt alle Spielergebisse mit diese mit gameresultService.getGameResults
                .sortedWith(compareBy({ -it.score }, { it.timeInSeconds }))  // --> hier wird -it.score nache Score absteigende und it.timeInsec. nach Zeit aufsteigend sortiert

        //  rank == null  angegeben → ganze Liste, welche sortiert ist -->  zurückgeben
        if (rank == null) {
            return leaderboardSorted
        }
        // Fehler prüfen --> ist rank gültig --> ok
        // Ist nicht --> HTTP 400 Bad Request
        if (rank < 1 || rank > leaderboardSorted.size) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }

        // Rang im Leaderboard startet bei 1
        // Index einer Liste startet bei 0
        // Spieler an auf der Rang 5 steht in der liste an Position 4

        val index = rank - 1 // Rang beginnt bei 1, Listenindex bei 0
        val start = maxOf(0, index - 3) // 3 Spieler davor
        val end = minOf(leaderboardSorted.size, index + 4) // 3 Spieler danach, aber nicht über das Listenende

        return leaderboardSorted.subList(start, end) // Spieler um den Rang + - 3
    }
}