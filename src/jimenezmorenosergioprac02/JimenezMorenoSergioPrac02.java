package jimenezmorenosergioprac02;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Semaphore;

/**
 * @author Sergio Jiménez Moreno
 */
public class JimenezMorenoSergioPrac02 { 
    
    // Variables de uso común
    public enum TipoCliente { NORMAL, PREMIUM };
    
    public class Atributos{
        public int esperandoPremium;
        public int esperandoNormal;
        public int mesasLibres;

        public Atributos() {
            esperandoNormal = 0;
            esperandoPremium = 0;
            mesasLibres = 10;
        }
        
    };
      
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    
        ExecutorService executor = new ForkJoinPool();
        
        ArrayList<Cliente> clientes = new ArrayList<>();
        
        Semaphore exmRestaurante = new Semaphore(1);
        Semaphore semPremium = new Semaphore(0);
        Semaphore semNormal = new Semaphore(0);
        Semaphore exmCocina = new Semaphore(1);
        Semaphore semPedidos = new Semaphore(0);
        Atributos atributos = new Atributos();
        
        ArrayList<Pedido> listaPlatos = new ArrayList<>();
        ArrayList<Plato>[] pedidoCliente = new ArrayList<Plato>()[];
        
        for (int i = 0; i < 20; i++) {
            if(i%2 == 0){
                Cliente nuevoCliente = new Cliente(i, TipoCliente.NORMAL, atributos, null, pedidoCliente, exmRestaurante, semPremium, semNormal, exmCocina, semPedidos)i, i, i, null, pedidoCliente, exmRestaurante, semPremium, semNormal, exmCocina, semPedidos);
            }else{
                
            }
        }
    }
    
}
