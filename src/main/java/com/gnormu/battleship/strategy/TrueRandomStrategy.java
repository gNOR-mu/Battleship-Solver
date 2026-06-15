package com.gnormu.battleship.strategy;

import java.util.concurrent.ThreadLocalRandom;

import com.gnormu.battleship.config.GameConfig;
import com.gnormu.battleship.domain.BoardView;

/**
 * Algoritmo de resolución ineficiente que dispara aleatoriamente sin considerar
 * si atina o falla. Simplemente dispara a coordenadas al azar.
 * 
 * 
 */
public class TrueRandomStrategy implements BattleshipStrategy {

    private static final String STRATEGY_NAME = "TrueRandom - Sin memoria";

    /**
     * {@inheritDoc}
     * 
     * @implNote Implementación concreta: Elije una coordenada aleatoriamente dentro
     *           de las dimensiones del tablero sin considerar absolutamente nada
     */
    @Override
    public int calculateNextShot(BoardView boardView) {
        return ThreadLocalRandom.current().nextInt(GameConfig.DIMENSION_SQUARED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStrategyName() {
        return STRATEGY_NAME;
    }

}
