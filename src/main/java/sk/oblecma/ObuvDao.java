/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.util.List;

/**
 *
 * @author Rastislav
 */
public interface ObuvDao {
    public void pridajObuv(Obuv novaObuv, Long ID);

    public void vyhodObuv(Long idObuvy, Long ID);

    public List<Obuv> dajVsetkyObuvyPodlaId(Long ID);

    public List<Obuv> dajVsetkyObuvy();
  
    public void upravObuvy(Obuv upravenaObuv);
    
    public Obuv dajObuv(Long id);
}
