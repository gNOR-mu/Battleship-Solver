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
    public void placeShips(Board board) {
        for (byte ship = 0; ship < ShipType.COUNT; ship++) {
            placeShip(board, ship);
        }
    }

    /**
     * Posiciona un barco en el tablero, puede estar posicionado de forma vertical u
     * horizontal
     * 
     * @param ship Barco a posicionar
     */
    private void placeShip(Board board, byte ship) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int length = ShipType.LENGTHS[ship];
        boolean placed = false;

        while (!placed) {
            boolean horizontal = random.nextBoolean();
            int row = random.nextInt(GameConfig.BOARD_DIMENSION);
            int col = random.nextInt(GameConfig.BOARD_DIMENSION);

            if (horizontal) {
                // Verificar si traspasa el borde horizontal
                if (col + length > GameConfig.BOARD_DIMENSION) {
                    continue;
                }

                // Verificación de ocupación (si cabe)
                boolean fits = true;
                for (int i = 0; i < length; i++) {
                    int coord = row * GameConfig.BOARD_DIMENSION + (col + i);
                    if (board.getCellState(coord) != CellState.WATER) {
                        fits = false;
                        break;
                    }
                }

                // Posicionamiento Horizontal
                if (fits) {
                    for (int i = 0; i < length; i++) {
                        int coord = row * GameConfig.BOARD_DIMENSION + (col + i);
                        board.setCellState(coord, CellState.SHIP);
                        board.putShip(coord, ship);
                    }
                    placed = true;
                }

            } else {
                // Verificar si traspasa el borde vertical
                if (row + length > GameConfig.BOARD_DIMENSION) {
                    continue;
                }

                // Verificación de ocupación (si cabe)
                boolean fits = true;
                for (int i = 0; i < length; i++) {
                    int coord = (row + i) * GameConfig.BOARD_DIMENSION + col;
                    if (board.getCellState(coord) != CellState.WATER) {
                        fits = false;
                        break;
                    }
                }

                // Posicionamiento Vertical
                if (fits) {
                    for (int i = 0; i < length; i++) {
                        int coord = (row + i) * GameConfig.BOARD_DIMENSION + col;
                        board.setCellState(coord, CellState.SHIP);
                        board.putShip(coord, ship);
                    }
                    placed = true;
                }
            }
        }
    }

}
