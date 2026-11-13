import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

//Clase principal con el menu interactivo
public class Main {

    private static Empresa empresa;

    public static void main(String[] args) {
        empresa = new Empresa("900123456-7", "EcoRuta");
        empresa.iniciarAportantesRegistrados();

        int opcion = 0;
        do {
            opcion = leerEntero("Sistema EcoRuta:" +
                    "\nSeleccione una opcion:" +
                    "\n1. Mostrar aportantes registrados" +
                    "\n2. Registrar entrega" +
                    "\n3. Agregar lote a una entrega" +
                    "\n4. Aceptar entrega" +
                    "\n5. Consultar comprobante de una entrega" +
                    "\n6. Consultar etiquetas de una entrega" +
                    "\n7. Consultar valor acumulado de un aportante" +
                    "\n8. Mostrar entregas" +
                    "\n0. Salir del sistema");

            switch (opcion) {

                case 1:
                    mostrarAportantes();
                    break;

                case 2:
                    registrarEntrega();
                    break;

                case 3:
                    agregarLote();
                    break;

                case 4:
                    aceptarEntrega();
                    break;

                case 5:
                    consultarComprobante();
                    break;

                case 6:
                    consultarEtiquetas();
                    break;

                case 7:
                    consultarValorAcumulado();
                    break;

                case 8:
                    mostrarEntregas();
                    break;

                case 0:
                    JOptionPane.showMessageDialog(null, "El programa finalizo");
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "La opcion no es valida");
                    break;
            }

        } while (opcion != 0);
    }

    //Pide un texto. Si se cancela devuelve un texto vacio
    private static String leerTexto(String mensaje) {
        String texto = JOptionPane.showInputDialog(mensaje);
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }

    //Pide un numero entero. Si no es un numero devuelve -1
    private static int leerEntero(String mensaje) {
        try {
            return Integer.parseInt(leerTexto(mensaje));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    //Muestra los aportantes registrados
    private static void mostrarAportantes() {
        JOptionPane.showMessageDialog(null, "Aportantes registrados:\n" + empresa.mostrarAportantes());
    }

    //Registra una entrega con la fecha actual
    private static void registrarEntrega() {
        String codigoEntrega = leerTexto("Ingrese el codigo de la entrega:");
        String identificacion = leerTexto("Ingrese la identificacion del aportante:");

        boolean resultado = empresa.registrarEntrega(codigoEntrega, LocalDate.now(), identificacion);

        if (resultado) {
            JOptionPane.showMessageDialog(null, "Registro exitoso. La entrega queda ABIERTA y sin lotes");
        } else {
            JOptionPane.showMessageDialog(null, "No se hizo el registro: el codigo ya existe " +
                    "o el aportante no esta registrado");
        }
    }

    //Crea el lote segun el tipo seleccionado y lo agrega a la entrega
    private static void agregarLote() {
        String codigoEntrega = leerTexto("Ingrese el codigo de la entrega:");
        if (empresa.obtenerEntrega(codigoEntrega) == null) {
            JOptionPane.showMessageDialog(null, "La entrega no existe");
            return;
        }

        int tipo = leerEntero("Seleccione el tipo de lote:" +
                "\n1. Papel y carton" +
                "\n2. Metal" +
                "\n3. Residuos electronicos");
        if (tipo < 1 || tipo > 3) {
            JOptionPane.showMessageDialog(null, "El tipo de lote no es valido");
            return;
        }

        String codigoLote = leerTexto("Ingrese el codigo del lote:");
        String descripcion = leerTexto("Ingrese la descripcion del lote:");
        int pesoKg = leerEntero("Ingrese el peso en kilogramos (entero mayor a 0):");

        Lote lote = null;

        switch (tipo) {

            case 1:
                int opcionClasificado = leerEntero("El material esta clasificado:" +
                        "\n1. Si" +
                        "\n2. No");
                lote = new LotePapelCarton(codigoLote, descripcion, pesoKg, opcionClasificado == 1);
                break;

            case 2:
                int opcionMetal = leerEntero("Seleccione el tipo de metal:" +
                        "\n1. Aluminio" +
                        "\n2. Acero");
                if (opcionMetal == 1) {
                    lote = new LoteMetal(codigoLote, descripcion, pesoKg, TipoMetal.ALUMINIO);
                } else if (opcionMetal == 2) {
                    lote = new LoteMetal(codigoLote, descripcion, pesoKg, TipoMetal.ACERO);
                } else {
                    JOptionPane.showMessageDialog(null, "El tipo de metal no es valido");
                    return;
                }
                break;

            case 3:
                int cantidadEquipos = leerEntero("Ingrese la cantidad de equipos completos (0 o mas):");
                if (cantidadEquipos < 0) {
                    JOptionPane.showMessageDialog(null, "La cantidad de equipos no es valida");
                    return;
                }
                lote = new LoteResiduoElectronico(codigoLote, descripcion, pesoKg, cantidadEquipos);
                break;
        }

        boolean resultado = empresa.agregarLoteAEntrega(codigoEntrega, lote);

        if (resultado) {
            JOptionPane.showMessageDialog(null, "Lote agregado. Valor del lote: $" + lote.calcularValor());
        } else {
            JOptionPane.showMessageDialog(null, "No se agrego el lote: revise que la entrega este ABIERTA, " +
                    "que el peso sea mayor a 0 y que el codigo del lote no este repetido");
        }
    }

    //Acepta una entrega y muestra su comprobante
    private static void aceptarEntrega() {
        String codigoEntrega = leerTexto("Ingrese el codigo de la entrega a aceptar:");

        boolean resultado = empresa.aceptarEntrega(codigoEntrega);

        if (resultado) {
            JOptionPane.showMessageDialog(null, "Entrega ACEPTADA\n" +
                    textoComprobante(empresa.consultarComprobante(codigoEntrega)));
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo aceptar: la entrega no existe, " +
                    "esta vacia o ya fue aceptada");
        }
    }

    //Consulta el comprobante de una entrega por su codigo
    private static void consultarComprobante() {
        String codigoEntrega = leerTexto("Ingrese el codigo de la entrega:");
        Comprobante comprobante = empresa.consultarComprobante(codigoEntrega);

        if (comprobante == null) {
            JOptionPane.showMessageDialog(null, "No hay comprobante: la entrega no existe " +
                    "o todavia no ha sido aceptada");
        } else {
            JOptionPane.showMessageDialog(null, textoComprobante(comprobante));
        }
    }

    //Consulta las etiquetas de una entrega por su codigo
    private static void consultarEtiquetas() {
        String codigoEntrega = leerTexto("Ingrese el codigo de la entrega:");
        List<String> etiquetas = empresa.consultarEtiquetas(codigoEntrega);

        if (etiquetas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay etiquetas: la entrega no existe " +
                    "o no tiene lotes de metal ni de residuos electronicos");
            return;
        }

        String mensaje = "Etiquetas de la entrega " + codigoEntrega + ":\n";
        for (String etiqueta : etiquetas) {
            mensaje += "\n" + etiqueta;
        }
        JOptionPane.showMessageDialog(null, mensaje);
    }

    //Consulta el valor acumulado de un aportante
    private static void consultarValorAcumulado() {
        String identificacion = leerTexto("Ingrese la identificacion del aportante:");
        double valor = empresa.calcularValorAcumuladoAportante(identificacion);
        JOptionPane.showMessageDialog(null, "Valor acumulado del aportante " + identificacion +
                ": $" + valor);
    }

    //Muestra todas las entregas registradas
    private static void mostrarEntregas() {
        JOptionPane.showMessageDialog(null, "Entregas registradas:\n" + empresa.mostrarEntregas());
    }

    //Arma el texto del comprobante
    private static String textoComprobante(Comprobante comprobante) {
        return "Comprobante:" +
                "\nCodigo de entrega: " + comprobante.codigoEntrega() +
                "\nIdentificacion del aportante: " + comprobante.identificacionAportante() +
                "\nPeso total: " + comprobante.pesoTotalKg() + " kg" +
                "\nValor total reconocido: $" + comprobante.valorTotalReconocido();
    }
}
