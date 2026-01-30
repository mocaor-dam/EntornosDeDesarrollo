public class VideojuegoService {

    private VideojuegoRepository repository;

    // Inyección de dependencias manual por constructor
    public VideojuegoService(VideojuegoRepository repository) {
        this.repository = repository;
    }

    /**
     * Registra un juego nuevo.
     * BUG: No valida si el título es null o vacío.
     * BUG: No valida si las horas son negativas.
     *
     * ARREGLADO SOLO LE FALTABA DOS CODICIONES SI ERA EL TITULO VACIO O NULO Y HORAS SEAN NO NEGATIVAS
     */
    public void registrarJuego(String titulo, String plataforma, int horas, int puntuacion) {
        if(titulo == null || titulo.isEmpty()){
            throw new IllegalArgumentException("El titulo no puedes dejarlo sin nombre");
        }
        if(horas < 0){
            throw new IllegalArgumentException("La hora no puede ser negativa");
        }
        Videojuego nuevo = new Videojuego(titulo, plataforma, horas, puntuacion);
        repository.guardar(nuevo);
    }

    /**
     * Devuelve una etiqueta basada en la puntuación (0-100).
     * BUGS DE LÍMITES INTENCIONADOS:
     * - 0 a 49: "Malo"
     * - 50 a 89: "Bueno"
     * - 90 a 100: "Obra Maestra"
     *
     * ARREGLADO (ERA EN LOS PARAMETROS IF (50-90) Y EL RETURN QUE SOBRA DEL FINAL
     */
    public String clasificarJuego(int puntuacion) {
        if (puntuacion < 0 || puntuacion > 100) {
            throw new IllegalArgumentException("Puntuación inválida");
        }

        // BUG: Si la puntuación es exactamente 50, no entra en el primer if (<50)
        // ni en el segundo (>50). Devuelve null o comportamiento inesperado.
        if (puntuacion < 50) {
            return "Malo";
        } else if (puntuacion >= 50 && puntuacion < 90) {
            return "Bueno";
        } else {
            return "Obra Maestra";
        }

    }

    /**
     * Determina si un juego se considera "Largo".
     * Regla: Más de 50 horas es largo, EXCEPTO si es de la plataforma "Movil",
     * que se considera largo a partir de 20 horas.
     */
    public boolean esJuegoLargo(Videojuego juego) {
        if (juego.getPlataforma().equals("Movil")) {
            return juego.getHorasJugadas() > 20;
        }
        return juego.getHorasJugadas() > 50;
    }

    /**
     * Calcula el total de horas jugadas en toda la biblioteca.
     * BUG: No gestiona los nulos en el array. Si el array tiene huecos, lanzará NullPointerException.
     *
     * ARREGLADO
     */
    public int calcularTotalHoras() {
        Videojuego[] todos = repository.obtenerTodos();
        int total = 0;

        for (int i = 0; i < todos.length; i++) {
            if (todos[i] != null) {
                total += todos[i].getHorasJugadas();
            }
        }
        return total;
    }
}
