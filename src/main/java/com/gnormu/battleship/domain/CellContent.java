package com.gnormu.battleship.domain;

import java.util.stream.IntStream;

/**
 * Clase que representa los distintos tipos de contenidos que puede almacenar
 * una celda utilizando el patrón Flyweight con optimizaciones de tipos
 * primitivos.
 * 
 * @implNote Se ha definido utilizar una clase final con bytes en vez de enum
 *           debido a que ofrecen un mejor rendimiento en las simulaciones.
 * 
 * @implNote Se utilizan los índices [1, cantidad de barcos] como
 *           índices de los largos correspondientes. El agua (WATER) posee el
 *           valor 0 por conveniencia. Solo los indices de los barcos poseen
 *           valores positivos (>=1), los otros índices deben estar invertidos
 *           (ej miss en vez de ser 6 es -6), esto favorece optimizaciones de la
 *           LUT en donde comparar si una casilla impacta a un barco es tan
 *           simple como verificar si el valor de su indice es positivo.
 * 
 * @implNote Al disparar a una casilla se espera que se inviertan los valores
 *           (ej barco impactado 1 pasa a ser -1), lo cual este diseño
 *           favorece y optimiza la utilización de memoria
 * 
 */
public final class CellContent {

    /**
     * Representación del largo de los distintos tipos de celdas basado en los
     * índices de los barcos, se utilizan un orden de largos de mayor a menor por
     * optimizaciones generales, ya que en las verificaciones un barco más largo es
     * menos probable que se haya hundido completamente, permitiendo una salida
     * temprana. Funciona como un Lookup Table de alto rendimiento (LUT).
     * <p>
     * Se utiliza 0 como agua por conveniencia (siempre primer indice), luego deben
     * seguir los barcos y finalmente los elementos adicionales a añadir como por
     * ejemplo "miss", los cuales deben tener largo 0.
     * <p>
     * Se utiliza un arreglo privado por seguridad, previniendo reasignaciones de
     * valores del arreglo
     */
    private static final byte[] SHIP_LENGTHS = {
            0, // 0 = WATER
            5, // 1 = CARRIER
            4, // 2 = BATTLESHIP
            3, // 3 = CRUISER
            3, // 4 = SUBMARINE
            2, // 5 = DESTROYER
            0, // 6 = MISS
    };

    /** Largo del arreglo de las longitudes de los barcos */
    public static final int SHIP_SIZE = SHIP_LENGTHS.length;

    /** Representación de agua en el tablero */
    public static final byte WATER = 0;

    /** Representación de un barco portaaviones en el tablero */
    public static final byte CARRIER = 1;

    /** Representación de un barco acorazado en el tablero */
    public static final byte BATTLESHIP = 2;

    /** Representación de un barco crucero en el tablero */
    public static final byte CRUISER = 3;

    /** Representación de un barco submarino en el tablero */
    public static final byte SUBMARINE = 4;

    /** Representación de un barco destructor en el tablero */
    public static final byte DESTROYER = 5;

    /** índice invertido equivalente a un disparo inválido */
    public static final byte MISS = -6;

    /** Cantidad total de vidas de los barcos */
    public static final int TOTAL_LIFES = calculateTotalLifes();

    /** Tamaño máximo de los barcos */
    public static final int MAX_SHIP_SIZE = calculateMaxShipSize();

    /**
     * Obtiene la longitud correspondiente al tipo de celda
     * 
     * @param cellContent Tipo del contenido de la celda
     * @return Longitud del contenido
     */
    public static byte getLength(byte cellContent) {
        return SHIP_LENGTHS[cellContent];
    }

    /**
     * Realiza una copia segura y optimizada de las dimensiones de los largos de los
     * barcos
     * 
     * @param destination Arreglo en donde se realiza la copia de los largos de los
     *                    barcos
     */
    public static void copyLength(byte[] destination) {
        System.arraycopy(SHIP_LENGTHS, 0, destination, 0, SHIP_LENGTHS.length);
    }

    private static int calculateMaxShipSize() {
        return IntStream.range(0, SHIP_LENGTHS.length)
                .map(i -> SHIP_LENGTHS[i])
                .max()
                .orElse(0);
    }

    /**
     * Calcula la cantidad total de vidas de barcos (equivalentes a sus largos) para
     * completar un juego
     * 
     * @return Cantidad total de vidas de los barcos.
     */
    private static int calculateTotalLifes() {
        return IntStream.range(0, SHIP_LENGTHS.length).map(i -> SHIP_LENGTHS[i]).sum();
    }

    /** Constructor privado, no se permite la inicialización de la clase */
    private CellContent() {
    }

}
