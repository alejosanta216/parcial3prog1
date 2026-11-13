import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//Centro EcoRuta: administra los aportantes y las entregas
public class Empresa {
    //Atributos
    private String nit;
    private String nombreEmpresa;
    private List<Aportante> listaAportantes;
    private List<Entrega> listaEntregas;

    //Constructor
    public Empresa(String nit, String nombreEmpresa) {
        this.nit = nit;
        this.nombreEmpresa = nombreEmpresa;
        this.listaAportantes = new ArrayList<>();
        this.listaEntregas = new ArrayList<>();
    }

    //Getters
    public String getNit() {
        return nit;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    //Devuelven una copia para proteger las listas originales
    public List<Aportante> getListaAportantes() {
        return new ArrayList<>(listaAportantes);
    }

    public List<Entrega> getListaEntregas() {
        return new ArrayList<>(listaEntregas);
    }

    //Busca la posicion de un aportante por su identificacion, devuelve -1 si no existe
    public int buscarAportante(String identificacion) {
        for (int i = 0; i < listaAportantes.size(); i++) {
            if (listaAportantes.get(i).identificacionAportante().equals(identificacion)) {
                return i;
            }
        }
        return -1;
    }

    //Registra un aportante si su identificacion no esta repetida
    public boolean registrarAportante(Aportante aportante) {
        if (buscarAportante(aportante.identificacionAportante()) == -1) {
            listaAportantes.add(aportante);
            return true;
        }
        return false;
    }

    //Carga los aportantes con los que inicia la aplicacion
    public void iniciarAportantesRegistrados() {
        registrarAportante(new Aportante("1001", "Maria Lopez", "maria.lopez@correo.com"));
        registrarAportante(new Aportante("1002", "Tienda La Esquina", "laesquina@correo.com"));
        registrarAportante(new Aportante("1003", "Colegio San Jose", "colegiosanjose@correo.com"));
    }

    //Arma el texto con los datos de todos los aportantes
    public String mostrarAportantes() {
        String mensaje = "";
        for (Aportante aportante : listaAportantes) {
            mensaje += "\nIdentificacion: " + aportante.identificacionAportante() +
                    "\nNombre: " + aportante.nombreAportante() +
                    "\nCorreo: " + aportante.correoAportante() + "\n";
        }
        return mensaje;
    }

    //Busca la posicion de una entrega por su codigo, devuelve -1 si no existe
    public int buscarEntrega(String codigoEntrega) {
        for (int i = 0; i < listaEntregas.size(); i++) {
            if (listaEntregas.get(i).getCodigoEntrega().equals(codigoEntrega)) {
                return i;
            }
        }
        return -1;
    }

    //Devuelve la entrega con ese codigo, o null si no existe
    public Entrega obtenerEntrega(String codigoEntrega) {
        int indice = buscarEntrega(codigoEntrega);
        if (indice != -1) {
            return listaEntregas.get(indice);
        }
        return null;
    }

    //Registra una entrega. Se rechaza si el codigo esta repetido o el aportante no existe
    public boolean registrarEntrega(String codigoEntrega, LocalDate fecha, String identificacion) {
        if (buscarEntrega(codigoEntrega) != -1) {
            return false;
        }

        int indiceAportante = buscarAportante(identificacion);
        if (indiceAportante == -1) {
            return false;
        }

        Aportante aportante = listaAportantes.get(indiceAportante);
        listaEntregas.add(new Entrega(codigoEntrega, fecha, aportante));
        return true;
    }

    //Agrega un lote a una entrega existente
    public boolean agregarLoteAEntrega(String codigoEntrega, Lote lote) {
        int indice = buscarEntrega(codigoEntrega);
        if (indice == -1) {
            return false;
        }
        return listaEntregas.get(indice).agregarLote(lote);
    }

    //Acepta una entrega. Se rechaza si no existe, esta vacia o ya fue aceptada
    public boolean aceptarEntrega(String codigoEntrega) {
        int indice = buscarEntrega(codigoEntrega);
        if (indice == -1) {
            return false;
        }
        return listaEntregas.get(indice).aceptar();
    }

    //Devuelve el comprobante de una entrega, o null si no existe o no ha sido aceptada
    public Comprobante consultarComprobante(String codigoEntrega) {
        int indice = buscarEntrega(codigoEntrega);
        if (indice == -1) {
            return null;
        }
        return listaEntregas.get(indice).getComprobante();
    }

    //Devuelve las etiquetas de una entrega, o una lista vacia si no existe
    public List<String> consultarEtiquetas(String codigoEntrega) {
        int indice = buscarEntrega(codigoEntrega);
        if (indice == -1) {
            return new ArrayList<>();
        }
        return listaEntregas.get(indice).obtenerEtiquetas();
    }

    //Suma los comprobantes de las entregas aceptadas de un aportante. Devuelve 0 si no tiene
    public double calcularValorAcumuladoAportante(String identificacion) {
        double total = 0;
        for (Entrega entrega : listaEntregas) {
            Comprobante comprobante = entrega.getComprobante();
            if (comprobante != null && comprobante.identificacionAportante().equals(identificacion)) {
                total += comprobante.valorTotalReconocido();
            }
        }
        return total;
    }

    //Arma el texto con los datos de todas las entregas
    public String mostrarEntregas() {
        String mensaje = "";
        for (Entrega entrega : listaEntregas) {
            mensaje += "\nCodigo: " + entrega.getCodigoEntrega() +
                    "\nFecha: " + entrega.getFechaEntrega() +
                    "\nAportante: " + entrega.getAportante().nombreAportante() +
                    "\nEstado: " + entrega.getEstado() +
                    "\nLotes: " + entrega.getListaLotes().size() +
                    "\nPeso total: " + entrega.calcularPesoTotal() + " kg" +
                    "\nValor total: $" + entrega.calcularValorTotal() + "\n";
        }
        return mensaje;
    }
}
