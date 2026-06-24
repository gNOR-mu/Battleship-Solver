package com.gnormu.battleship.strategy;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gnormu.battleship.config.GameConfig;
import com.gnormu.battleship.domain.Board2D;
import com.gnormu.battleship.domain.BoardView;
import com.gnormu.battleship.domain.CellContent;

public class HuntTargetStrategyTest {

    private Board2D board;
    private BoardView boardView;
    private HuntTargetStrategy strategy;

    @BeforeEach
    void setup() {
        board = new Board2D();
        boardView = new BoardView(board);
        strategy = new HuntTargetStrategy();
    }

    @Test
    @DisplayName("HuntTargetStrategy: Debe buscar celdas adyacentes tras registrar un impacto")
    void hunt_shouldTargetNeighborsAfterHit() {
        // 1. Resetear estrategia
        strategy.reset();

        // 2. Realizar un primer disparo simulado
        byte firstShot = strategy.calculateNextShot(boardView);

        // 3. Simular que el disparo dio en un Barco (CARRIER = 1)
        strategy.setLastShoot(CellContent.CARRIER);

        // 4. El siguiente disparo debe ser un vecino adyacente del primer disparo
        byte secondShot = strategy.calculateNextShot(boardView);

        // Los vecinos posibles para firstShot a una distancia de 1 celda
        int diff = Math.abs(secondShot - firstShot);
        byte dim = GameConfig.BOARD_DIMENSION;

        // Debe ser arriba/abajo (diferencia == dim) o izquierda/derecha (diferencia ==
        // 1)
        assertTrue(diff == 1 || diff == dim,
                "El segundo disparo (" + secondShot + ") debe ser adyacente al primero (" + firstShot + ")");
    }
}
