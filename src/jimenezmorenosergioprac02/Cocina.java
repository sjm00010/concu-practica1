package jimenezmorenosergioprac02;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Sergio Jiménez Moreno
 */
public class Cocina implements Runnable{
    // Constantes
    private final int TIEMPO_COCINAR = 1;
    
    // Variables locales
    private int idCliente;
    private Semaphore mesa;
    private Plato plato;
    private Pedido pedido;
    private boolean finServicio;
    
    // Variables de instancia
    private ArrayList<Pedido> listaPlatos;
    private ArrayList<Plato>[] pedidoCliente;
    
    // Semaforos
    private Semaphore exmCocina;
    private Semaphore semPedidos;

    public Cocina( ArrayList<Pedido> listaPlatos, ArrayList<Plato>[] pedidoCliente, Semaphore exmCocina, Semaphore semPedidos) {
        this.finServicio = false;
        this.listaPlatos = listaPlatos;
        this.pedidoCliente = pedidoCliente;
        this.exmCocina = exmCocina;
        this.semPedidos = semPedidos;
    }
    
    @Override
    public void run() {
        do{
            try {
                recogerPedido();
                prepararPedido();
                servirPedido();
            } catch (InterruptedException ex) {
                System.out.println("COCINA - El proceso va a finalizar.");
                finServicio = true;
            }
        }while(!finServicio);
        
    }
    
    private void recogerPedido() throws InterruptedException{
        semPedidos.wait();
        exmCocina.wait();
        pedido = listaPlatos.get(0);
        exmCocina.notify();
        
        idCliente = pedido.getIdCliente();
        mesa = pedido.getSemMesa();
        plato = pedido.getPlato();
    }
    
    private void servirPedido(){
        pedidoCliente[idCliente].add(plato);
        mesa.notify();
    }
    
    private void prepararPedido() throws InterruptedException{
        TimeUnit.SECONDS.sleep(TIEMPO_COCINAR);
    }
    
}
