package com.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
interface GameRepository extends JpaRepository<GameEntity, Long> {

    /**
     * Returns an active game where one player is waiting for another to join
     *
     * @return game that is not over, with one participant
     */
    @Query("SELECT t FROM GameEntity t where t.player2 is null and t.over = false")
    GameEntity findGameWithOneParticipant();

}