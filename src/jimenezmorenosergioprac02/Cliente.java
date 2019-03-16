package jimenezmorenosergioprac02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import jimenezmorenosergioprac02.JimenezMorenoSergioPrac02.Atributos;
import jimenezmorenosergioprac02.JimenezMorenoSergioPrac02.TipoCliente;

/**
 * @author Sergio Jiménez Moreno
 */
public class Cliente implements Runnable{
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
    private int mesa;
    private int platosPedido;
    private Plato plato;
    
    private Random generador = new Random();
    
    // Variables de instancia
    private int id;
    private TipoCliente tipoCliente;
    private Atributos atributos;
    private ArrayList<Pedido> listaPlatos;
    private ArrayList<Plato>[] pedidoCliente;
    
    // Semaforos
    private Semaphore exmRestaurante;
    private Semaphore semPremium;
    private Semaphore semNormal;
    private Semaphore exmCocina;
    private Semaphore semPedidos;
    private Semaphore semMesa;

    public Cliente( int id, TipoCliente tipoCliente, Atributos atributos, ArrayList<Pedido> listaPlatos, ArrayList<Plato>[] pedidoCliente, Semaphore exmRestaurante, Semaphore semPremium, Semaphore semNormal, Semaphore exmCocina, Semaphore semPedidos) {
        this.id = id;
        this.tipoCliente = tipoCliente;
        this.atributos.esperandoPremium = atributos.esperandoPremium;
        this.atributos.esperandoNormal = atributos.esperandoNormal;
        this.atributos.mesasLibres = atributos.mesasLibres;
        this.listaPlatos = listaPlatos;
        this.pedidoCliente = pedidoCliente;
        this.exmRestaurante = exmRestaurante;
        this.semPremium = semPremium;
        this.semNormal = semNormal;
        this.exmCocina = exmCocina;
        this.semPedidos = semPedidos;
        this.semMesa = new Semaphore(0);
    }

    
    
    @Override
    public void run() {
        try {
            llegar();
            System.out.println("CLIENTE("+id+")- Llegada al restaurante : "+new Date());
            entrarRestaurante();
            System.out.println("CLIENTE("+id+")- Entrada al restaurante : "+new Date());
            hacerPedido();
            comerPedido();
            salirRestaurante();
            System.out.println("CLIENTE("+id+")- Salida del restaurante : "+new Date());
        } catch (InterruptedException ex) {
            System.out.println("CLIENTE("+id+") - Se produjo un error, proceso interumpido.");
        }
    }

    private void entrarRestaurante() throws InterruptedException{
        exmRestaurante.wait();
        if(atributos.mesasLibres == 0){
            if(tipoCliente == TipoCliente.PREMIUM){
                atributos.esperandoPremium++;
                exmRestaurante.notify();
                semPremium.wait();
                atributos.esperandoPremium--;
            }else{
                atributos.esperandoNormal++;
                exmRestaurante.notify();
                semNormal.wait();
                atributos.esperandoNormal--;
            }
        }
        // sería exmRestaurante.wait();
        mesa=atributos.mesasLibres;
        atributos.mesasLibres--;
        exmRestaurante.notify();
    }
    
    private void hacerPedido() throws InterruptedException{
        platosPedido = generarNumPlatos();
        for (int i = 0; i < platosPedido; i++) {
            plato = generarPlato();
            Pedido pedido = new Pedido(plato, semMesa, id);
            exmCocina.wait();
            listaPlatos.add(pedido);
            Collections.sort(listaPlatos);
            exmCocina.notify();
            semPedidos.notify();
        }
    }
    
    private void comerPedido() throws InterruptedException{
        for (int i = 0; i < platosPedido; i++) {
            semMesa.wait();
            plato = pedidoCliente[id].remove(0);
            System.out.println("CLIENTE("+id+")- Pidio un plato de "+plato.getPrecio()+" EUROS");
            comer();
        }
    }
    
    private void salirRestaurante() throws InterruptedException{
        exmRestaurante.wait();
        atributos.mesasLibres++;
        if(atributos.esperandoPremium > 0){
            semPremium.notify();
        }else if (atributos.esperandoNormal > 0){
            semNormal.notify();
        }else{
            exmRestaurante.notify();
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
