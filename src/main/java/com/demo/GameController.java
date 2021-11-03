package com.demo;

import com.exceptions.GameCantPlaceMarkException;
import com.exceptions.GameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
class GameController {
    private static GameRepository repository;

    int columns = 9;
    int rows = 6;
    int winLine = 5;

    GameController(GameRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/game/new")
    @Transactional
    GameEntity newGame(@RequestParam String userName) {
        GameEntity gameWithOneParticipant = repository.findGameWithOneParticipant();

        if (gameWithOneParticipant == null) {
            GameEntity newGame = new GameEntity();
            newGame.setPlayer1(userName);
            newGame.setTurn(userName);
            return repository.save(newGame);
        } else {
            gameWithOneParticipant.setPlayer2(userName);
            return repository.save(gameWithOneParticipant);
        }
    }

    @GetMapping("/game/{id}")
    GameEntity getGame(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
    }

    @PostMapping("/game/{id}/over")
    GameEntity setGameOver(@PathVariable Long id) {
        GameEntity game = repository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
        game.setOver(true);
        return repository.save(game);
    }

    /**
     * @param id       - gameId
     * @param position - (0,n-1) - the row in which the current player places mark
     * @param mark     - (X/Y) - the mark of the current player
     * @return
     */
    @PostMapping("/game/{id}/{position}/{mark}")
    GameEntity updateGame(@PathVariable Long id, @PathVariable Integer position, @PathVariable Character mark) throws GameCantPlaceMarkException {
        GameEntity game = repository.findById(id).orElse(null);

        if (game == null) throw new GameNotFoundException(id);
        if (position >= columns) throw new GameCantPlaceMarkException(position);
        int indexPlaced = placeMarkInIndexColumn(game, position, mark);
        if (indexPlaced == -1) throw new GameCantPlaceMarkException(position);

        /* ROW */
        GameEntity rowWin = checkRowWin(game, mark, indexPlaced);
        if (rowWin != null) return rowWin;

        /* COLUMN */
        GameEntity columnWin = checkColumnWin(game, mark, indexPlaced);
        if (columnWin != null) return columnWin;


        /* DIAGONAL1 */
        /* DIAGONAL2 */


        changeTurnToOtherPlayer(game);
        return repository.save(game);
    }

    /**
     * It is now the other players turn to make a move
     *
     * @param game game entity
     */
    public void changeTurnToOtherPlayer(GameEntity game) {
        if (game.getTurn().equals(game.getPlayer1())) game.setTurn(game.getPlayer2());
        else game.setTurn(game.getPlayer1());
    }

    /**
     * Try to place mark in column with given index
     *
     * @param game
     * @param position
     * @param mark
     * @return index of placed mark or -1 if it wasn't possible to place mark
     */
    public int placeMarkInIndexColumn(GameEntity game, Integer position, Character mark) {
        int indexPlaced = -1;
        for (int i = rows - 1; i >= 0; i--) {
            if (game.getState().charAt(position + columns * i) == '_') {
                StringBuilder temp = new StringBuilder(game.getState());
                temp.replace(position + columns * i, position + columns * i + 1, mark.toString());
                game.setState(temp.toString());
                indexPlaced = position + columns * i;
                break;
            }
        }
        return indexPlaced;
    }

    /**
     * @param game        game entity
     * @param mark        X/Y the mark of the current player
     * @param indexPlaced the index of last placed mark
     * @return game if row is winning or null if not
     * @return
     */
    public GameEntity checkColumnWin(GameEntity game, Character mark, int indexPlaced) {
        int count = 0;
        int maxCount = 0;

        int topMostIndex = indexPlaced % columns;
        for (int i = 0; i < rows; i++) {
            if (game.getState().charAt(topMostIndex + i * columns) == mark)
                count++;
            else {
                if (count > maxCount) maxCount = count;
                count = 0;
            }
        }

        return checkCountForWin(game, Math.max(count, maxCount));
    }

    /**
     * @param game        game entity
     * @param mark        X/Y the mark of the current player
     * @param indexPlaced the index of last placed mark
     * @return game if row is winning or null if not
     */
    public GameEntity checkRowWin(GameEntity game, Character mark, int indexPlaced) {
        int count = 0;
        int maxCount = 0;

        for (int i = 0; i < columns; i++) {
            if (game.getState().charAt(indexPlaced - indexPlaced % columns + i) == mark) count++;
            else {
                if (count > maxCount) maxCount = count;
                count = 0;
            }
        }

        return checkCountForWin(game, Math.max(count, maxCount));
    }

    /**
     * @param game  game entity
     * @param count the count of marks
     * @return game if count is enough to win game or null if not
     */
    public GameEntity checkCountForWin(GameEntity game, int count) {
        if (count >= winLine) {
            game.setWin(true);
            game.setPlayerWin(game.getTurn());
            return repository.save(game);
        }
        return null;
    }
}