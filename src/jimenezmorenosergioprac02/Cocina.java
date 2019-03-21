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
    ContadorClientes contador;
    
    // Semaforos
    private Semaphore exmCocina;
    private Semaphore semPedidos;

    public Cocina( ArrayList<Pedido> listaPlatos, ArrayList<Plato>[] pedidoCliente, Semaphore exmCocina, Semaphore semPedidos, ContadorClientes contador) {
        this.finServicio = false;
        this.listaPlatos = listaPlatos;
        this.pedidoCliente = pedidoCliente;
        this.exmCocina = exmCocina;
        this.semPedidos = semPedidos;
        this.contador = contador;
    }
    
    @Override
    public void run() {
        do{
            try {
                recogerPedido();
                prepararPedido();
                servirPedido();
                if(contador.getNumClientes() <= 5 && listaPlatos.isEmpty()){
                    finServicio = true;
                }
            } catch (InterruptedException ex) {
                System.out.println("COCINA - ERROR : El proceso va a finalizar.");
                finServicio = true;
            }
        }while(!finServicio);
        System.out.println("COCINA - El proceso a finalizado.");
    }
    
    private void recogerPedido() throws InterruptedException{
        semPedidos.acquire();
        exmCocina.acquire();
        listaPlatos.sort(null);
        pedido = listaPlatos.remove(0);
        exmCocina.release();
        
        idCliente = pedido.getIdCliente();
        mesa = pedido.getSemMesa();
        plato = pedido.getPlato();
        System.out.println("COCINA - Recogio un pedido de "+plato.getPrecio()+" €");
    }
    
    private void servirPedido(){
        pedidoCliente[idCliente].add(plato);
        //System.out.println("COCINA - Preparo un pedido de "+plato.getPrecio()+" €");
        mesa.release();
    }
    
    private void prepararPedido() throws InterruptedException{
        TimeUnit.SECONDS.sleep(TIEMPO_COCINAR);
    }
    
}
