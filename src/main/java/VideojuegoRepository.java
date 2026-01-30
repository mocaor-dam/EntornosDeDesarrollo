/**
 * Interfaz que define las operaciones de base de datos.
 * En los tests, esta interfaz será MOCKEADA.
 * En la App principal, usaremos una implementación "dummy" en memoria.
 */
public interface VideojuegoRepository {

    // Guarda el juego en la base de datos
    void guardar(Videojuego juego);

    // Devuelve un array con todos los juegos
    Videojuego[] obtenerTodos();

    // Busca un juego por título exacto
    Videojuego buscarPorTitulo(String titulo);
}