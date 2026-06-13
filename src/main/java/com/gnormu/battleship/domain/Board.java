package com.gnormu.battleship.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.gnormu.battleship.config.GameConfig;

/**
 * Clase que representa el tablero de juego.
 * 
 * @implNote Utilizar una matriz para el tablero y un mapa para la ubicación de
 *           los barcos permite un acceso rápido a las celdas y a la información
 *           de los barcos.
 * 
 * @implNote Utilizar un arreglo primitivo para la vida de los barcos reduce el
 *           uso de memoria y tiempo de clonado al momento de utilizarlos en un
 *           tablero con cientos de miles de {@link Board} como se pretende
 *           usar.
 * 
 * @implNote Se ha optado utilizar por el patrón Flyweight para la instanciación
 *           de los barcos ({@link ShipType}), por lo que la lógica de manejo de
 *           vida del barco se ha delegado a la clase {@link Board}
 */
public class Board {
    /** Dimensión del tablero (ancho y alto) */
    private final int dimension;

    /** Arreglo bidimensional que representa el tablero de juego */
    private final CellState[][] grid;

    /** Mapa que representa la ubicación de los barcos en el tablero */
    private final Map<Coordinate, ShipType> shipsGrid;

    /** Arreglo primitivo para la vida de los barcos (patrón Flyweight) */
    private final int[] shipHealths;

    public Board() {
        this.dimension = GameConfig.BOARD_DIMENSION;
        this.grid = new CellState[dimension][dimension];
        this.shipsGrid = new HashMap<>();
        this.shipHealths = new int[ShipType.VALUES.size()];

        initialize();
    }

    /**
     * Limpia completamente el tablero de juego, restableciendo su estado inicial y
     * colocando los barcos aleatoriamente
     */
    public void initialize() {
        // Limpia la matriz volviéndola agua
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                grid[i][j] = CellState.WATER;
            }
        }

        // Limpia el mapa manteniendo su capacidad en memoria
        shipsGrid.clear();

        // Resetea las vidas de los barcos (patrón Flyweight)
        for (ShipType type : ShipType.VALUES) {
            shipHealths[type.ordinal()] = type.getLength();
        }

        placeShips();
    }

    /**
     * Realiza un disparo en la coordenada indicada, actualizando el estado de la
     * celda y restando vida al barco en caso de impacto.
     * 
     * @param coord Coordenada del disparo
     * 
     * @throws IllegalArgumentException si la coordenada del disparo está fuera de
     *                                  los límites del tablero
     */
    public void shoot(Coordinate coord) {
        int row = coord.row();
        int col = coord.column();
        if (row < 0 || row >= dimension || col < 0 || col >= dimension) {
            throw new IllegalArgumentException(
                    "Disparo fuera de los límites: " + row + "," + col);
        }

        CellState state = grid[row][col];
        if (state == CellState.SHIP) {
            grid[row][col] = CellState.HIT;
            ShipType affectedShip = shipsGrid.get(coord);
            if (affectedShip != null) {
                shipHealths[affectedShip.ordinal()]--;
            }
        } else if (state == CellState.WATER) {
            grid[row][col] = CellState.MISS;
        }
    }

    /**
     * Coloca todos los barcos en el tablero de forma aleatoria
     */
    private void placeShips() {
        for (ShipType ship : ShipType.VALUES) {
            placeShip(ship);
        }
    }

    /**
     * Posiciona un barco en el tablero, puede estar posicionado de forma vertical u
     * horizontal
     * 
     * @param ship Barco a posicionar
     */
    private void placeShip(ShipType ship) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int length = ship.getLength();
        boolean placed = false;

        while (!placed) {
            boolean horizontal = random.nextBoolean();
            int row, col;

            if (horizontal) {
                row = random.nextInt(dimension);
                col = random.nextInt(dimension - length + 1);

                // Verificación Horizontal
                boolean fits = true;
                for (int i = 0; i < length; i++) {
                    if (grid[row][col + i] != CellState.WATER) {
                        fits = false;
                        break;
                    }
                }

                // Posicionamiento Horizontal
                if (fits) {
                    for (int i = 0; i < length; i++) {
                        grid[row][col + i] = CellState.SHIP;
                        shipsGrid.put(Coordinate.of(row, col + i), ship);
                    }
                    placed = true;
                }

            } else {
                row = random.nextInt(dimension - length + 1);
                col = random.nextInt(dimension);

                // Verificación Vertical
                boolean fits = true;
                for (int i = 0; i < length; i++) {
                    if (grid[row + i][col] != CellState.WATER) {
                        fits = false;
                        break;
                    }
                }

                // Posicionamiento Vertical
                if (fits) {
                    for (int i = 0; i < length; i++) {
                        grid[row + i][col] = CellState.SHIP;
                        shipsGrid.put(Coordinate.of(row + i, col), ship);
                    }
                    placed = true;
                }
            }
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
     * Verifica si el juego ha terminado
     * 
     * @return true si el juego ha terminado, false en caso contrario
     */
    public boolean isGameOver() {
        for (int health : shipHealths) {
            if (health > 0) {
                return false;
            }
        }
        return true;
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
}
