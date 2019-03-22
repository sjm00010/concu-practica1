package jimenezmorenosergioprac02;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
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
        final int INICIO_VARIABLES = 0;
        final int AFORO = 10;
     
        ExecutorService executor = (ExecutorService)Executors.newCachedThreadPool();
        ExecutorService executorCocina = (ExecutorService)Executors.newCachedThreadPool();
        
        List<Cliente> clientes = new ArrayList<>();
        
        Semaphore exmRestaurante = new Semaphore(1);
        Semaphore semPremium = new Semaphore(0);
        Semaphore semNormal = new Semaphore(0);
        Semaphore exmCocina = new Semaphore(1);
        Semaphore semPedidos = new Semaphore(0);
        Atributos atributos = new Atributos(INICIO_VARIABLES, INICIO_VARIABLES, AFORO);
        ContadorClientes contador = new ContadorClientes(NUM_CLIENTES);
        
        ArrayList<Pedido> listaPlatos = new ArrayList<>();
        ArrayList[] pedidoCliente = new ArrayList[NUM_CLIENTES];
        for (int i = 0; i < NUM_CLIENTES; i++) {
            pedidoCliente[i] = new ArrayList<Plato>();
        }
        System.out.println("Hilo(PRINCIPAL) : Crea los clientes");
        for (int i = 0; i < NUM_CLIENTES; i++) {
            Cliente nuevoCliente;
            if(i%2 == 0){
                nuevoCliente = new Cliente(i, TipoCliente.NORMAL, atributos, contador, listaPlatos, pedidoCliente, exmRestaurante, semPremium, semNormal, exmCocina, semPedidos);
            }else{
                nuevoCliente = new Cliente(i, TipoCliente.PREMIUM, atributos, contador, listaPlatos, pedidoCliente, exmRestaurante, semPremium, semNormal, exmCocina, semPedidos);
            }
            clientes.add(nuevoCliente);
        }
        
        System.out.println("Hilo(PRINCIPAL) : Crea la cocina");
        Cocina cocina = new Cocina(listaPlatos, pedidoCliente, exmCocina, semPedidos,contador);
        System.out.println("Hilo(PRINCIPAL) : Ejecuta la cocina");
        executorCocina.execute(cocina);
        
        System.out.println("Hilo(PRINCIPAL) : Ejecuta los clientes y espera a que finalicen");
        List<Future<Integer>> listaTareas = new ArrayList();
        try {
            listaTareas = executor.invokeAll(clientes);
        } catch (InterruptedException ex) {
            System.out.println("Hilo(PRINCIPAL) : Error al invocar los clientes");
        }
        System.out.println("Hilo(PRINCIPAL) : Los clientes han finalizado");
        
        System.out.println("Hilo(PRINCIPAL) : Va a calcular la recaudación total");
        int recaudacion=0;
        for (Future<Integer> listaTarea : listaTareas) {
            try {
                recaudacion += listaTarea.get();
            } catch (InterruptedException | ExecutionException ex) {
                System.out.println("Hilo(PRINCIPAL) : Error al calcular la recaudación");
            }
        }
        System.out.println("Hilo(PRINCIPAL) : La recaudación total a sido "+recaudacion+" €");
        
        System.out.println("Hilo(PRINCIPAL) : Va a interrumpir los clientes.");
        executor.shutdown();
        System.out.println("Hilo(PRINCIPAL) : Va a interrumpir la cocina.");
        executorCocina.shutdown();
        System.out.println("Hilo(PRINCIPAL) : Programa finalizado");
        
    }
    
}
