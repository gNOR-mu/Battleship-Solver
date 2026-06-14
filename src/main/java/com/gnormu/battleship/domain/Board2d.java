package com.gnormu.battleship.domain;

import java.util.Arrays;
import java.util.Map;

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
    private final CellState[][] grid;

    public Board2d() {
        super();
        this.grid = new CellState[GameConfig.BOARD_DIMENSION][GameConfig.BOARD_DIMENSION];
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

    /**
     * Obtiene el grid del tablero
     * 
     * @return
     */
    public CellState[][] getGrid() {
        return grid;
    }

    /**
     * Obtiene una copia inmutable del mapa que representa la ubicación de los
     * barcos en el tablero
     * 
     * @return Copia inmutable del mapa de barcos
     */
    public Map<Coordinate, ShipType> getShipsGrid() {
        return Map.copyOf(shipsGrid);
    }

    @Override
    public CellState getCellState(int row, int col) {
        return grid[row][col];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCellState(int row, int col, CellState state) {
        grid[row][col] = state;
    }

}
