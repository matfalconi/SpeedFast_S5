package com.speedfast.concurrencia;

import com.speedfast.modelo.EstadoPedido;
import com.speedfast.modelo.Pedido;

import java.util.concurrent.ThreadLocalRandom;

public class Repartidor implements Runnable {

    private final String nombre;
    private final ZonaDeCarga zonaDeCarga;

    /**
     * @param nombre
     * @param ZonaDeCarga
     */
    public Repartidor(String nombre, ZonaDeCarga zonaDeCarga) {
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Ejecutacion por hilo. Mientras existan pedidos, retira uno y se coloca en {@code EN_REPARTO},
     * se simula la entrega con {@link Thread#sleep(long)} y finalmente lo marca como {@code ENTREGADO}
     */

    @Override
    public void run() {
        //Se trabaja mientras se tengan pedidos disponibles
        while (zonaDeCarga.hayPedidosPendientes()) {

            Pedido pedido = zonaDeCarga.retirarPedido();
            if (pedido == null) {
                break;
            }

            System.out.printf("[%s] Retiró el pedido #%d (%s) -> estado: %s%n",
                    nombre,pedido.getId(), pedido.getDireccionEntrega(),pedido.getEstado());

            try {
                // Simula tiempo de traslado y entrega
                int tiempoEntrega = ThreadLocalRandom.current().nextInt(1000, 3001);
                Thread.sleep(tiempoEntrega);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.printf("[%s] Entrega del pedido #%d interrumpida.%n",
                        nombre, pedido.getId());
                return;
            }

            pedido.setEstado(EstadoPedido.ENTREGADO);
            System.out.printf("[%s] Entregó el pedido #%d en %s -> estado: %s%n",
                    nombre, pedido.getId(), pedido.getDireccionEntrega(), pedido.getEstado());
        }

        System.out.printf("[%s] No quedan pedidos disponibles. Finaliza su jornada.%n", nombre);
    }
}
