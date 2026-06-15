package com.gnormu.battleship.config;

/**
 * Clase que contiene la configuración del juego.
 * 
 */
public class GameConfig {
    private GameConfig() {
    }

    /** Dimensión de un tablero cuadrado */
    public static final int BOARD_DIMENSION = 10;

    /** Dimensión al cuadrado de un tablero */
    public static final int DIMENSION_SQUARED = BOARD_DIMENSION * BOARD_DIMENSION;

    /**
     * Cantidad total de intentos permitidos correspondiente a
     * {@link GameConfig#DIMENSION_SQUARED} * 1000
     */
    public static final int MAX_ATTEMPTS = DIMENSION_SQUARED * 1000;

}
