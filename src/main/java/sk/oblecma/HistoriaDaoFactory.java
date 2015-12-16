/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

/**
 *
 * @author Slavomír
 */
public enum HistoriaDaoFactory {
    instance;
    public HistoriaDao dajHistoriaDao()
    {
        
        if(spustac.NapojeneNaDatabazu)
        {
            return new DatabazovaHistoriaDao();
        }
        
        return new PamatovaHistoriaDao();
    }
}
