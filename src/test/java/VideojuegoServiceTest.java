import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class VideojuegoServiceTest {

    private VideojuegoRepository vr;
    private VideojuegoService vs;

    private VideojuegoService service = new VideojuegoService(null);

    @BeforeEach
    void setUp(){
        vr = mock(VideojuegoRepository.class);

        vs = new VideojuegoService(vr);
    }

    /**
     * Test para probar menor de 50
     */

    @Test
    void testClasificarJuego_LimiteInferiorMalo() {
        // Prueba del límite superior del rango Malo 0
        assertEquals("Malo", service.clasificarJuego(0));
    }
    @Test
    void testClasificarJuego_LimiteSuperiorMalo() {
        // Prueba del límite superior del rango Malo 49
        assertEquals("Malo", service.clasificarJuego(49));
    }
    @Test
    void testClasificarJuego_PuntuacionExacta50() {
        // Prueba del límite exacto 50 que falla como estado (Sin Clasificar)
        assertEquals("Bueno", service.clasificarJuego(50));
    }
    @Test
    void testClasificarJuego_LimiteInferiorBueno() {
        // Prueba del limite inferior Bueno 51
        assertEquals("Bueno", service.clasificarJuego(51));
    }
    @Test
    void testClasificarJuego_LimiteSuperiorBueno() {
        //Prueba del limite superior Bueno 89
        assertEquals("Bueno", service.clasificarJuego(89));
    }
    @Test
    void testClasificarJuego_LimiteInferiorObraMaestra() {
        // Prueba el límite inferior del rango Obra Maestra 90
        assertEquals("Obra Maestra", service.clasificarJuego(90));
    }
    @Test
    void testClasificarJuego_LimiteSuperiorObraMaestra() {
        // Prueba el límite superior del rango Obra Maestra 100
        assertEquals("Obra Maestra", service.clasificarJuego(100));
    }



}
