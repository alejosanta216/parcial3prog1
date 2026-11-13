
//Lote de papel y carton (no genera etiqueta)
public class LotePapelCarton extends Lote {
    //Valores por kilogramo
    private static final double VALOR_KG = 500;
    private static final double ADICIONAL_KG_CLASIFICADO = 100;

    //Atributos
    private boolean clasificado;

    //Constructor
    public LotePapelCarton(String codigoLote, String descripcionLote, int pesoKg, boolean clasificado) {
        super(codigoLote, descripcionLote, pesoKg);
        this.clasificado = clasificado;
    }

    //Getters
    public boolean isClasificado() {
        return clasificado;
    }

    //Calcula el valor: $500 por kg, mas $100 por kg si esta clasificado
    @Override
    public double calcularValor() {
        double valor = pesoKg * VALOR_KG;
        if (clasificado) {
            valor += pesoKg * ADICIONAL_KG_CLASIFICADO;
        }
        return valor;
    }
}