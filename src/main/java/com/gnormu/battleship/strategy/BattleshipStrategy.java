package com.gnormu.battleship.strategy;

import com.gnormu.battleship.domain.BoardView;
import com.gnormu.battleship.domain.Coordinate;

/**
 * Interfaz que define el comportamiendo de las estrategias de resolución
 */
public interface BattleshipStrategy {

    /**
     * Calcula la coordenada {@link Coordinate} del siguiente disparo.
     * 
     * @param boardView Tablero de solo lectura con información pública del juego
     * @return Coordenada a disparar
     */
    Coordinate calculateNextShot(BoardView boardView);

    /**
     * Reinicia la estrategia a su estado inicial
     * 
     */
    default void reset() {
    };
}
