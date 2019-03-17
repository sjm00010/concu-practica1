package jimenezmorenosergioprac02;

/**
 * @author Sergio Jiménez Moreno
 */
public class Atributos {
    public int esperandoPremium;
    public int esperandoNormal;
    public int mesasLibres;

    public Atributos() {
        esperandoNormal = 0;
        esperandoPremium = 0;
        mesasLibres = 10;
    }

    public Atributos(int esperandoPremium, int esperandoNormal, int mesasLibres) {
        this.esperandoPremium = esperandoPremium;
        this.esperandoNormal = esperandoNormal;
        this.mesasLibres = mesasLibres;
    }
        
}
