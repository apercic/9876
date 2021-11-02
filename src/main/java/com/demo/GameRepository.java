package com.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
interface GameRepository extends JpaRepository<GameEntity, Long> {

    @Query("SELECT t FROM GameEntity t where t.player2 is null")
    GameEntity findGameWithOneParticipant();

}