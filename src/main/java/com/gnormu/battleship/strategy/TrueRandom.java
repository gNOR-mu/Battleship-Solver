package com.gnormu.battleship.strategy;

import java.util.concurrent.ThreadLocalRandom;

import com.gnormu.battleship.config.GameConfig;
import com.gnormu.battleship.domain.BoardView;
import com.gnormu.battleship.domain.Coordinate;

/**
 * Algoritmo de resolución ineficiente que dispara aleatoriamente sin considerar
 * si atina o falla. Simplemente dispara a coordenadas al azar.
 * 
 * 
 */
public class TrueRandom implements BattleshipStrategy {

    /**
     * {@inheritDoc}
     * 
     * @implNote Implementación concreta: Elije una coordenada aleatoriamente dentro
     *           de las dimensiones del tablero sin considerar absolutamente nada
     */
    @Override
    public Coordinate calculateNextShot(BoardView boardView) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int row = random.nextInt(GameConfig.BOARD_DIMENSION);
        int col = random.nextInt(GameConfig.BOARD_DIMENSION);

        return Coordinate.of(row, col);
    }

}
