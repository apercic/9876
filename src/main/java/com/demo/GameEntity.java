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
    private byte[] state;
    private Boolean turn;

    private Boolean win;

}