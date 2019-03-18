package jimenezmorenosergioprac02;

/**
 * @author Sergio Jiménez Moreno
 */
public class Atributos {
    private int esperandoPremium;
    private int esperandoNormal;
    private int mesasLibres;

    public Atributos(int esperandoPremium, int esperandoNormal, int mesasLibres) {
        this.esperandoPremium = esperandoPremium;
        this.esperandoNormal = esperandoNormal;
        this.mesasLibres = mesasLibres;
    }

    public int getEsperandoNormal() {
        return esperandoNormal;
    }

    public int getEsperandoPremium() {
        return esperandoPremium;
    }

    public int getMesasLibres() {
        return mesasLibres;
    }

    public void incrementaEsperandoNormal() {
        this.esperandoNormal++;
    }
    
    public void decrementaEsperandoNormal() {
        this.esperandoNormal--;
    }

    public void incrementaEsperandoPremium() {
        this.esperandoPremium++;
    }
     
    public void decrementaEsperandoPremium() {
        this.esperandoPremium--;
    }

    public void incrementaMesasLibres() {
        this.mesasLibres++;
    }
    
    public void decrementaMesasLibres() {
        this.mesasLibres--;
    }
}
