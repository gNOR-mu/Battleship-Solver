package com.gnormu.battleship.strategy;

import com.gnormu.battleship.config.GameConfig;
import com.gnormu.battleship.domain.BoardView;
import com.gnormu.battleship.domain.Coordinate;

/**
 * Estrategia de resolución de fuerza bruta: Recorre el grid completo del
 * tablero sin considerar impactos
 */
public class BruteForceStrategy implements BattleshipStrategy {

    /**
     * Indice utilizado para recorrer el tablero
     */
    private int idx = 0;

    /**
     * {@inheritDoc}
     * 
     * @implNote Implementación concreta: Recorre el tablero completo de izquierda a
     *           derecha, arriba a abajo. No considera impactos anteriores y calcula
     *           los índices de fila y columna según el {@link #idx}, el cual se
     *           incrementa luego de utilizarse
     */
    @Override
    public Coordinate calculateNextShot(BoardView boardView) {
        final int row = idx / GameConfig.BOARD_DIMENSION;
        final int col = idx % GameConfig.BOARD_DIMENSION;
        idx++;
        return Coordinate.of(row, col);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        this.idx = 0;
    }

}
