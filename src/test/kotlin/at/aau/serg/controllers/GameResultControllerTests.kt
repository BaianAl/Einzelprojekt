package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import kotlin.test.assertEquals
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when` as whenever
import kotlin.test.Test

class GameResultControllerTests {

    // Warum habe ich diese Klasse erstellt ?
    // um einen 100 % Coverage in GEsamtResultController

    private lateinit var mockedService: GameResultService   // lateinit = diese Variable wird später befüllt nicht sofort
    private lateinit var controller: GameResultController

    @BeforeEach // Vor jedem Test wird ein new Mock erstellt --> damit jeder Test sauber startet
    fun setup() {
        mockedService = mock() // Der Controller bekommt den Mock-Service --> Somit wird nur den Controller getest und nicht den echten Service
        controller = GameResultController(mockedService)
    }

    @Test // hier wird geprüft, ob Controller das Result von Service richtig zurückgibt ????
    fun test_getGR_returnsObject() {
        val result = GameResult(1, "player", 50, 5.0)
        whenever(mockedService.getGameResult(1)).thenReturn(result) //--> bei Aufrufen von GameResult soll result also Zeile 28 zurückgeben
        val res = controller.getGameResult(1) // hier wird diese Methode des Controlles aufgrufen
        verify(mockedService).getGameResult(1)
        assertEquals(result, res) // prüft ob das Controller das richtige zurückgibt
    }





    @Test
    //Also hier wird getestet --> Fehlerfall, wenn ein Ergebnis nicht exsistiert
    // Warum ? damit der Controller korrekt mit nicht vorhandenen Id umgeht
    fun test_getGR_returnsNull() {
        whenever(mockedService.getGameResult(99)).thenReturn(null)
        val res = controller.getGameResult(99)
        verify(mockedService).getGameResult(99)
        assertEquals(null, res)
    }

    @Test
    // prüft ob der Cont. alle GameRsults korrekt zurückgibt
    // Also die List von Player richtig zurcükgibt
    fun test_getAllGR_returnsList() {
        val list = listOf(GameResult(1, "player", 10, 5.0))
        whenever(mockedService.getGameResults()).thenReturn(list)

        val res = controller.getAllGameResults()

        verify(mockedService).getGameResults()
        assertEquals(list, res)
    }

    @Test
    // hier wird geprüft, dass er Controller das Hinzugefügen weiterleitet an Service
    fun test_addGR_callsService() {

        val result = GameResult(0, "player", 10, 5.0)
        controller.addGameResult(result)
        verify(mockedService).addGameResult(result)
    }

    @Test
    // prüft, dass der Controller das Löschen eines Ergebnisses weriterleitet.
    fun test_deleteGR_callsService() {
        controller.deleteGameResult(1)
        verify(mockedService).deleteGameResult(1)
    }
}