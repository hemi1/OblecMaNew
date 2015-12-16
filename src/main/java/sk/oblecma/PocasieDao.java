package sk.oblecma;

import java.util.List;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Slavomír
 */
public interface PocasieDao {
    public void zistiPocasieData(String oznaceneMesto) throws NeuspesneZiskanieDatException;
    public Pocasie vratPocasie();
    public void vyhodnotPocasie();
    public List<String> vratMozneLokacie();
}
