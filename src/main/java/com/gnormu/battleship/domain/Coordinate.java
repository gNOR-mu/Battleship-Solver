package com.gnormu.battleship.domain;

import com.gnormu.battleship.config.GameConfig;

/**
 * Representa una coordenada en el tablero de juego.
 * 
 * @param row    Coordenada correspondiente a la fila.
 * @param column Coordenada correspondiente a la columna.
 */
public record Coordinate(
        int row,
        int column) {

    /** Caché de coordenadas pre-creadas */
    private static Coordinate[] cache;
    static {
        initializeCache();
    }

    /**
     * Inicializa la caché de coordenadas para la dimensión del tablero dada.
     * Debe ser llamada al inicio del programa.
     * 
     */
    public static void initializeCache() {
        cache = new Coordinate[GameConfig.BOARD_DIMENSION * GameConfig.BOARD_DIMENSION];
        for (int i = 0; i < GameConfig.BOARD_DIMENSION * GameConfig.BOARD_DIMENSION; i++) {
            cache[i] = new Coordinate(i / GameConfig.BOARD_DIMENSION, i % GameConfig.BOARD_DIMENSION);
        }
    }

    /**
     * Obtiene la coordenada cacheada si está dentro de los límites de la caché.
     * Si no se ha inicializado la caché o está fuera de sus límites, crea una nueva
     * instancia.
     * 
     * @param row    Fila
     * @param column Columna
     * @return Instancia cacheada o nueva si está fuera de límites
     */
    public static Coordinate of(int row, int column) {
        if (row >= 0 && row < GameConfig.BOARD_DIMENSION && column >= 0 && column < GameConfig.BOARD_DIMENSION) {
            return cache[row * GameConfig.BOARD_DIMENSION + column];
        }
        return new Coordinate(row, column);
    }
}
