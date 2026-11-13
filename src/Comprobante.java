//Comprobante de una entrega aceptada. Record: sus datos no se modifican
public record Comprobante(String codigoEntrega, String identificacionAportante,
                          int pesoTotalKg, double valorTotalReconocido) {
}