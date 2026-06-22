package com.gnormu.battleship.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CellContentTest {

    @Test
    @DisplayName("Agua: Debe tener un largo de 0")
    void waterLength_shouldBeZero() {
        assertEquals(0, CellContent.getLength(CellContent.WATER));
    }

    @Test
    @DisplayName("Total de vidas: Debe ser igual a la suma de los largos de los barcos")
    void totalLifes_shouldMatchSumOfShipLengths() {
        int expectedSum = 0;
        for (byte ship = 1; ship <= 5; ship++) {
            expectedSum += CellContent.getLength(ship);
        }
        assertEquals(expectedSum, CellContent.TOTAL_LIFES);
    }
}
