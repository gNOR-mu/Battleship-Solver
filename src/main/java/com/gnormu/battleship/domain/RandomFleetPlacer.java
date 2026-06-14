package com.gnormu.battleship.domain;

import java.util.concurrent.ThreadLocalRandom;

import com.gnormu.battleship.config.GameConfig;

public class RandomFleetPlacer implements FleetPlacer {

    /**
     * {@inheritDoc}
     * 
     * @implNote Posiciona todos los barcos en el tablero de forma aleatoria
     */
    @Override
    public void placeShips(AbstractBoard board) {
        for (ShipType ship : ShipType.VALUES) {
            placeShip(board, ship);
        }
    }

    /**
     * Posiciona un barco en el tablero, puede estar posicionado de forma vertical u
     * horizontal
     * 
     * @param ship Barco a posicionar
     */
    private void placeShip(AbstractBoard board, ShipType ship) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int length = ship.getLength();
        boolean placed = false;

        while (!placed) {
            boolean horizontal = random.nextBoolean();
            int row, col;

            if (horizontal) {
                row = random.nextInt(GameConfig.BOARD_DIMENSION);
                col = random.nextInt(GameConfig.BOARD_DIMENSION - length + 1);

                // Verificación Horizontal
                boolean fits = true;
                for (int i = 0; i < length; i++) {
                    if (board.getCellState(row, col + i) != CellState.WATER) {
                        fits = false;
                        break;
                    }
                }

                // Posicionamiento Horizontal
                if (fits) {
                    for (int i = 0; i < length; i++) {
                        board.setCellState(row, col + i, CellState.SHIP);
                        board.shipsGrid.put(Coordinate.of(row, col + i), ship);
                    }
                    placed = true;
                }

            } else {
                row = random.nextInt(GameConfig.BOARD_DIMENSION - length + 1);
                col = random.nextInt(GameConfig.BOARD_DIMENSION);

                // Verificación Vertical
                boolean fits = true;
                for (int i = 0; i < length; i++) {
                    if (board.getCellState(row + i, col) != CellState.WATER) {
                        fits = false;
                        break;
                    }
                }

                // Posicionamiento Vertical
                if (fits) {
                    for (int i = 0; i < length; i++) {
                        board.setCellState(row + i, col, CellState.SHIP);
                        board.shipsGrid.put(Coordinate.of(row + i, col), ship);
                    }
                    placed = true;
                }
            }
        }
    }

}
