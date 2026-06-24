package com.gnormu.battleship.strategy;

import com.gnormu.battleship.config.GameConfig;
import com.gnormu.battleship.domain.BoardView;
import com.gnormu.battleship.strategy.algorithms.FastPRNG;

/**
 * Algoritmo de resolución ineficiente que dispara aleatoriamente sin considerar
 * si atina o falla. Simplemente dispara a coordenadas al azar.
 * 
 * 
 */
public class TrueRandomStrategy implements BattleshipStrategy {

    private final FastPRNG prng = new FastPRNG();

    /**
     * {@inheritDoc}
     * 
     * @implNote Implementación concreta: Elije una coordenada aleatoriamente dentro
     *           de las dimensiones del tablero sin considerar absolutamente nada
     */
    @Override
    public byte calculateNextShot(BoardView boardView) {
        return (byte) prng.nextInt(GameConfig.DIMENSION_SQUARED);
    }

    /**
     * {@inheritDoc}
     * 
     * @implNote Utiliza el nombre de la clase como nombre de la estrategia
     *           eliminando la palabra "Strategy" del nombre
     */
    @Override
    public final String getStrategyName() {
        return this.getClass().getSimpleName()
                .replace("Strategy", "");
    }
}
