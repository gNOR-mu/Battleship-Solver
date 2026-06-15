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
    public byte getCellState(int coordinate) {

        byte state = board.getCellState(coordinate);

        // no se revela si la celda contiene un barco
        if (state == CellState.SHIP) {
            return CellState.WATER;
        }

        return state;
    }
}
