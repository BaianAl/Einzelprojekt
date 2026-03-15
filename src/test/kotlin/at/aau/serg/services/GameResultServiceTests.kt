package at.aau.serg.services

import at.aau.serg.models.GameResult
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GameResultServiceTests {

    private lateinit var service: GameResultService

    @BeforeEach
    fun setup() {
        service = GameResultService()
    }

    @Test
    fun test_getGameResults_emptyList() {
        val result = service.getGameResults()

        assertEquals(emptyList<GameResult>(), result)
    }

    @Test
    fun test_addGameResult_getGameResults_containsSingleElement() {
        val gameResult = GameResult(1, "player1", 17, 15.3)

        service.addGameResult(gameResult)
        val res = service.getGameResults()

        assertEquals(1, res.size)
        assertEquals(gameResult, res[0])
    }

    @Test
    fun test_getGameResultById_existingId_returnsObject() {
        val gameResult = GameResult(1, "player1", 17, 15.3)
        service.addGameResult(gameResult)

        val res = service.getGameResult(1)

        assertEquals(gameResult, res)
    }

    @Test
    fun test_getGameResultById_nonexistentId_returnsNull() {
        val gameResult = GameResult(1, "player1", 17, 15.3)
        service.addGameResult(gameResult)

        val res = service.getGameResult(22)

        assertNull(res)
    }

    @Test
    fun test_addGameResult_multipleEntries_correctId() {
        val gameResult1 = GameResult(0, "player1", 17, 15.3)
        val gameResult2 = GameResult(0, "player2", 25, 16.0)

        service.addGameResult(gameResult1)
        service.addGameResult(gameResult2)

        val res = service.getGameResults()

        assertEquals(2, res.size)

        assertEquals(gameResult1, res[0])
        assertEquals(1, res[0].id)

        assertEquals(gameResult2, res[1])
        assertEquals(2, res[1].id)
    }
    @Test
    //@Test sagt schon dass es nur ein Test ist
    // Es wird hier ein Player erstellt  mit Id , name , score und Zeit
    // dann in die Liste einfügen
    // dann wird der Player anhand seiner Id gelöscht --> schau Zeile 83
    // Zeile 86 --> hier wird geprüft, wen n man nach einem Player suchen soll jetzt null zurückkommen --> weil er gelöscht wurde : )
    fun test_deleteGameResult_existingId_removesEntry() {
        val gameResult = GameResult(0, "player1", 50, 55.5)
        service.addGameResult(gameResult)

        service.deleteGameResult(gameResult.id)

        assertNull(service.getGameResult(gameResult.id))
    }

    @Test
    // Also hier wird getestet--> wenn man eine Id löschen möchte, welche gar nicht exsistiert
    //wieder ein Spiel wird erstellt und zu Liste hinzugefügt
    // Die List hat Eintrag 1
    fun test_deleteGameResult_nonexistentId() {
        val gameResult = GameResult(0, "player1", 50, 55.5)
        service.addGameResult(gameResult)
        // So hier versucht man diese Id zu Löschen, die gibt aber gar nicht
        // diese L ist für Long
        service.deleteGameResult(99L)
        // Die Lsit hat weiterhin 1 Eintrag --> also das löschen einer nicht exsitierten ID nothing kaputt gemacht
        assertEquals(1, service.getGameResults().size)
    }
}

}