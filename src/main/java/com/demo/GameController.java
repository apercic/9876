package com.demo;

import org.springframework.web.bind.annotation.*;

@RestController
class GameController {
    private static GameRepository repository;

    int n = 5; //size of grid = n*n
    int winLine = 3; //how many in line to win

    GameController(GameRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/game/new")
    GameEntity newGame(@RequestParam String userName) {
        GameEntity gameWithOneParticipant = repository.findGameWithOneParticipant();

        if (gameWithOneParticipant == null) {
            GameEntity newGame = new GameEntity();
            newGame.setPlayer1(userName);
            return repository.save(newGame);
        } else {
            gameWithOneParticipant.setPlayer2(userName);
            return repository.save(gameWithOneParticipant);
        }
    }

    @GetMapping("/game/{id}/{playerName}")
    static GameEntity getGame(@PathVariable Long id, @PathVariable String playerName) {
        GameEntity game = repository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
        if (game != null) {
            if (game.getPlayer1() == null)
                game.setPlayer1(playerName);
            else
                game.setPlayer2(playerName);

            return repository.save(game);
        }
        return null;
    }

    /**
     * @param id       - gameId
     * @param position - (0,n-1) - the row in which the current player places mark
     * @param mark     - (X/Y) - the mark of the current player
     * @return
     */
    @PutMapping("/game/{id}/{position}")
    GameEntity updateGame(@PathVariable Long id, @PathVariable Integer position, @PathVariable Byte mark) {
        //position runs from [0 - (n-1)]
        GameEntity game = repository.findById(id).orElse(null);
        if (game != null) {
            //column position is bigger than grid
            if (position >= n) return null;

            //check if we can place in column position
            boolean placed = false;
            int indexPlaced = -1;
            for (int i = n - 1; i >= 0; i--) {
                if (game.getState()[position + n * i] == 0) {
                    game.getState()[position + n * i] = mark;
                    placed = true;
                    indexPlaced = position + n * i;
                    break;
                }
            }
            if (!placed) return null; //if we could not place mark in position column

            if (game.getTurn().equals("player1")) game.setTurn("player2");
            else game.setTurn("player1");


            //ROW
            int count = 0;
            for (int i = 0; i < n; i++) {
                if (game.getState()[position + n * i] == mark) count++;
                else count = 0; //if they are in same row but not together
            }

            if (count >= winLine) {
                game.setWin(true);
                return repository.save(game);
            }

            //COLUMN
            count = 0;
            int leftMostIndex = indexPlaced - (indexPlaced % n);
            for (int i = leftMostIndex; i < leftMostIndex + n; i++) {
                if (game.getState()[i] == mark) count++;
                else count = 0;
            }
            if (count >= winLine) {
                game.setWin(true);
                return repository.save(game);
            }
            count = 0;
            //DIAGONAL1
            count = 0;
            //DIAGONAL2
            return repository.save(game);
        }
        return null;
    }
}