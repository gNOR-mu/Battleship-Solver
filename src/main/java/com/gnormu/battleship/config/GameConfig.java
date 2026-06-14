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

    /** Cantidad total de tableros a generar */
    public static final int TOTAL_BOARDS = 500_000;

    /**
     * Cantidad total de intentos permitidos correspondiente a
     * {@link GameConfig#BOARD_DIMENSION} al cuadrado * 1000
     */
    public static final int MAX_ATTEMPTS = BOARD_DIMENSION * BOARD_DIMENSION * 1000;

}
