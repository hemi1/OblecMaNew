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
public class Pocasie {
  private  int rychlostVetra;
  private  int teplotaCezDen;
  private  int pravdepodobnostZrazok;

  
  private boolean sneh;
  private boolean zima;
  private boolean teplo;
   
    private boolean tropickaHorucava;
    private boolean horuco;
    private boolean teplejsie;
    private boolean priemerne;
    private boolean chladno;
    private boolean velkaZima;
    
    private boolean dazd;

    private boolean slabyVietor;
    private boolean silnyVietor;
    private boolean velmiSilnyVietor;
    private boolean burlivyVietor;

    public boolean isSlabyVietor() {
        return slabyVietor;
    }

    public void setSlabyVietor(boolean slabyVietor) {
        this.slabyVietor = slabyVietor;
    }

    public boolean isSilnyVietor() {
        return silnyVietor;
    }

    public void setSilnyVietor(boolean silnyVietor) {
        this.silnyVietor = silnyVietor;
    }

    public boolean isVelmiSilnyVietor() {
        return velmiSilnyVietor;
    }

    public void setVelmiSilnyVietor(boolean velmiSilnyVietor) {
        this.velmiSilnyVietor = velmiSilnyVietor;
    }

    public boolean isBurlivyVietor() {
        return burlivyVietor;
    }

    public void setBurlivyVietor(boolean burlivyVietor) {
        this.burlivyVietor = burlivyVietor;
    }
    
    public boolean isDazd() {
        return dazd;
    }

    public void setDazd(boolean dazd) {
        this.dazd = dazd;
    }
    public int getRychlostVetra() {
        return rychlostVetra;
    }

    public void setRychlostVetra(int rychlostVetra) {
        this.rychlostVetra = rychlostVetra;
    }

    public int getTeplotaCezDen() {
        return teplotaCezDen;
    }

    public void setTeplotaCezDen(int teplotaCezDen) {
        this.teplotaCezDen = teplotaCezDen;
    }

    public int getPravdepodobnostZrazok() {
        return pravdepodobnostZrazok;
    }

    public void setPravdepodobnostZrazok(int pravdepodobnostZrazok) {
        this.pravdepodobnostZrazok = pravdepodobnostZrazok;
    }

    public boolean isSneh() {
        return sneh;
    }

    public void setSneh(boolean sneh) {
        this.sneh = sneh;
    }

    public boolean isZima() {
        return zima;
    }

    public void setZima(boolean zima) {
        this.zima = zima;
    }

    public boolean isTeplo() {
        return teplo;
    }

    public void setTeplo(boolean teplo) {
        this.teplo = teplo;
    }

    public boolean isTropickaHorucava() {
        return tropickaHorucava;
    }

    public void setTropickaHorucava(boolean tropickaHorucava) {
        this.tropickaHorucava = tropickaHorucava;
    }

    public boolean isHoruco() {
        return horuco;
    }

    public void setHoruco(boolean horuco) {
        this.horuco = horuco;
    }

    public boolean isTeplejsie() {
        return teplejsie;
    }

    public void setTeplejsie(boolean teplejsie) {
        this.teplejsie = teplejsie;
    }

    public boolean isPriemerne() {
        return priemerne;
    }

    public void setPriemerne(boolean priemerne) {
        this.priemerne = priemerne;
    }

    public boolean isChladno() {
        return chladno;
    }

    public void setChladno(boolean chladno) {
        this.chladno = chladno;
    }

    public boolean isVelkaZima() {
        return velkaZima;
    }

    public void setVelkaZima(boolean velkaZima) {
        this.velkaZima = velkaZima;
    }
    
    public boolean fuka()
    {
        if(silnyVietor || velmiSilnyVietor || burlivyVietor)
        {
            return true;
        }
        
        return false;
    }
  
   
}
