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
    private static Coordinate[][] cache;

    static {
        initializeCache();
    }

    /**
     * Inicializa la caché de coordenadas para la dimensión del tablero dada.
     * Debe ser llamada al inicio del programa.
     * 
     */
    public static void initializeCache() {
        cache = new Coordinate[GameConfig.BOARD_DIMENSION][GameConfig.BOARD_DIMENSION];
        for (int r = 0; r < GameConfig.BOARD_DIMENSION; r++) {
            for (int c = 0; c < GameConfig.BOARD_DIMENSION; c++) {
                cache[r][c] = new Coordinate(r, c);
            }
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
        if (cache != null && row >= 0 && row < cache.length && column >= 0 && column < cache[row].length) {
            return cache[row][column];
        }
        return new Coordinate(row, column);
    }
}
