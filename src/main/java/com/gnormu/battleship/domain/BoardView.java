package com.gnormu.battleship.domain;

/**
 * Vista de solo lectura del tablero. Garantiza que no se pueda revelar por
 * accidente el tablero original
 */
public class BoardView {
    private final CellState[][] grid;

    public BoardView(CellState[][] grid) {
        this.grid = grid;
    }

    /**
     * Obtiene el estado de una celda del tablero, oculta los barcos sin tocar
     * 
     * @param coord Coordenada del disparo
     * @return Estado de la celda
     */
    public CellState getCellState(Coordinate coord) {

        int row = coord.row();
        int col = coord.column();

        CellState state = grid[row][col];

        // no se revela si la celda contiene un barco
        if (state == CellState.SHIP) {
            return CellState.WATER;
        }

        return state;
    }
}
