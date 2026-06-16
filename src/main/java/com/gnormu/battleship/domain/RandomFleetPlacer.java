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
        int dimension = GameConfig.BOARD_DIMENSION;
        int maxStart = dimension - length;

        while (true) {
            boolean horizontal = random.nextBoolean();
            int row, col;
            int baseCoord;
            int step;

            if (horizontal) {
                row = random.nextInt(dimension);
                col = random.nextInt(maxStart + 1);
                baseCoord = row * dimension + col;
                step = 1;
            } else {
                row = random.nextInt(maxStart + 1);
                col = random.nextInt(dimension);
                baseCoord = row * dimension + col;
                step = dimension;
            }

            // Verificación de ocupación (si cabe)
            boolean fits = true;
            for (int i = 0; i < length; i++) {
                if (board.getCellState(baseCoord + i * step) != CellState.WATER) {
                    fits = false;
                    break;
                }
            }

            // Posicionamiento
            if (fits) {
                for (int i = 0; i < length; i++) {
                    int coord = baseCoord + i * step;
                    board.setCellState(coord, CellState.SHIP);
                    board.putShip(coord, ship);
                }
                break;
            }
        }
    }

}
