package com.speedfast.concurrencia;

import com.speedfast.modelo.EstadoPedido;
import com.speedfast.modelo.Pedido;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ZonaDeCarga {

    private final BlockingQueue<Pedido> pedidosPendientes = new LinkedBlockingQueue<>();

    public synchronized void agregarPedido(Pedido p) {
        pedidosPendientes.offer(p);
        System.out.printf ("[Zona de Carga] Pedido #%d registrado con destino a %s%n",
                p.getId(), p.getDireccionEntrega());
    }

    public synchronized Pedido retirarPedido() {
        Pedido pedido = pedidosPendientes.poll();
        if (pedido != null) {
            pedido.setEstado(EstadoPedido.EN_REPARTO);
        }
        return pedido;
    }

    public synchronized boolean hayPedidosPendientes() {
        return !pedidosPendientes.isEmpty();
    }

    public synchronized int cantidadPendientes() {
        return pedidosPendientes.size();
    }
}
