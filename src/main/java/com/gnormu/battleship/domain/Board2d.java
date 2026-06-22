package com.gnormu.battleship.domain;

import java.util.Arrays;

import com.gnormu.battleship.config.GameConfig;

/**
 * Clase que representa el tablero de juego.
 * 
 * @implNote Utilizar una matriz para el tablero y un mapa para la ubicación de
 *           los barcos permite un acceso rápido a las celdas y a la información
 *           de los barcos.
 */
public class Board2D extends AbstractBoard {

    private static final String BOARD_NAME = "Board 2D";

    /** Arreglo bidimensional que representa el tablero de juego */
    private final byte[][] grid;

    public Board2D() {
        super();
        this.grid = new byte[GameConfig.BOARD_DIMENSION][GameConfig.BOARD_DIMENSION];
        reset();
    }

    /**
     * {@inheritDoc}
     * 
     * @implNote Recorre todo el tablero en un for, estableciendo todas
     *           las casillas en agua con {@link Arrays#fill}
     */
    public void clearBoardGrid() {
        for (int i = 0; i < GameConfig.BOARD_DIMENSION; i++) {
            Arrays.fill(grid[i], CellContent.WATER);
        }
    }

    @Override
    public byte getCellState(byte coordinate) {
        return grid[coordinate / GameConfig.BOARD_DIMENSION][coordinate % GameConfig.BOARD_DIMENSION];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCellState(byte coordinate, byte state) {
        grid[coordinate / GameConfig.BOARD_DIMENSION][coordinate % GameConfig.BOARD_DIMENSION] = state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBoardName() {
        return BOARD_NAME;
    }

    @Override
    public void putShip(byte coordinate, byte ship) {
        grid[coordinate / GameConfig.BOARD_DIMENSION][coordinate % GameConfig.BOARD_DIMENSION] = ship;
    }
}
