package jimenezmorenosergioprac02;

import java.util.concurrent.Semaphore;

/**
 * @author Sergio Jiménez Moreno
 */
public class Pedido implements Comparable<Pedido>{
    // Variables
    private Plato plato;
    private Semaphore semMesa;
    private int idCliente;

    public Pedido(Plato plato, Semaphore semMesa, int idCliente) {
        this.plato = plato;
        this.semMesa = semMesa;
        this.idCliente = idCliente;
    }

    @Override
    public int compareTo(Pedido o) {
        int resultado;
        if(o.plato.getPrecio() < this.plato.getPrecio()){
            resultado = -1;
        }else if(o.plato.getPrecio() > this.plato.getPrecio()){
            resultado = 1;
        }else{
            resultado = 0;
        }
        return resultado;
    }

    public Semaphore getSemMesa() {
        return semMesa;
    }

    public Plato getPlato() {
        return plato;
    }

    public int getIdCliente() {
        return idCliente;
    }
    
}
