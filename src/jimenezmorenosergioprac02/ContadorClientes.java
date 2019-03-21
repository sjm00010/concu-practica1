/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jimenezmorenosergioprac02;

import java.util.concurrent.Semaphore;

/**
 *
 * @author PORTATIL
 */
public class ContadorClientes {
    Semaphore exmNumClientes;
    private int numClientes;

    public ContadorClientes(int numClientes) {
        this.numClientes = numClientes;
        this.exmNumClientes = new Semaphore(1);
    }

    public int getNumClientes() {
        System.out.println("DATO - Numero de clientes : "+ numClientes);
        return numClientes;
    }

    public void decrementaNumClientes() throws InterruptedException {
        exmNumClientes.acquire();
        this.numClientes--;
        exmNumClientes.release();
    }
    
    
}
