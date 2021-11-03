package com.demo;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class DemoApplicationTests {

    @Mock
    GameRepository repository;

    @Test
    void testChangeTurnToOtherPlayer() {
        GameController gameController = new GameController(repository);
        GameEntity game = new GameEntity();
        game.setPlayer1("player1");
        game.setPlayer2("player2");
        game.setTurn("player1");
        gameController.changeTurnToOtherPlayer(game);
        assertEquals(game.getTurn(), "player2");
    }

    @Test
    void testPlaceMarkInIndexColumn() {
        GameController gameController = new GameController(repository);
        GameEntity game = new GameEntity();
        String gameState = "X".repeat(54);
        game.setState(gameState);
        int rezult = gameController.placeMarkInIndexColumn(game, 1, 'X');
        assertEquals(rezult, -1);
    }

    @Test
    void testCheckRowWin() {
        GameController gameController = new GameController(repository);
        GameEntity game = new GameEntity();
        String gameState = "_".repeat(45) + "X".repeat(9);
        game.setState(gameState);
        game.setWin(true);

        when(repository.save(game)).thenReturn(new GameEntity());
        GameEntity gameEntity = gameController.checkRowWin(game, 'X', 49);

        assertNotEquals(gameEntity, null);
    }

    @Test
    void testChecColumnWin() {
        GameController gameController = new GameController(repository);
        GameEntity game = new GameEntity();
        StringBuilder gameState = new StringBuilder("_".repeat(54));
        gameState.replace(0, 1, "X");
        gameState.replace(9, 10, "X");
        gameState.replace(18, 19, "X");
        gameState.replace(27, 28, "X");
        gameState.replace(36, 37, "X");

        game.setState(gameState.toString());
        game.setWin(true);

        when(repository.save(game)).thenReturn(new GameEntity());
        GameEntity gameEntity = gameController.checkColumnWin(game, 'X', 18);

        assertNotEquals(gameEntity, null);
    }
}
