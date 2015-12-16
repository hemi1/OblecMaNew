/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.sql.Date;

/**
 *
 * @author Slavomír
 */
public class mojDatum extends Date{

    public mojDatum(int year, int month, int day) {
        super(year, month, day);
    }

    mojDatum() {
       super(System.currentTimeMillis());
    }
    
    
    @Override
    public String toString() {
        
        int rok = this.getYear()+1900;
        int mesiac = this.getMonth();
        int den = this.getDate();
        return den+"."+mesiac+" "+rok;
    }
    
}
