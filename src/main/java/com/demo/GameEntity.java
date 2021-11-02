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
    private byte[] state = new byte[25];
    private String turn;
    private Boolean win;

    private String player1;
    private String player2;

}