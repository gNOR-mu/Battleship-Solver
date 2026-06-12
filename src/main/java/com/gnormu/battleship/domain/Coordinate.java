package com.gnormu.battleship.domain;

/**
 * Representa una coordenada en el tablero de juego.
 * 
 * @param row    Coordenada correspondiente a la fila.
 * @param column Coordenada correspondiente a la columna.
 */
public record Coordinate(
        int row,
        int column) {
}
