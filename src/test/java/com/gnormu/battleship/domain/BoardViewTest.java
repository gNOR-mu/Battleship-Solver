package com.gnormu.battleship.domain;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gnormu.battleship.config.GameConfig;

public class BoardViewTest {

    private BoardView boardView;

    private Board2d board;

    @BeforeEach
    void setup() {
        board = new Board2d();
        boardView = new BoardView(board);
    }

    @Test
    @DisplayName("Ocultamiento datos: No se deben mostrar los barcos")
    void getCellState_shouldHideShips() {
        for (int i = 0; i < GameConfig.BOARD_DIMENSION; i++) {
            for (int j = 0; j < GameConfig.BOARD_DIMENSION; j++) {
                Coordinate coordinate = Coordinate.of(i, j);
                assertNotEquals(CellState.SHIP, boardView.getCellState(coordinate));
            }
        }
    }
}
