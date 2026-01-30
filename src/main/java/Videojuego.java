public class Videojuego {
    private String titulo;
    private String plataforma;
    private int horasJugadas;
    private int puntuacion; // 0 a 100

    public Videojuego(String titulo, String plataforma, int horasJugadas, int puntuacion) {
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.horasJugadas = horasJugadas;
        this.puntuacion = puntuacion;
    }

    // Getters y Setters
    public String getTitulo() { return titulo; }
    public String getPlataforma() { return plataforma; }
    public int getHorasJugadas() { return horasJugadas; }
    public int getPuntuacion() { return puntuacion; }

    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }

    @Override
    public String toString() {
        return titulo + " (" + plataforma + ") - " + horasJugadas + "h";
    }
}