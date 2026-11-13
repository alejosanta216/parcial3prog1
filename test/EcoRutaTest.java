import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

//Pruebas funcionales del parcial
class EcoRutaTest {
    private LocalDate hoy = LocalDate.now();

    //Crea la empresa con los aportantes iniciales (1001, 1002, 1003)
    private Empresa empresa() {
        Empresa empresa = new Empresa("900123456-7", "EcoRuta");
        empresa.iniciarAportantesRegistrados();
        return empresa;
    }

    //Prueba 1: registro de entrega, rechazo de codigo repetido y de aportante desconocido
    @Test
    void registroDeEntregaYRechazos() {
        Empresa empresa = empresa();

        assertTrue(empresa.registrarEntrega("E01", hoy, "1001"));
        Entrega entrega = empresa.obtenerEntrega("E01");
        assertNotNull(entrega);
        assertEquals(EstadoEntrega.ABIERTA, entrega.getEstado());
        assertTrue(entrega.getListaLotes().isEmpty());
        assertNull(entrega.getComprobante());

        assertFalse(empresa.registrarEntrega("E01", hoy, "1002"));
        assertFalse(empresa.registrarEntrega("E02", hoy, "9999"));

        assertEquals(1, empresa.getListaEntregas().size());
        assertSame(entrega, empresa.obtenerEntrega("E01"));
        assertEquals("1001", entrega.getAportante().identificacionAportante());
    }

    //Prueba 2: validacion de lotes (codigo repetido y peso cero)
    @Test
    void validacionDeLotes() {
        Empresa empresa = empresa();
        empresa.registrarEntrega("E01", hoy, "1001");
        Lote lote = new LotePapelCarton("L01", "Carton", 5, false);

        assertTrue(empresa.agregarLoteAEntrega("E01", lote));
        assertFalse(empresa.agregarLoteAEntrega("E01", new LoteMetal("L01", "Latas", 3, TipoMetal.ACERO)));
        assertFalse(empresa.agregarLoteAEntrega("E01", new LotePapelCarton("L02", "Papel", 0, true)));

        List<Lote> lotes = empresa.obtenerEntrega("E01").getListaLotes();
        assertEquals(1, lotes.size());
        assertSame(lote, lotes.get(0));
    }

    //Prueba 3: no se puede aceptar una entrega vacia
    @Test
    void rechazoDeEntregaVacia() {
        Empresa empresa = empresa();
        empresa.registrarEntrega("E01", hoy, "1001");

        assertFalse(empresa.aceptarEntrega("E01"));
        assertEquals(EstadoEntrega.ABIERTA, empresa.obtenerEntrega("E01").getEstado());
        assertNull(empresa.consultarComprobante("E01"));
    }

    //Prueba 4: aceptacion con los tres tipos de lote
    @Test
    void aceptacionConTresTiposDeLote() {
        Empresa empresa = empresa();
        empresa.registrarEntrega("E01", hoy, "1001");
        Lote papel = new LotePapelCarton("L01", "Carton clasificado", 10, true);
        Lote aluminio = new LoteMetal("L02", "Latas de aluminio", 5, TipoMetal.ALUMINIO);
        Lote electronico = new LoteResiduoElectronico("L03", "Computadores", 4, 2);
        empresa.agregarLoteAEntrega("E01", papel);
        empresa.agregarLoteAEntrega("E01", aluminio);
        empresa.agregarLoteAEntrega("E01", electronico);

        //Cada lote calcula su valor a su manera (polimorfismo)
        double[] valoresEsperados = {6000, 11000, 7200};
        double[] valoresObtenidos = {papel.calcularValor(), aluminio.calcularValor(), electronico.calcularValor()};
        assertArrayEquals(valoresEsperados, valoresObtenidos, 0.001);

        assertTrue(empresa.aceptarEntrega("E01"));
        assertEquals(EstadoEntrega.ACEPTADA, empresa.obtenerEntrega("E01").getEstado());

        Comprobante comprobante = empresa.consultarComprobante("E01");
        assertNotNull(comprobante);
        assertEquals("E01", comprobante.codigoEntrega());
        assertEquals("1001", comprobante.identificacionAportante());
        assertEquals(19, comprobante.pesoTotalKg());
        assertEquals(24200, comprobante.valorTotalReconocido(), 0.001);
    }

    //Prueba 5: etiquetas solo de metal y residuos electronicos
    @Test
    void consultaDeEtiquetas() {
        Empresa empresa = empresa();
        empresa.registrarEntrega("E01", hoy, "1001");
        empresa.agregarLoteAEntrega("E01", new LotePapelCarton("L01", "Carton clasificado", 10, true));
        empresa.agregarLoteAEntrega("E01", new LoteMetal("L02", "Latas de aluminio", 5, TipoMetal.ALUMINIO));
        empresa.agregarLoteAEntrega("E01", new LoteResiduoElectronico("L03", "Computadores", 4, 2));
        empresa.aceptarEntrega("E01");

        List<String> etiquetas = empresa.consultarEtiquetas("E01");

        assertEquals(2, etiquetas.size());
        assertIterableEquals(List.of("Lote: L02 - Metal: ALUMINIO", "Lote: L03 - Equipos completos: 2"), etiquetas);
        assertLinesMatch(List.of(".*L02.*ALUMINIO.*", ".*L03.*2.*"), etiquetas);
        assertNotSame(etiquetas, empresa.consultarEtiquetas("E01"));
    }
}
