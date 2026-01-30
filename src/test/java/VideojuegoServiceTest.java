import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

class VideojuegoServiceTest {

    private VideojuegoRepository vr;
    private VideojuegoService vs;

    @BeforeEach
    void setUp() {
        // Simulación del repositorio (Mock)
        vr = Mockito.mock(VideojuegoRepository.class);
        vs = new VideojuegoService(vr);
    }

    // --- 1. TESTS DE CLASIFICACIÓN (LÍMITES) ---

    @Test
    void clasificarJuego_limites() {
        // Rango Malo (0-49)
        assertEquals("Malo", vs.clasificarJuego(0));
        assertEquals("Malo", vs.clasificarJuego(49));

        // Rango Bueno (50-89) -> Aquí estaba el BUG-001 original
        assertEquals("Bueno", vs.clasificarJuego(50));
        assertEquals("Bueno", vs.clasificarJuego(89));

        // Rango Obra Maestra (90-100)
        assertEquals("Obra Maestra", vs.clasificarJuego(90));
        assertEquals("Obra Maestra", vs.clasificarJuego(100));
    }

    @Test
    void clasificarJuego_fueraDeRango() {
        assertThrows(IllegalArgumentException.class, () -> vs.clasificarJuego(101));
        assertThrows(IllegalArgumentException.class, () -> vs.clasificarJuego(-1));
    }

    // --- 2. TESTS PARAMETRIZADOS ---

    @ParameterizedTest
    @CsvSource({
            "PC, 60, true",    // > 50h es largo
            "PC, 40, false",   // < 50h no es largo
            "Movil, 25, true", // > 20h en Movil es largo
            "Movil, 20, false" // <= 20h en Movil no es largo
    })
    void esJuegoLargo_parametrizado(String plataforma, int horas, boolean esperado) {
        Videojuego juego = new Videojuego("Test Game", plataforma, horas, 80);
        assertEquals(esperado, vs.esJuegoLargo(juego));
    }

    // --- 3. GESTIÓN DE EXCEPCIONES Y VALIDACIÓN ---

    @Test
    void registrarJuego_tituloNuloOVacio() {
        assertThrows(IllegalArgumentException.class, () ->
                vs.registrarJuego(null, "PC", 10, 80)
        );
        assertThrows(IllegalArgumentException.class, () ->
                vs.registrarJuego("", "PC", 10, 80)
        );
    }

    @Test
    void registrarJuego_horasNegativas() {
        assertThrows(IllegalArgumentException.class, () ->
                vs.registrarJuego("Sonic", "PC", -5, 80)
        );
    }

    // --- 4. SIMULACIÓN CON MOCKS (VERIFY & STUB) ---

    @Test
    void registrarJuego_valido_verifyGuardar() {
        // Ejecución
        vs.registrarJuego("Zelda", "Switch", 80, 95);

        // Verificación: Aseguramos que el mock llamó al método guardar 1 vez
        verify(vr, times(1)).guardar(any(Videojuego.class));
    }

    @Test
    void stub_buscarEldenRing() {
        // Configuración del Stub
        Videojuego eldenRing = new Videojuego("Elden Ring", "PC", 120, 95);
        when(vr.buscarPorTitulo("Elden Ring")).thenReturn(eldenRing);

        // Ejecución
        Videojuego resultado = vr.buscarPorTitulo("Elden Ring");

        // Comprobación
        assertNotNull(resultado);
        assertEquals("Elden Ring", resultado.getTitulo());
        assertEquals(120, resultado.getHorasJugadas());
    }
}