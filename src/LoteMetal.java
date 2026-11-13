

//Lote de metal (genera etiqueta)
public class LoteMetal extends Lote implements IGenerarEtiqueta {
    //Valores por kilogramo
    private static final double VALOR_KG_ALUMINIO = 2200;
    private static final double VALOR_KG_ACERO = 900;

    //Atributos
    private TipoMetal tipoMetal;

    //Constructor
    public LoteMetal(String codigoLote, String descripcionLote, int pesoKg, TipoMetal tipoMetal) {
        super(codigoLote, descripcionLote, pesoKg);
        this.tipoMetal = tipoMetal;
    }

    //Getters
    public TipoMetal getTipoMetal() {
        return tipoMetal;
    }

    //Calcula el valor: $2.200 por kg de aluminio o $900 por kg de acero
    @Override
    public double calcularValor() {
        if (tipoMetal == TipoMetal.ALUMINIO) {
            return pesoKg * VALOR_KG_ALUMINIO;
        }
        return pesoKg * VALOR_KG_ACERO;
    }

    //Etiqueta con el codigo del lote y el tipo de metal
    @Override
    public String generarEtiqueta() {
        return "Lote: " + codigoLote + " - Metal: " + tipoMetal;
    }
}
