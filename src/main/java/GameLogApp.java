public class GameLogApp {

    public static void main(String[] args) {
        // 1. Preparamos el entorno con una implementación "fake" del repositorio
        VideojuegoRepository repoReal = new RepositorioEnMemoria();
        VideojuegoService servicio = new VideojuegoService(repoReal);

        System.out.println("Iniciando GameLog...");

        // 2. Añadimos algunos datos de prueba
        servicio.registrarJuego("Super Mario", "Nintendo", 10, 95);
        servicio.registrarJuego("Candy Crush", "Movil", 5, 40);

        // El repositorio tiene capacidad para 5 juegos, pero solo hemos metido 2.
        // Los otros 3 huecos son NULL.

        // 3. INTENTO DE CÁLCULO (AQUÍ FALLARÁ)
        // El alumno debe poner un BREAKPOINT aquí y entrar con F7/F8
        // para ver por qué explota al sumar las horas.
        System.out.println("Calculando estadísticas...");

        try {
            int totalHoras = servicio.calcularTotalHoras();
            System.out.println("Total horas jugadas: " + totalHoras);
        } catch (Exception e) {
            System.err.println("¡ERROR CRÍTICO EN EL SISTEMA!");
            System.err.println("Causa: " + e.toString());
            System.err.println("Usa el depurador para encontrar el null en el array.");
        }
    }
}

// --- Implementación simple para poder ejecutar el código sin Mockito ---
class RepositorioEnMemoria implements VideojuegoRepository {
    // Array fijo simulando la tabla de base de datos
    private Videojuego[] baseDeDatos = new Videojuego[5];
    private int contador = 0;

    @Override
    public void guardar(Videojuego juego) {
        if (contador < baseDeDatos.length) {
            baseDeDatos[contador] = juego;
            contador++;
        }
    }

    @Override
    public Videojuego[] obtenerTodos() {
        // Devuelve el array completo, INCLUYENDO LOS HUECOS NULL
        return baseDeDatos;
    }

    @Override
    public Videojuego buscarPorTitulo(String titulo) {
        for (Videojuego v : baseDeDatos) {
            if (v != null && v.getTitulo().equals(titulo)) {
                return v;
            }
        }
        return null;
    }
}