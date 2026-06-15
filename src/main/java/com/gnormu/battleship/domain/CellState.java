package com.gnormu.battleship.domain;

/**
 * Clase que representa los posibles estados válidos de una celda del tablero
 * 
 * @implNote Se ha definido utilizar una clase final en vez de enum con bytes
 *           debido a que ofrecen un mejor rendimiento
 */
public final class CellState {
    /** Representa que una casilla contiene agua */
    public static final byte WATER = 0;

    /** Representa que se ha realizado un disparo a una casilla con agua */
    public static final byte MISS = 1;

    /** Representa que se ha realizado un disparo a una casilla con un barco */
    public static final byte HIT = 2;

    /** Representa que una casilla contiene un barco o un fragmento de él */
    public static final byte SHIP = 3;

    private CellState() {
    }
}
