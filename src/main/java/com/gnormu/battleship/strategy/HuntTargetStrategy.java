package com.gnormu.battleship.strategy;

import java.util.concurrent.ThreadLocalRandom;

import com.gnormu.battleship.config.GameConfig;
import com.gnormu.battleship.domain.BoardView;
import com.gnormu.battleship.domain.CellState;

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

    private boolean isHuntMode;
    private int remainingCells;
    private int lastCoord;

    // Arreglo Fisher-Yates para el azar
    private final int[] emptyCells;

    // Arreglo de búsqueda inversa para O(1) en eliminaciones
    private final int[] cellToIndex;

    // Stack de celdas objetivo para el modo Target
    private final int[] targets;
    private int targetCount;

    public HuntTargetStrategy() {
        int totalCells = GameConfig.DIMENSION_SQUARED;
        this.emptyCells = new int[totalCells];
        this.cellToIndex = new int[totalCells];
        this.targets = new int[totalCells];

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
                    break;
                }
            }

            if (nextShot != -1) {
                removeFromEmptyCells(nextShot);
            } else {
                isHuntMode = true;
            }
        }

        // Modo Hunt
        if (isHuntMode) {
            int randomIndex = ThreadLocalRandom.current().nextInt(remainingCells);
            nextShot = emptyCells[randomIndex];

            remainingCells--;
            int lastAvailableCell = emptyCells[remainingCells];

            emptyCells[randomIndex] = lastAvailableCell;
            cellToIndex[lastAvailableCell] = randomIndex;
        }

        lastCoord = nextShot;
        return nextShot;
    }

    private void addTarget(BoardView boardView, int coord) {
        if (coord >= 0 && coord < GameConfig.DIMENSION_SQUARED) {
            if (boardView.getCellState(coord) == CellState.WATER) {
                targets[targetCount++] = coord;
            }
        }
    }

    private void removeFromEmptyCells(int coord) {
        int indexToRemove = cellToIndex[coord];

        if (indexToRemove < remainingCells) {
            remainingCells--;
            int lastAvailableCell = emptyCells[remainingCells];
            emptyCells[indexToRemove] = lastAvailableCell;
            cellToIndex[lastAvailableCell] = indexToRemove;
            cellToIndex[coord] = GameConfig.DIMENSION_SQUARED;
        }
    }

    @Override
    public void reset() {
        int totalCells = GameConfig.DIMENSION_SQUARED;
        for (int i = 0; i < totalCells; i++) {
            this.emptyCells[i] = i;
            this.cellToIndex[i] = i;
        }
        this.remainingCells = totalCells;
        this.isHuntMode = true;
        this.lastCoord = -1;
        this.targetCount = 0;
    }
}