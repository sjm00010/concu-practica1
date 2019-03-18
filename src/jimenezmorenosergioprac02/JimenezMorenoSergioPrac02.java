package jimenezmorenosergioprac02;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sergio Jiménez Moreno
 */
public class JimenezMorenoSergioPrac02 { 
    
    // Variables de uso común
    public enum TipoCliente { NORMAL, PREMIUM };
      
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Constantes
        final int NUM_CLIENTES = 20;
    
        ExecutorService executor = new ForkJoinPool();
        
        List<Cliente> clientes = new ArrayList<>();
        
        Semaphore exmRestaurante = new Semaphore(1);
        Semaphore semPremium = new Semaphore(0);
        Semaphore semNormal = new Semaphore(0);
        Semaphore exmCocina = new Semaphore(1);
        Semaphore semPedidos = new Semaphore(0);
        Atributos atributos = new Atributos();
        
        ArrayList<Pedido> listaPlatos = new ArrayList<>();
        //ArrayList<Plato>[] pedidoCliente = new ArrayList<>(NUM_CLIENTES);
        ArrayList<ArrayList<Plato>> pedidoCliente = new ArrayList<>();
        
        for (int i = 0; i < NUM_CLIENTES; i++) {
            Cliente nuevoCliente;
            if(i%2 == 0){
                nuevoCliente = new Cliente(i, TipoCliente.NORMAL, atributos, listaPlatos, pedidoCliente, exmRestaurante, semPremium, semNormal, exmCocina, semPedidos);
            }else{
                nuevoCliente = new Cliente(i, TipoCliente.PREMIUM, atributos, listaPlatos, pedidoCliente, exmRestaurante, semPremium, semNormal, exmCocina, semPedidos);
            }
            clientes.add(nuevoCliente);
        }
        
        Cocina cocina = new Cocina(listaPlatos, pedidoCliente, exmCocina, semPedidos);
        Thread hiloCocina = new Thread(cocina);
        hiloCocina.start();
        
        List<Future<Integer>> listaTareas = new ArrayList();
        try {
            listaTareas = executor.invokeAll(clientes);
        } catch (InterruptedException ex) {
            Logger.getLogger(JimenezMorenoSergioPrac02.class.getName()).log(Level.SEVERE, null, ex);
        }
        int recaudacion=0;
        for (Future<Integer> listaTarea : listaTareas) {
            try {
                recaudacion += listaTarea.get();
            } catch (InterruptedException ex) {
                Logger.getLogger(JimenezMorenoSergioPrac02.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(JimenezMorenoSergioPrac02.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        System.out.println("Hilo(PRINCIPAL) : La recaudacion total a sido "+recaudacion+" EUROS");
        
        executor.shutdown();
        hiloCocina.interrupt();
        
    }
    
}
