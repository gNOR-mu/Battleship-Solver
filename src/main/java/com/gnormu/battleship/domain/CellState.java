package com.gnormu.battleship.domain;

/**
 * Enum que representa los posibles estados válidos de una celda del tablero
 */
public enum CellState {
    /** Representa que una casilla contiene agua */
    WATER,

    /** Representa que se ha realizado un disparo a una casilla con agua */
    MISS,

    /** Representa que se ha realizado un disparo a una casilla con un barco */
    HIT,

    /** Representa que una casilla contiene un barco o un fragmento de él */
    SHIP,
}
