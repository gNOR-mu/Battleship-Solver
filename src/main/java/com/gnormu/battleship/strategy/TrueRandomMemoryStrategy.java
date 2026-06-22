package com.gnormu.battleship.strategy;

import com.gnormu.battleship.domain.BoardView;
import com.gnormu.battleship.strategy.algorithms.FisherYatesShuffle;

/**
 * Algoritmo de resolución ineficiente que dispara aleatoriamente sin considerar
 * si atina o falla. Simplemente dispara a coordenadas al azar que no se hayan
 * disparado anteriormente.
 * 
 * Utiliza una implementación del algoritmo Fisher-Yates Shuffle para
 * seleccionar una celda aleatoria de las que quedan por disparar.
 * 
 */
public class TrueRandomMemoryStrategy extends AbstractBattleshipStrategy {

    private final FisherYatesShuffle shuffler;

    public TrueRandomMemoryStrategy() {
        shuffler = new FisherYatesShuffle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte calculateNextShot(BoardView boardView) {
        return shuffler.drawRandom();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        shuffler.reset();
    }

}
