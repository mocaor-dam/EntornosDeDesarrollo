import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

/**
 * Pruebas unitarias para {@link VideojuegoService} usando JUnit 5 y Mockito.
 *
 * <p>
 * Esta clase crea un mock de {@link VideojuegoRepository} y verifica el comportamiento
 * público del servicio {@link VideojuegoService} en distintos escenarios:
 * clasificación por puntuación, validaciones de entrada, registro de juegos y uso de stubs.
 * </p>
 */
public class VideojuegoServiceTest {

    /**
     * Mock del repositorio usado para simular las operaciones de persistencia.
     *
     * @see VideojuegoRepository
     */
    private VideojuegoRepository repositoryMock;

    /**
     * Instancia del servicio bajo prueba. Se inyecta el {@link #repositoryMock} para
     * aislar la lógica de negocio de la capa de datos.
     */
    private VideojuegoService service;

    /**
     * Inicializa el mock y la instancia del servicio antes de cada test.
     *
     * <p>
     * Se ejecuta antes de cada método de prueba para asegurar un estado limpio y evitar
     * dependencias entre tests.
     * </p>
     */
    @BeforeEach
    void setUp() {
        repositoryMock = Mockito.mock(VideojuegoRepository.class);
        service = new VideojuegoService(repositoryMock);
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
        assertEquals("Malo", service.clasificarJuego(0));
        assertEquals("Malo", service.clasificarJuego(49));
        assertEquals("Bueno", service.clasificarJuego(50));
        assertEquals("Bueno", service.clasificarJuego(89));
        assertEquals("Obra Maestra", service.clasificarJuego(90));
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
        assertThrows(IllegalArgumentException.class, () -> service.clasificarJuego(120));
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
        assertEquals(esperado, service.esJuegoLargo(juego));
    }

    /**
     * Verifica que intentar registrar un juego con título nulo lanza
     * {@link IllegalArgumentException}.
     */
    @Test
    void registrarJuego_tituloNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registrarJuego(null, "PC", 10, 80)
        );
    }

    /**
     * Verifica que intentar registrar un juego con horas negativas lanza
     * {@link IllegalArgumentException}.
     */
    @Test
    void registrarJuego_conTituloYHorasNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registrarJuego("Sonic", "PC", -20, 80)
        );
    }

    /**
     * Verifica que intentar registrar un juego con título vacío lanza
     * {@link IllegalArgumentException}.
     */
    @Test
    void registrarJuego_tituloVacio() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registrarJuego("", "PC", 10, 80)
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
        service.registrarJuego("Zelda", "Switch", 80, 95);
        verify(repositoryMock, times(1)).guardar(any(Videojuego.class));
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
        when(repositoryMock.buscarPorTitulo("Elden Ring")).thenReturn(eldenRing);

        Videojuego resultado = repositoryMock.buscarPorTitulo("Elden Ring");

        assertNotNull(resultado);
        assertEquals("Elden Ring", resultado.getTitulo());
    }
}