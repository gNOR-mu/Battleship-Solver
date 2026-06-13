package com.gnormu.battleship.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ShipTypeTest {

    @Test
    @DisplayName("Barcos: No pueden tener un largo <= 0")
    void constructor_shouldThrowExceptionForLengthLessThanOne() {
        // arrange
        List<ShipType> ships = ShipType.VALUES;

        // act & assert
        assertFalse(ships.isEmpty());

        for (ShipType ship : ships) {
            assertTrue(ship.getLength() > 0);
        }
    }
}
