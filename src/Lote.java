

//Clase padre abstracta de todos los lotes
public abstract class Lote {
    //Atributos
    protected String codigoLote;
    protected String descripcionLote;
    protected int pesoKg;

    //Constructor
    public Lote(String codigoLote, String descripcionLote, int pesoKg) {
        this.codigoLote = codigoLote;
        this.descripcionLote = descripcionLote;
        this.pesoKg = pesoKg;
    }

    //Getters
    public String getCodigoLote() {
        return codigoLote;
    }

    public String getDescripcionLote() {
        return descripcionLote;
    }

    public int getPesoKg() {
        return pesoKg;
    }

    //Valida que el peso sea mayor a cero
    public boolean esPesoValido() {
        return pesoKg > 0;
    }

    //Metodo abstracto: cada tipo de lote calcula su valor
    public abstract double calcularValor();
}
