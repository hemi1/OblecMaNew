package sk.oblecma;


import java.io.File;
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
public interface ObrazokDao {
     public List<Obrazok> zistiData();
     public void pridajData(File file) throws NepodariloSaPridatObrazokException;
}
