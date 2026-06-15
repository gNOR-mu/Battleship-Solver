package com.gnormu.battleship.domain;

import java.util.Arrays;

import com.gnormu.battleship.config.GameConfig;

public class Board1d extends AbstractBoard {
    private static final String BOARD_NAME = "Board 1D";

    private final byte[] grid;

    public Board1d() {
        super();
        this.grid = new byte[GameConfig.DIMENSION_SQUARED];
        clear();
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Utiliza {@link Arrays#fill}
     */
    @Override
    protected void clearBoardGrid() {
        Arrays.fill(grid, CellState.WATER);
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Acceso directo indexado en O(1)
     */
    @Override
    public byte getCellState(int coordinate) {
        return grid[coordinate];
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Acceso directo indexado en O(1)
     */
    @Override
    public void setCellState(int coordinate, byte state) {
        grid[coordinate] = state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBoardName() {
        return BOARD_NAME;
    }

}
