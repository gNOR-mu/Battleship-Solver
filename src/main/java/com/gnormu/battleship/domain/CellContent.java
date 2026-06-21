package com.gnormu.battleship.domain;

/**
 * Clase que representa los distintos tipos de contenidos que puede almacenar
 * una celda utilizando el patrón Flyweight con optimizaciones de tipos
 * primitivos.
 * 
 * @implNote Se ha definido utilizar una clase final con bytes en vez de enum
 *           debido a que ofrecen un mejor rendimiento en las simulaciones.
 * 
 * @implNote Se utilizan los índices 0...4 (los barcos) como índices de los
 *           largos correspondientes. El agua (WATER) posee el valor
 *           Byte.MIN_VALUE.
 */
public final class CellContent {
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

    /**
     * Representación del largo de los distintos tipos de celdas basado en los
     * índices de los barcos, se utilizan un orden de largos de mayor a menor por
     * optimizaciones generales, ya que en las verificaciones un barco más largo es
     * menos probable que se haya hundido completamente, permitiendo una salida
     * temprana
     */
    private static final byte[] LENGTHS = {
            0, // WATER = 0
            5, // CARRIER = 1
            4, // BATTLESHIP = 2
            3, // CRUISER = 3
            3, // SUBMARINE = 4
            2 // DESTROYER = 5
    };

    /**
     * Obtiene la longitud correspondiente al tipo de celda
     * 
     * @param cellContent Tipo del contenido de la celda
     * @return Longitud del contenido
     */
    public static byte getLength(byte cellContent) {
        return LENGTHS[cellContent];
    }

    /**
     * Realiza una copia segura y optimizada de las longitudes iniciales de los
     * barcos en el arreglo de destino provisto, evitando creación de objetos en
     * memoria.
     * 
     * @param destination Arreglo destino donde se copian las longitudes
     */
    public static void copyLengths(byte[] destination) {
        System.arraycopy(LENGTHS, 0, destination, 0, LENGTHS.length);
    }

    /** Constructor privado, no se permite la inicialización de la clase */
    private CellContent() {
    }

}
