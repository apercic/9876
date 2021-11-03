package com.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
class GameEntity {

    private @Id
    @GeneratedValue
    Long id;
    private String state = "_".repeat(25);
    private String turn;

    private boolean win;
    private String playerWin;
    private boolean over;

    private String player1;
    private String player2;

}