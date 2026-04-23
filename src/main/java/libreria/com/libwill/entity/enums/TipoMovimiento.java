package libreria.com.libwill.entity.enums;

public enum TipoMovimiento {
    INGRESO,           // Cuando se agrega stock
    VENTA_ONLINE,      // Descuento por pedido online
    VENTA_FISICA,      // Descuento por venta física
    AJUSTE             // Ajuste manual (corrección)
}