package com.speedfast;

import com.speedfast.concurrencia.Repartidor;
import com.speedfast.concurrencia.ZonaDeCarga;
import com.speedfast.modelo.Pedido;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Punto de entrada de la simulación "Coordinación de entregas en SpeedFast".
 *
 * <p>Crea la zona de carga compartida, registra los pedidos e inicia varios
 * repartidores que trabajan en paralelo mediante un {@link ExecutorService}.
 * Gracias a la sincronización de la zona de carga, cada pedido es atendido por
 * un único repartidor, evitando entregas duplicadas.</p>
 */
public class Main {

    private static final int CANTIDAD_REPARTIDORES = 3;

    public static void main(String[] args) throws InterruptedException {

        System.out.println("===== SpeedFast: Sincronizando procesos en sistemas concurrentes =====\n");

        // 1. Se crea el recurso compartido.
        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();

        // 2. Se agregan al menos 5 pedidos al sistema.
        zonaDeCarga.agregarPedido(new Pedido(1, "Av. Providencia 1234, Santiago"));
        zonaDeCarga.agregarPedido(new Pedido(2, "Calle Los Aromos 567, Viña del Mar"));
        zonaDeCarga.agregarPedido(new Pedido(3, "Pasaje El Sol 89, Concepción"));
        zonaDeCarga.agregarPedido(new Pedido(4, "Av. Alemania 4321, Temuco"));
        zonaDeCarga.agregarPedido(new Pedido(5, "Camino Real 100, La Serena"));
        zonaDeCarga.agregarPedido(new Pedido(6, "Av. España 2020, Valparaíso"));

        System.out.printf("%nTotal de pedidos en la zona de carga: %d%n",
                zonaDeCarga.cantidadPendientes());
        System.out.println("\n----- Inicia el despacho concurrente -----\n");

        // 3. Se crean e inician los hilos de tipo Repartidor usando ExecutorService.
        ExecutorService flota = Executors.newFixedThreadPool(CANTIDAD_REPARTIDORES);
        for (int i = 1; i <= CANTIDAD_REPARTIDORES; i++) {
            flota.execute(new Repartidor("Repartidor-" + i, zonaDeCarga));
        }

        // 4. Se ordena el cierre ordenado y se espera la finalización del proceso.
        flota.shutdown();
        boolean finalizoATiempo = flota.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println();
        if (finalizoATiempo) {
            System.out.println("Todos los pedidos han sido entregados correctamente.");
        } else {
            System.out.println("Advertencia: el proceso no finalizó dentro del tiempo esperado.");
        }
    }
}
