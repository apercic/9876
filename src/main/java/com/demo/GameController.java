package com.demo;

import org.springframework.web.bind.annotation.*;

@RestController
class GameController {
    private final GameRepository repository;

    GameController(GameRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/game/new")
    GameEntity newGame(@RequestBody GameEntity newGame) {
        return repository.save(newGame);
    }

    @GetMapping("/game/{id}")
    GameEntity getGame(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
    }

    @PutMapping("/game/{id}/{position}")
    GameEntity updateGame(@PathVariable Long id, @PathVariable Long position) {
        return repository.findById(id)
                .map(game -> {
                    game.setState(new byte[]{'0', '0', '1', '0'});
                    game.setTurn(!game.getTurn());
                    return repository.save(game);
                }).orElse(null); //todo or else if game not exist
    }

    @DeleteMapping("/game/{id}")
    void deleteGame(@PathVariable Long id) {
        repository.deleteById(id);
    }
}