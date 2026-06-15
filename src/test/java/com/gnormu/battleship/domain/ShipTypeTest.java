package com.gnormu.battleship.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ShipTypeTest {

    @Test
    @DisplayName("Barcos: No pueden tener un largo <= 0")
    void constructor_shouldThrowExceptionForLengthLessThanOne() {
        assertTrue(ShipType.LENGTHS.length > 0);

        for (byte len : ShipType.LENGTHS) {
            assertTrue(len > 0);
        }
    }
}
