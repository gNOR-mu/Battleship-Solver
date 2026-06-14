package com.gnormu.battleship.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gnormu.battleship.config.GameConfig;

public class BoardTest {

    private Board2d board;

    private FleetPlacer placer;

    @BeforeEach
    void setup() {
        placer = new RandomFleetPlacer();
        board = new Board2d();

    }

    @Test
    @DisplayName("Nuevo tablero: Debe inicializar correctamente el tablero")
    void createNewBoard_shouldInitializeBoardCorrectly() {
        // arrange
        int totalShipLength = ShipType.VALUES.stream()
                .mapToInt(ShipType::getLength)
                .sum();
        var grid = board.getGrid();
        int shipCellsCount = 0;
        placer.placeShips(board);

        // act & assert
        assertNotEquals(0, board.getShipsGrid().size());
        assertEquals(GameConfig.BOARD_DIMENSION, grid.length);
        assertEquals(GameConfig.BOARD_DIMENSION, grid[0].length);
        assertFalse(board.getShipsGrid().isEmpty());

        for (int i = 0; i < GameConfig.BOARD_DIMENSION; i++) {
            for (int j = 0; j < GameConfig.BOARD_DIMENSION; j++) {
                if (grid[i][j] == CellState.SHIP) {
                    shipCellsCount++;
                }
            }
        }

        assertEquals(totalShipLength, shipCellsCount);
    }

    @Test
    @DisplayName("Limpiar tablero: Debe restablecer correctamente el tablero")
    void clearBoard_shouldResetBoardCorrectly() {
        // arrange
        int totalShipLength = ShipType.VALUES.stream()
                .mapToInt(ShipType::getLength)
                .sum();
        var grid = board.getGrid();
        int shipCellsCount = 0;

        // act & assert
        for (int i = 0; i < GameConfig.BOARD_DIMENSION; i++) {
            for (int j = 0; j < GameConfig.BOARD_DIMENSION; j++) {
                assertEquals(CellState.WATER, grid[i][j]);
            }
        }

        board.shoot(Coordinate.of(0, 0));

        assertTrue(grid[0][0] == CellState.MISS);

        board.clear();
        placer.placeShips(board);

        assertNotEquals(0, board.getShipsGrid().size());
        assertEquals(GameConfig.BOARD_DIMENSION, board.getGrid().length);
        assertEquals(GameConfig.BOARD_DIMENSION, board.getGrid()[0].length);
        assertFalse(board.getShipsGrid().isEmpty());
        assertTrue(grid[0][0] == CellState.WATER || grid[0][0] == CellState.SHIP);

        for (int i = 0; i < GameConfig.BOARD_DIMENSION; i++) {
            for (int j = 0; j < GameConfig.BOARD_DIMENSION; j++) {
                if (grid[i][j] == CellState.SHIP) {
                    shipCellsCount++;
                }
            }
        }

        assertEquals(totalShipLength, shipCellsCount);
    }

    @Test
    @DisplayName("Disparo inválido: Debe lanzar excepción")
    void invalidShoot_shouldThrowException() {
        // arrange
        Coordinate invalCoordinate = Coordinate.of(-10, -10000);

        // act
        Exception ex = assertThrows(IllegalArgumentException.class, () -> board.shoot(invalCoordinate));

        assertEquals("Disparo fuera de los límites: -10,-10000", ex.getMessage());
    }

    @Test
    @DisplayName("Disparo válido: Debe alterar el estado correctamente")
    void validShoot_shouldUpdateCellStateCorrectly() {
        // arrange
        Coordinate coord = Coordinate.of(0, 0);
        CellState initialState = board.getGrid()[coord.row()][coord.column()];

        // act
        board.shoot(coord);

        // assert
        assertNotEquals(initialState, board.getGrid()[coord.row()][coord.column()]);
    }

    @Test
    @DisplayName("Game Over: Debe finalizar cuando no quedan barcos en el tablero")
    void gameOver_shouldFinishWhenNoShipsLeft() {
        // arrange
        placer.placeShips(board);

        Map<Coordinate, ShipType> ship = board.getShipsGrid();
        Coordinate firstCoord = ship.keySet().stream().findFirst().orElse(null);

        // act & asser
        // no puede ser juego terminado prematuro
        assertFalse(board.isGameOver());

        board.shoot(Coordinate.of(0, 0));

        board.shoot(firstCoord);

        assertFalse(board.isGameOver());

        for (Coordinate coordinate : ship.keySet()) {
            board.shoot(coordinate);
        }

        assertTrue(board.isGameOver());
    }
}
