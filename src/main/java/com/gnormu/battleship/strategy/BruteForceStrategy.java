package com.gnormu.battleship.strategy;

import com.gnormu.battleship.domain.BoardView;

/**
 * Estrategia de resolución de fuerza bruta: Recorre el grid completo del
 * tablero sin considerar impactos
 */
public class BruteForceStrategy implements BattleshipStrategy {

    private static final String STRATEGY_NAME = "BruteForce";

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
    public int calculateNextShot(BoardView boardView) {
        return idx++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        this.idx = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStrategyName() {
        return STRATEGY_NAME;
    }

}
