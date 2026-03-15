package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    //prüft, ob getLb nach Score absteigend sortiert sind
    fun test_getLB_correctScoreSorting() {
        val first = GameResult(1, "Maus", 30, 30.0)
        val second = GameResult(2, "Katze", 25, 15.0)
        val third = GameResult(3, "Hund", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null) // hier wird Null hinzugefügt

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLB_sameScoreANDfastestTime() {
        // id ist absichtlich nicht sortiert um es wirklich zu testen
        val fast = GameResult(1, "Maus", 30, 30.0)
        val medium = GameResult(3, "Katze", 25, 15.0)
        val slow = GameResult(2, "Hund", 10, 15.0)
        val res = controller.getLeaderboard(null)

        whenever(mockedService.getGameResults()).thenReturn(listOf(medium, slow, fast))
        assertEquals(fast, res[0])
        assertEquals(medium, res[1])
        assertEquals(slow, res[2])
    }

    @Test
    // Neuer Rank test
    fun test_getLB_Rank() {
        val list = listOf(
            GameResult(1, "A", 90, 5.0),
            GameResult(2, "M", 80, 6.0),
            GameResult(3, "N", 70, 7.0),
            GameResult(4, "Z", 60, 8.0),
            GameResult(5, "Q", 50, 9.0),
            GameResult(6, "L", 40, 10.0),
            GameResult(7, "T", 30, 11.0),
            GameResult(8, "F", 20, 12.0)
        )

        whenever(mockedService.getGameResults()).thenReturn(list)

        val res = controller.getLeaderboard(5)

        assertEquals(7, res.size)
        assertEquals("M", res[0].playerName)
        assertEquals("F", res[6].playerName)
    }

    @Test
    // rank = -1 somit ist er unglültigt weil kleiner als 1 ist
    fun test_getLB_invaildRank_throws400() {
        whenever(mockedService.getGameResults()).thenReturn(emptyList())

        assertFailsWith<ResponseStatusException> {
            controller.getLeaderboard(-1)
        }
    }

    @Test
    // rank ist hier = 0 und ist ungültigt
    fun test_getLeaderboard_rankZero_throws400() {
        whenever(mockedService.getGameResults()).thenReturn(
            listOf(GameResult(1, "sisi", 90, 5.0))
        )

        assertFailsWith<ResponseStatusException> {
            controller.getLeaderboard(0)
        }
    }

    @Test
    // hier wird getest --> rank ist größer --> also hier habt man kein 5 Plätze
    fun test_getLB_RankLarge_throws400() {
        whenever(mockedService.getGameResults()).thenReturn(
            listOf(GameResult(1, "sisi", 90, 5.0))
        )

        assertFailsWith<ResponseStatusException> {
            controller.getLeaderboard(5)
        }
    }
}

