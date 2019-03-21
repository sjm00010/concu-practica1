package jimenezmorenosergioprac02;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import jimenezmorenosergioprac02.JimenezMorenoSergioPrac02.TipoCliente;

/**
 * @author Sergio Jiménez Moreno
 */
public class Cliente implements Callable<Integer>{
    // Constantes
    private final int TIEMPO_LLEGADA = 10;
    private final int MIN_PLATOS = 3;
    private final int MAX_PLATOS = 5;
    private final int MIN_PRECIO_PREMIUM = 10;
    private final int MAX_PRECIO_PREMIUM = 40;
    private final int MIN_PRECIO_NORMAL = 5;
    private final int MAX_PRECIO_NORMAL = 20;
    private final int MIN_TIEMPO_COMER = 2;
    private final int MAX_TIEMPO_COMER = 5;
    
    // Variables locales
    private int total;
    private int mesa;
    private int platosPedido;
    private Plato plato;
    
    private Random generador = new Random();
    
    // Variables de instancia
    private int id;
    private TipoCliente tipoCliente;
    private Atributos atributos;
    private ContadorClientes contador;
    private ArrayList<Pedido> listaPlatos;
    private ArrayList<Plato>[] pedidoCliente;
    
    // Semaforos
    private Semaphore exmRestaurante;
    private Semaphore semPremium;
    private Semaphore semNormal;
    private Semaphore exmCocina;
    private Semaphore semPedidos;
    private Semaphore semMesa;

    public Cliente( int id, TipoCliente tipoCliente, Atributos atributos,ContadorClientes contador, ArrayList<Pedido> listaPlatos, ArrayList<Plato>[] pedidoCliente, Semaphore exmRestaurante, Semaphore semPremium, Semaphore semNormal, Semaphore exmCocina, Semaphore semPedidos) {
        this.id = id;
        this.tipoCliente = tipoCliente;
        this.atributos = atributos;
        this.contador = contador;
        this.listaPlatos = listaPlatos;
        this.pedidoCliente = pedidoCliente;
        this.exmRestaurante = exmRestaurante;
        this.semPremium = semPremium;
        this.semNormal = semNormal;
        this.exmCocina = exmCocina;
        this.semPedidos = semPedidos;
        this.semMesa = new Semaphore(0);
        this.total = 0;
    }
    
    @Override
    public Integer call() {
        try {
            llegar();
            System.out.println("CLIENTE("+id+")-"+tipoCliente+"- Llegada al restaurante : "+new Date());
            entrarRestaurante();
            System.out.println("CLIENTE("+id+")-"+tipoCliente+"- Entrada al restaurante : "+new Date());
            hacerPedido();
            comerPedido();
            salirRestaurante();
            System.out.println("CLIENTE("+id+")- Salida del restaurante : "+new Date());
            contador.decrementaNumClientes();
        } catch (InterruptedException ex) {
            System.out.println("CLIENTE("+id+") - Se produjo un error, proceso interumpido.");
        }
        return total;
    }

    private void entrarRestaurante() throws InterruptedException{
        exmRestaurante.acquire();
        if(atributos.getMesasLibres() == 0){
            if(tipoCliente == TipoCliente.PREMIUM){
                atributos.incrementaEsperandoPremium();
                exmRestaurante.release();
                semPremium.acquire();
                atributos.decrementaEsperandoPremium();
            }else{
                atributos.incrementaEsperandoNormal();
                exmRestaurante.release();
                semNormal.acquire();
                atributos.decrementaEsperandoNormal();
            }
        }
        // sería exmRestaurante.wait();
        mesa=atributos.getMesasLibres();
        atributos.decrementaMesasLibres();
        System.out.println("DATO - Aforo : "+atributos.getMesasLibres());
        exmRestaurante.release();
    }
    
    private void hacerPedido() throws InterruptedException{
        platosPedido = generarNumPlatos();
        System.out.println("CLIENTE("+id+")- Precios de los "+platosPedido+" platos pedidos : ");
        for (int i = 0; i < platosPedido; i++) {
            plato = generarPlato();
            Pedido pedido = new Pedido(plato, semMesa, id);
            exmCocina.acquire();
            listaPlatos.add(pedido);
            exmCocina.release();
            semPedidos.release();
            System.out.print(plato.getPrecio()+"€ ");
        }
        System.out.println("");
    }
    
    private void comerPedido() throws InterruptedException{
        for (int i = 0; i < platosPedido; i++) {
            semMesa.acquire();
            plato = pedidoCliente[id].remove(0);
            //System.out.println("CLIENTE("+id+")- Come un plato de "+plato.getPrecio()+" €");
            total += plato.getPrecio();
            comer();
        }
    }
    
    private void salirRestaurante() throws InterruptedException{
        exmRestaurante.acquire();
        atributos.incrementaMesasLibres();
        if(atributos.getEsperandoPremium() > 0){
            semPremium.release();
        }else if (atributos.getEsperandoNormal() > 0){
            semNormal.release();
        }else{
            exmRestaurante.release();
        }
    }
    
    // Metodos auxiliares
    
    private void llegar() throws InterruptedException {
        TimeUnit.SECONDS.sleep(generador.nextInt(TIEMPO_LLEGADA));
    }
    
    private int generarNumPlatos(){
        return generador.nextInt((MAX_PLATOS-MIN_PLATOS))+MIN_PLATOS;
    }

    private Plato generarPlato() {
        Plato platoAleatorio;
        if(tipoCliente == TipoCliente.NORMAL){
            platoAleatorio = new Plato(generador.nextInt((MAX_PRECIO_NORMAL-MIN_PRECIO_NORMAL))+MIN_PRECIO_NORMAL);
        }else{
            platoAleatorio = new Plato(generador.nextInt((MAX_PRECIO_PREMIUM-MIN_PRECIO_PREMIUM))+MIN_PRECIO_PREMIUM);
        }
        return platoAleatorio;
    }

    private void comer() throws InterruptedException {
        TimeUnit.SECONDS.sleep(generador.nextInt((MAX_TIEMPO_COMER-MIN_TIEMPO_COMER))+MIN_TIEMPO_COMER);
    }

}
