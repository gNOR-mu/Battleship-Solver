package com.gnormu.battleship.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        // Colocamos un barco en el tablero
        board.putShip((byte) 0, CellContent.CARRIER);

        // Verificamos que en el tablero original sí está el barco
        assertNotEquals(CellContent.WATER, board.getCellState((byte) 0));

        // Verificamos que la vista del tablero oculta el barco (retornando agua)
        assertEquals(CellContent.WATER, boardView.getCellState((byte) 0));
    }
}
