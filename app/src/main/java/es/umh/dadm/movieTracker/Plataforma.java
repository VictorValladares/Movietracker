package es.umh.dadm.movieTracker;

public class Plataforma {
    // Atributos de la clase Plataforma
    private int id = -1;
    private int idUsuario;
    private String imagen;
    private String nombrePlataforma;
    private String urlAcceso;
    private String usuarioAcceso;
    private String passwordAcceso;

    // Constructor
    public Plataforma(int idUsuario, String imagen, String nombrePlataforma, String urlAcceso, String usuarioAcceso, String passwordAcceso) {
        this.idUsuario = idUsuario;
        this.imagen = imagen;
        this.nombrePlataforma = nombrePlataforma;
        this.urlAcceso = urlAcceso;
        this.usuarioAcceso = usuarioAcceso;
        this.passwordAcceso = passwordAcceso;
    }

    // Constructor para manipular plataformas existentes (con ID)
    public Plataforma(int id, int idUsuario, String imagen, String nombrePlataforma, String urlAcceso, String usuarioAcceso, String passwordAcceso) {
        this(idUsuario, imagen, nombrePlataforma, urlAcceso, usuarioAcceso, passwordAcceso); // Llama al constructor sin ID
        this.id = id;
    }

    public String platformtoString() {
        return R.string.platformtostring_plataforma + nombrePlataforma +
                R.string.platformtostring_url + urlAcceso +
                R.string.platformtostring_usuario + usuarioAcceso +
                R.string.platformtostring_password + passwordAcceso;
    }

    @Override
    public String toString()
    {
        return this.nombrePlataforma;
    }
    // getters y setters
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombrePlataforma() {
        return nombrePlataforma;
    }

    public void setNombrePlataforma(String nombrePlataforma) {
        this.nombrePlataforma = nombrePlataforma;
    }

    public String getUrlAcceso() {
        return urlAcceso;
    }

    public void setUrlAcceso(String urlAcceso) {
        this.urlAcceso = urlAcceso;
    }

    public String getUsuarioAcceso() {
        return usuarioAcceso;
    }

    public void setUsuarioAcceso(String usuarioAcceso) {
        this.usuarioAcceso = usuarioAcceso;
    }

    public String getPasswordAcceso() {
        return passwordAcceso;
    }

    public void setPasswordAcceso(String passwordAcceso) {
        this.passwordAcceso = passwordAcceso;
    }


}
