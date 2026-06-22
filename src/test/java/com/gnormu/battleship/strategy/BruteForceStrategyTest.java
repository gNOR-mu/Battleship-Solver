package com.gnormu.battleship.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gnormu.battleship.domain.Board2D;
import com.gnormu.battleship.domain.BoardView;

public class BruteForceStrategyTest {

    private Board2D board;
    private BoardView boardView;
    private BattleshipStrategy strategy;

    @BeforeEach
    void setup() {
        board = new Board2D();
        boardView = new BoardView(board);
        strategy = new BruteForceStrategy();
    }

    @Test
    @DisplayName("Sugerencia: Debe sugerir el movimiento que sigue")
    void suggestMove_shouldSuggestNextMoveCorrectly() {
        // arrange
        List<Integer> firstValidCoordinates = List.of(0, 1, 2);

        // act & assert
        for (int i = 0; i < firstValidCoordinates.size(); i++) {
            assertEquals(firstValidCoordinates.get(i).intValue(), strategy.calculateNextShot(boardView));
        }

        assertNotEquals(firstValidCoordinates.get(0).intValue(), strategy.calculateNextShot(boardView));
    }

    @Test
    @DisplayName("Reset: Debe restableer el índice")
    void reset_shouldResetIndexCorrectly() {
        // arrange
        int firstValidCoordinate = 0;
        int secondValidCoordinate = 1;

        // act & assert
        assertEquals(firstValidCoordinate, strategy.calculateNextShot(boardView));
        assertEquals(secondValidCoordinate, strategy.calculateNextShot(boardView));

        strategy.reset();

        assertEquals(firstValidCoordinate, strategy.calculateNextShot(boardView));
        assertEquals(secondValidCoordinate, strategy.calculateNextShot(boardView));
    }

}
