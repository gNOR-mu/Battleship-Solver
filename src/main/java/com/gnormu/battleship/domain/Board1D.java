package com.gnormu.battleship.domain;

import java.util.Arrays;

import com.gnormu.battleship.config.GameConfig;

/**
 * Clase que representa el tablero de juego utilizando un arreglo lineal de una
 * dimensión.
 * 
 */
public class Board1D extends AbstractBoard {

    private final byte[] grid;

    public Board1D() {
        this.grid = new byte[GameConfig.DIMENSION_SQUARED];
        reset();
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Acceso directo indexado en O(1)
     */
    @Override
    public byte getCellState(byte coordinate) {
        return grid[coordinate];
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Acceso directo indexado en O(1)
     */
    @Override
    public void setCellState(byte coordinate, byte state) {
        grid[coordinate] = state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putShip(byte coordinate, byte ship) {
        grid[coordinate] = ship;
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Utiliza {@link Arrays#fill} para rellenar de forma eficiente
     */
    @Override
    protected void clearBoardGrid() {
        Arrays.fill(grid, CellContent.WATER);
    }

}
