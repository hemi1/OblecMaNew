package sk.oblecma;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Slavomír
 */
public enum ObrazokDaoFactory {
    Instanse;
    public ObrazokDao dajObrazokDao()
    {
        if(spustac.NapojeneNaDatabazu)
        {
            return new DatabazovyObrazokDao();
        }
        return new PametovyObrazokDao();
    }
}
