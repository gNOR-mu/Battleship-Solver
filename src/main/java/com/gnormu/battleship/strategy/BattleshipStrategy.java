package com.gnormu.battleship.strategy;

import com.gnormu.battleship.config.GameConfig;
import com.gnormu.battleship.domain.BoardView;

/**
 * Interfaz que define el comportamiendo de las estrategias de resolución
 */
public interface BattleshipStrategy {

    /**
     * Calcula la coordenada del siguiente disparo.
     * 
     * @param boardView Tablero de solo lectura con información pública del juego
     * @return Coordenada lineal a disparar [0, {@link GameConfig#DIMENSION_SQUARED}
     *         - 1]
     */
    int calculateNextShot(BoardView boardView);

    /**
     * @return Nombre de la estrategia
     */
    String getStrategyName();

    /**
     * Reinicia la estrategia a su estado inicial
     * 
     */
    default void reset() {
    };
}
