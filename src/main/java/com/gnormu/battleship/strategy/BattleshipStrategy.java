package com.gnormu.battleship.strategy;

import com.gnormu.battleship.common.Reportable;
import com.gnormu.battleship.config.GameConfig;
import com.gnormu.battleship.domain.BoardView;

/**
 * Interfaz que define el comportamiendo de las estrategias de resolución
 */
public interface BattleshipStrategy extends Reportable {

    /**
     * Calcula la coordenada del siguiente disparo.
     * 
     * @param boardView Tablero de solo lectura con información pública del juego
     * @return Coordenada lineal a disparar [0, {@link GameConfig#DIMENSION_SQUARED}
     *         - 1]
     */
    byte calculateNextShot(BoardView boardView);

    /**
     * Establece el resultado del último disparo
     * 
     * @param lastShoot Contenido inicial de la última celda impactada
     */
    default void setLastShoot(byte lastShoot) {
    }

    /**
     * Reinicia la estrategia a su estado inicial
     * 
     */
    default void reset() {
    };

    /**
     * {@inheritDoc}
     * 
     * @implNote Utiliza el nombre de la clase como
     *           nombre de la estrategia eliminando la palabra "Strategy" del nombre
     */
    default String getName() {
        return this.getClass().getSimpleName().replace("Strategy", "");
    }
}
