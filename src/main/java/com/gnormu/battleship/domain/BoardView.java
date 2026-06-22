package com.gnormu.battleship.domain;

/**
 * Vista de solo lectura del tablero. Garantiza que no se pueda revelar por
 * accidente el tablero original
 */
public class BoardView {
    private final Board board;

    public BoardView(Board board) {
        this.board = board;
    }

    /**
     * Obtiene el estado de una celda del tablero, oculta los barcos sin tocar
     * 
     * @param coordinate Coordenada lineal del disparo
     * @return Estado de la celda
     */
    public byte getCellState(byte coordinate) {

        byte state = board.getCellState(coordinate);

        // no se revela si la celda contiene un barco
        if (state > 0) {
            return CellContent.WATER;
        }

        return state;
    }

    /**
     * Indica si un barco ha sido hundido
     * 
     * @param ship Identificador del barco
     * @return <code>true</code> en caso de que el barco ya fue hundido,
     *         <code>false</code> en caso contrario
     */
    public boolean isShipSunk(byte ship) {
        return board.isShipSunk(ship);
    }
}
