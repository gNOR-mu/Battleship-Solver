package com.gnormu.battleship.domain;

import java.util.Arrays;

import com.gnormu.battleship.config.GameConfig;

public class Board1d extends AbstractBoard {
    private static final String BOARD_NAME = "Board 1D";

    private final byte[] grid;

    public Board1d() {
        super();
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
    public String getBoardName() {
        return BOARD_NAME;
    }

    @Override
    public void putShip(byte coordinate, byte ship) {
        grid[coordinate] = ship;
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Utiliza {@link Arrays#fill}
     */
    @Override
    protected void clearBoardGrid() {
        Arrays.fill(grid, CellContent.WATER);
    }

}
