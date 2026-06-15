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
public class Board2d extends AbstractBoard {
    /** Arreglo bidimensional que representa el tablero de juego */
    private final byte[][] grid;

    public Board2d() {
        super();
        this.grid = new byte[GameConfig.BOARD_DIMENSION][GameConfig.BOARD_DIMENSION];
        clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @implNote Recorre todo el tablero en un for, estableciendo todas
     *           las casillas en agua con {@link Arrays#fill}
     */
    public void clearBoardGrid() {
        for (int i = 0; i < GameConfig.BOARD_DIMENSION; i++) {
            Arrays.fill(grid[i], CellState.WATER);
        }
    }

    @Override
    public byte getCellState(int coordinate) {
        return grid[coordinate / GameConfig.BOARD_DIMENSION][coordinate % GameConfig.BOARD_DIMENSION];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCellState(int coordinate, byte state) {
        grid[coordinate / GameConfig.BOARD_DIMENSION][coordinate % GameConfig.BOARD_DIMENSION] = state;
    }

}
