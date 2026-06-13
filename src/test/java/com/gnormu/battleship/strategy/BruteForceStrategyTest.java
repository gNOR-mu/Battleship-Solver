package com.gnormu.battleship.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gnormu.battleship.domain.Board;
import com.gnormu.battleship.domain.BoardView;
import com.gnormu.battleship.domain.Coordinate;

public class BruteForceStrategyTest {

    private Board board;
    private BoardView boardView;
    private BattleshipStrategy strategy;

    @BeforeEach
    void setup() {
        board = new Board();
        boardView = new BoardView(board.getGrid());
        strategy = new BruteForceStrategy();
    }

    @Test
    @DisplayName("Sugerencia: Debe sugerir el movimiento que sigue")
    void suggestMove_shouldSuggestNextMoveCorrectly() {
        // arrange
        List<Coordinate> firstValidCoordinates = List.of(
                Coordinate.of(0, 0),
                Coordinate.of(0, 1),
                Coordinate.of(0, 2));

        // act & assert
        for (int i = 0; i < firstValidCoordinates.size(); i++) {
            assertEquals(firstValidCoordinates.get(i), strategy.calculateNextShot(boardView));
        }

        assertNotEquals(firstValidCoordinates.get(0), strategy.calculateNextShot(boardView));
    }

    @Test
    @DisplayName("Reset: Debe restableer el índice")
    void reset_shouldResetIndexCorrectly() {
        // arrange
        Coordinate firstValidCoordinate = Coordinate.of(0, 0);
        Coordinate secondValidCoordinate = Coordinate.of(0, 1);

        // act & assert
        assertEquals(firstValidCoordinate, strategy.calculateNextShot(boardView));
        assertEquals(secondValidCoordinate, strategy.calculateNextShot(boardView));

        strategy.reset();

        assertEquals(firstValidCoordinate, strategy.calculateNextShot(boardView));
        assertEquals(secondValidCoordinate, strategy.calculateNextShot(boardView));
    }

}
