package com.tienda.enums;

public enum EstadoPedido {
    EN_PREPARACION,
    PENDIENTE,
    ENTREGADO,
    CANCELADO;

    public boolean puedeCambiarA(EstadoPedido nuevoEstado, TipoVenta tipoVenta) {

        // Nunca se sale de estos estados
        if (this == ENTREGADO || this == CANCELADO) {
            return false;
        }

        if (tipoVenta == TipoVenta.LOCAL) {
            return switch (this) {
                case PENDIENTE ->
                        nuevoEstado == ENTREGADO || nuevoEstado == CANCELADO;
                default -> false;
            };
        }

        if (tipoVenta == TipoVenta.ENVIO) {
            return switch (this) {
                case PENDIENTE ->
                        nuevoEstado == EN_PREPARACION || nuevoEstado == CANCELADO;
                case EN_PREPARACION ->
                        nuevoEstado == ENTREGADO || nuevoEstado == CANCELADO;
                default -> false;
            };
        }
        return false;
    }
}
