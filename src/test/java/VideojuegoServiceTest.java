import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;



public class VideojuegoServiceTest {

    /**
     * Mock del repositorio usado para simular las operaciones de persistencia.
     */
    private VideojuegoRepository vr;


    private VideojuegoService vs;

    /**
     * Inicializa el mock y la instancia del servicio antes de cada test.
     * Se ejecuta antes de cada método de prueba para asegurar un estado limpio y evitar
     * dependencias entre tests.
     *
     */
    @BeforeEach
    void setUp() {
        vr = Mockito.mock(VideojuegoRepository.class);
        vs = new VideojuegoService(vr);
    }

    /**
     * Verifica la clasificación de un juego en los límites de los rangos esperados.
     *
     * <p>
     * Casos comprobados:
     * <ul>
     *   <li>0 y 49 -> "Malo"</li>
     *   <li>50 y 89 -> "Bueno"</li>
     *   <li>90 -> "Obra Maestra"</li>
     * </ul>
     * </p>
     */
    @Test
    void clasificarJuego_limites() {
        assertEquals("Malo", vs.clasificarJuego(0));
        assertEquals("Malo", vs.clasificarJuego(49));
        assertEquals("Bueno", vs.clasificarJuego(50));
        assertEquals("Bueno", vs.clasificarJuego(89));
        assertEquals("Obra Maestra", vs.clasificarJuego(90));
    }

    /**
     * Comprueba que una puntuación fuera del rango válido provoca una excepción.
     *
     * <p>
     * Aquí se prueba con el valor 120 y se espera que se lance
     * {@link IllegalArgumentException}.
     * </p>
     *
     * @throws IllegalArgumentException si la puntuación está fuera del rango permitido
     */
    @Test
    void clasificarJuego_fueraDeRango() {
        assertThrows(IllegalArgumentException.class, () -> vs.clasificarJuego(120));
    }

    /**
     * Test parametrizado para {@link VideojuegoService#esJuegoLargo(Videojuego)}.
     *
     * <p>
     * Se reciben diferentes combinaciones de plataforma y horas desde {@link CsvSource}
     * y se compara el resultado con el valor esperado.
     * </p>
     *
     * @param plataforma la plataforma del juego (por ejemplo, "PC", "Movil")
     * @param horas      la duración en horas que se usará para construir el {@link Videojuego}
     * @param esperado   valor esperado (true si se considera juego largo, false en caso contrario)
     */
    @ParameterizedTest
    @CsvSource({
            "PC, 60, true",
            "PC, 40, false",
            "Movil, 25, true",
            "Movil, 20, false"
    })
    void esJuegoLargo_parametrizado(String plataforma, int horas, boolean esperado) {
        Videojuego juego = new Videojuego("Test", plataforma, horas, 80);
        assertEquals(esperado, vs.esJuegoLargo(juego));
    }

    /**
     * Verifica que intentar registrar un juego con título nulo lanza
     * {@link IllegalArgumentException}.
     */
    @Test
    void registrarJuego_tituloNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                vs.registrarJuego(null, "PC", 10, 80)
        );
    }

    /**
     * Verifica que intentar registrar un juego con horas negativas lanza
     * {@link IllegalArgumentException}.
     */
    @Test
    void registrarJuego_conTituloYHorasNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                vs.registrarJuego("Sonic", "PC", -20, 80)
        );
    }

    /**
     * Verifica que intentar registrar un juego con título vacío lanza
     * {@link IllegalArgumentException}.
     */
    @Test
    void registrarJuego_tituloVacio() {
        assertThrows(IllegalArgumentException.class, () ->
                vs.registrarJuego("", "PC", 10, 80)
        );
    }

    /**
     * Comprueba el flujo feliz de registrar un juego válido.
     *
     * <p>
     * Se registra un juego con datos válidos y se verifica que el repositorio reciba
     * una llamada a {@code guardar} exactamente una vez con cualquier instancia de
     * {@link Videojuego}.
     * </p>
     */
    @Test
    void registrarJuego_valido_verifyGuardar() {
        vs.registrarJuego("Zelda", "Switch", 80, 95);
        verify(vr, times(1)).guardar(any(Videojuego.class));
    }

    /**
     * Ejemplo de stub: cuando el repositorio devuelve un videojuego concreto para
     * el título "Elden Ring", se comprueba que el objeto devuelto no es nulo y que
     * su título coincide con el solicitado.
     *
     * <p>
     * Este test muestra cómo configurar el comportamiento del mock mediante
     * {@link org.mockito.Mockito#when(Object)} y cómo consumir ese stub.
     * </p>
     */
    @Test
    void stub_buscarEldenRing() {
        Videojuego eldenRing = new Videojuego("Elden Ring", "PC", 120, 95);
        when(vr.buscarPorTitulo("Elden Ring")).thenReturn(eldenRing);

        Videojuego resultado = vr.buscarPorTitulo("Elden Ring");

        assertNotNull(resultado);
        assertEquals("Elden Ring", resultado.getTitulo());
    }
}