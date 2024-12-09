package es.umh.dadm.movieTracker;

public class Pelicula {
    private int id;
    private int idUsuario;
    private int idPlataforma;
    private String caratula;
    private String titulo;
    private String duracion;
    private String genero;
    private float calificacion;
    private String nombrePlataforma;


    // Constructor
    public Pelicula(int idUsuario, int idPlataforma, String caratula, String titulo, String duracion, String genero, float calificacion, String nombrePlataforma) {
        this.idUsuario = idUsuario;
        this.idPlataforma = idPlataforma;
        this.caratula = caratula;
        this.titulo = titulo;
        this.duracion = duracion;
        this.genero = genero;
        this.calificacion = calificacion;
        this.nombrePlataforma = nombrePlataforma;
    }

    public Pelicula(int id, int idUsuario, int idPlataforma, String caratula, String titulo, String duracion, String genero, float calificacion, String nombrePlataforma) {
        this(idUsuario, idPlataforma, caratula, titulo, duracion, genero, calificacion, nombrePlataforma);
        this.id = id;
    }

    public Pelicula() {

    }

    public String peliculatoString() {
        return R.string.peliculatostring_pelicula + titulo + R.string.peliculatostring_duracion + duracion + R.string.peliculatostring_genero + genero +
                R.string.peliculatostring_clasificacion + calificacion + R.string.peliculatostring_plataforma + nombrePlataforma;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPlataforma() {
        return idPlataforma;
    }

    public void setIdPlataforma(int idPlataforma) {
        this.idPlataforma = idPlataforma;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public String getNombrePlataforma() {
        return nombrePlataforma;
    }

    public void setNombrePlataforma(String nombrePlataforma) {
        this.nombrePlataforma = nombrePlataforma;
    }
}
