package com.gnormu.battleship.domain;

import java.util.Arrays;

import com.gnormu.battleship.config.GameConfig;

public class Board1d extends AbstractBoard {
    private final CellState[] grid;

    public Board1d() {
        super();
        this.grid = new CellState[GameConfig.BOARD_DIMENSION * GameConfig.BOARD_DIMENSION];
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
     * @implNote Calcula el índice usando {@code row * SIZE + col}
     */
    @Override
    public CellState getCellState(int row, int col) {
        return grid[row * GameConfig.BOARD_DIMENSION + col];
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Calcula el índice usando {@code row * SIZE + col}
     */
    @Override
    public void setCellState(int row, int col, CellState state) {
        grid[row * GameConfig.BOARD_DIMENSION + col] = state;
    }

}
