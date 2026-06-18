package com.gnormu.battleship.strategy;

import com.gnormu.battleship.config.GameConfig;
import com.gnormu.battleship.domain.BoardView;
import com.gnormu.battleship.domain.CellState;
import com.gnormu.battleship.strategy.algorithms.FisherYatesShuffle;

/**
 * Estrategia de resolución de Hunt and Target:
 * 
 * - Modo Hunt: Dispara al azar hasta que encuentra un barco (un "Hit").
 * - Modo Target: Una vez que acierta, deja de disparar al azar y empieza a
 * probar las celdas adyacentes (arriba, abajo, izquierda, derecha) hasta hundir
 * el barco.
 * 
 * <p>
 * No considera si habian otros barcos en la trayectoria.
 * 
 * @implNote Es prácticamente un {@link TrueRandomMemoryStrategy} con pasos
 *           adicionales
 */
public class HuntTargetStrategy extends AbstractBattleshipStrategy {

    private final FisherYatesShuffle shuffler;

    // Stack de celdas objetivo para el modo Target
    private final int[] targets;
    private int targetCount;

    private boolean isHuntMode;
    private int lastCoord;

    public HuntTargetStrategy() {
        this.shuffler = new FisherYatesShuffle();
        this.targets = new int[GameConfig.DIMENSION_SQUARED];

        reset();
    }

    @Override
    public int calculateNextShot(BoardView boardView) {
        // Si hubo un disparo anterior, evaluamos su resultado
        // al principio no hay ningún disparo
        if (lastCoord != -1) {
            byte lastShotState = boardView.getCellState(lastCoord);
            if (lastShotState == CellState.HIT) {
                isHuntMode = false;

                int x = lastCoord % GameConfig.BOARD_DIMENSION;
                int y = lastCoord / GameConfig.BOARD_DIMENSION;

                // se añaden los adyacentes
                addTarget(boardView, y > 0 ? lastCoord - GameConfig.BOARD_DIMENSION : -1);
                addTarget(boardView, y < GameConfig.BOARD_DIMENSION - 1 ? lastCoord + GameConfig.BOARD_DIMENSION : -1);
                addTarget(boardView, x > 0 ? lastCoord - 1 : -1);
                addTarget(boardView, x < GameConfig.BOARD_DIMENSION - 1 ? lastCoord + 1 : -1);
            }
        }

        int nextShot = -1;

        // Modo Target
        if (!isHuntMode) {
            while (targetCount > 0) {
                int coord = targets[--targetCount];
                if (boardView.getCellState(coord) == CellState.WATER) {
                    nextShot = coord;
                    shuffler.remove(coord);
                    break;
                }
            }

            if (nextShot == -1) {
                isHuntMode = true;
            }
        }

        // Modo Hunt
        if (isHuntMode) {
            nextShot = shuffler.drawRandom();
        }

        lastCoord = nextShot;
        return nextShot;
    }

    @Override
    public void reset() {
        shuffler.reset();
        this.isHuntMode = true;
        this.lastCoord = -1;
        this.targetCount = 0;
    }

    private void addTarget(BoardView boardView, int coord) {
        if (coord >= 0 && coord < GameConfig.DIMENSION_SQUARED) {
            if (boardView.getCellState(coord) == CellState.WATER) {
                targets[targetCount++] = coord;
            }
        }
    }
}