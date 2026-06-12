package com.gnormu.battleship.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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
    /** Dimensiones del tablero por defecto, tanto como su ancho y alto */
    private static final int DEFAULT_DIMENSION = 10;

    /** Arreglo bidimensional que representa el tablero de juego */
    private final CellStates[][] grid;

    /** Mapa que representa la ubicación de los barcos en el tablero */
    private final Map<Coordinate, ShipType> shipsGrid;

    /** Arreglo primitivo para la vida de los barcos (patrón Flyweight) */
    private final int[] shipHealths;

    public Board() {
        this.grid = new CellStates[DEFAULT_DIMENSION][DEFAULT_DIMENSION];
        this.shipsGrid = new HashMap<>();
        this.shipHealths = new int[ShipType.values().length];

        initialize();

    }

    /**
     * Limpia completamente el tablero de juego, restableciendo su estado inicial y
     * colocando los barcos aleatoriamente
     */
    public void initialize() {
        // Limpia la matriz volviéndola agua
        for (int i = 0; i < DEFAULT_DIMENSION; i++) {
            for (int j = 0; j < DEFAULT_DIMENSION; j++) {
                grid[i][j] = CellStates.WATER;
            }
        }

        // Limpia el mapa manteniendo su capacidad en memoria
        shipsGrid.clear();

        // Resetea las vidas de los barcos (patrón Flyweight)
        for (ShipType type : ShipType.values()) {
            shipHealths[type.ordinal()] = type.getLength();
        }

        placeShips();
    }

    /**
     * Realiza un disparo en la coordenada indicada, actualizando el estado de la
     * celda
     * y restando vida al barco en caso de impacto.
     * 
     * @param coord Coordenada del disparo
     */
    public void shoot(Coordinate coord) {
        int row = coord.row();
        int col = coord.column();
        if (row < 0 || row >= DEFAULT_DIMENSION || col < 0 || col >= DEFAULT_DIMENSION) {
            return;
        }

        CellStates state = grid[row][col];
        if (state == CellStates.SHIP) {
            grid[row][col] = CellStates.HIT;
            ShipType affectedShip = shipsGrid.get(coord);
            if (affectedShip != null) {
                shipHealths[affectedShip.ordinal()]--;
            }
        } else if (state == CellStates.WATER) {
            grid[row][col] = CellStates.MISS;
        }
    }

    /**
     * Coloca todos los barcos en el tablero de forma aleatoria
     */
    private void placeShips() {
        for (ShipType ship : ShipType.values()) {
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
                row = random.nextInt(DEFAULT_DIMENSION);
                col = random.nextInt(DEFAULT_DIMENSION - length + 1);

                // Verificación Horizontal
                boolean fits = true;
                for (int i = 0; i < length; i++) {
                    if (grid[row][col + i] != CellStates.WATER) {
                        fits = false;
                        break;
                    }
                }

                // Posicionamiento Horizontal
                if (fits) {
                    for (int i = 0; i < length; i++) {
                        grid[row][col + i] = CellStates.SHIP;
                        shipsGrid.put(new Coordinate(row, col + i), ship);
                    }
                    placed = true;
                }

            } else {
                row = random.nextInt(DEFAULT_DIMENSION - length + 1);
                col = random.nextInt(DEFAULT_DIMENSION);

                // Verificación Vertical
                boolean fits = true;
                for (int i = 0; i < length; i++) {
                    if (grid[row + i][col] != CellStates.WATER) {
                        fits = false;
                        break;
                    }
                }

                // Posicionamiento Vertical
                if (fits) {
                    for (int i = 0; i < length; i++) {
                        grid[row + i][col] = CellStates.SHIP;
                        shipsGrid.put(new Coordinate(row + i, col), ship);
                    }
                    placed = true;
                }
            }
        }
    }
}
