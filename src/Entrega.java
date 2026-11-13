import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//Entrega de material que hace un aportante
public class Entrega {
    //Atributos
    private String codigoEntrega;
    private LocalDate fechaEntrega;
    private EstadoEntrega estado;
    private Aportante aportante;
    private List<Lote> listaLotes;
    private Comprobante comprobante;

    //Constructor: la entrega inicia ABIERTA, sin lotes y sin comprobante
    public Entrega(String codigoEntrega, LocalDate fechaEntrega, Aportante aportante) {
        this.codigoEntrega = codigoEntrega;
        this.fechaEntrega = fechaEntrega;
        this.aportante = aportante;

        this.estado = EstadoEntrega.ABIERTA;
        this.listaLotes = new ArrayList<>();
        this.comprobante = null;
    }

    //Getters
    public String getCodigoEntrega() {
        return codigoEntrega;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public EstadoEntrega getEstado() {
        return estado;
    }

    public Aportante getAportante() {
        return aportante;
    }

    //Devuelve una copia para proteger la lista original
    public List<Lote> getListaLotes() {
        return new ArrayList<>(listaLotes);
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    //Busca la posicion de un lote por su codigo, devuelve -1 si no existe
    public int buscarLote(String codigoLote) {
        for (int i = 0; i < listaLotes.size(); i++) {
            if (listaLotes.get(i).getCodigoLote().equals(codigoLote)) {
                return i;
            }
        }
        return -1;
    }

    //Agrega un lote si la entrega esta abierta, el peso es valido y el codigo no esta repetido
    public boolean agregarLote(Lote lote) {
        if (estado != EstadoEntrega.ABIERTA) {
            return false;
        }

        if (lote == null || !lote.esPesoValido()) {
            return false;
        }

        if (buscarLote(lote.getCodigoLote()) != -1) {
            return false;
        }

        listaLotes.add(lote);
        return true;
    }

    //Suma el peso de todos los lotes
    public int calcularPesoTotal() {
        int pesoTotal = 0;
        for (Lote lote : listaLotes) {
            pesoTotal += lote.getPesoKg();
        }
        return pesoTotal;
    }

    //Suma el valor de todos los lotes (polimorfismo)
    public double calcularValorTotal() {
        double valorTotal = 0;
        for (Lote lote : listaLotes) {
            valorTotal += lote.calcularValor();
        }
        return valorTotal;
    }

    //Acepta la entrega y genera el comprobante. Se rechaza si no esta abierta o esta vacia
    public boolean aceptar() {
        if (estado != EstadoEntrega.ABIERTA) {
            return false;
        }

        if (listaLotes.isEmpty()) {
            return false;
        }

        estado = EstadoEntrega.ACEPTADA;
        comprobante = new Comprobante(codigoEntrega, aportante.identificacionAportante(),
                calcularPesoTotal(), calcularValorTotal());
        return true;
    }

    //Devuelve las etiquetas de los lotes que implementan la interface
    public List<String> obtenerEtiquetas() {
        List<String> etiquetas = new ArrayList<>();
        for (Lote lote : listaLotes) {
            if (lote instanceof IGenerarEtiqueta) {
                IGenerarEtiqueta loteConEtiqueta = (IGenerarEtiqueta) lote;
                etiquetas.add(loteConEtiqueta.generarEtiqueta());
            }
        }
        return etiquetas;
    }
}

