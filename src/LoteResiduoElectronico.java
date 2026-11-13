

//Lote de residuos electronicos (genera etiqueta)
public class LoteResiduoElectronico extends Lote implements IGenerarEtiqueta {
    //Valores reconocidos
    private static final double VALOR_KG = 1400;
    private static final double VALOR_EQUIPO_COMPLETO = 800;

    //Atributos
    private int cantidadEquiposCompletos;

    //Constructor
    public LoteResiduoElectronico(String codigoLote, String descripcionLote, int pesoKg,
                                  int cantidadEquiposCompletos) {
        super(codigoLote, descripcionLote, pesoKg);
        this.cantidadEquiposCompletos = cantidadEquiposCompletos;
    }

    //Getters
    public int getCantidadEquiposCompletos() {
        return cantidadEquiposCompletos;
    }

    //Calcula el valor: $1.400 por kg, mas $800 por cada equipo completo
    @Override
    public double calcularValor() {
        return (pesoKg * VALOR_KG) + (cantidadEquiposCompletos * VALOR_EQUIPO_COMPLETO);
    }

    //Etiqueta con el codigo del lote y la cantidad de equipos completos
    @Override
    public String generarEtiqueta() {
        return "Lote: " + codigoLote + " - Equipos completos: " + cantidadEquiposCompletos;
    }
}
