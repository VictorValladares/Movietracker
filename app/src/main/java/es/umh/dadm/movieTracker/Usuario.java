package es.umh.dadm.movieTracker;

public class Usuario {
    private String nombre;
    private String apellidos;
    private String email;
    private String fechaNacimiento;
    private String respuestaSeguridad;
    private String preguntaSeguridad;
    private String intereses;
    private boolean autenticacion;

    // Constructor
    public Usuario(String nombre, String apellidos, String email, String fechaNacimiento, String respuestaSeguridad, String preguntaSeguridad, String intereses, boolean autenticacion) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.respuestaSeguridad = respuestaSeguridad;
        this.preguntaSeguridad = preguntaSeguridad;
        this.intereses = intereses;
        this.autenticacion = autenticacion;
    }

    // Métodos getters y setters para cada campo
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getRespuestaSeguridad() {
        return respuestaSeguridad;
    }

    public void setRespuestaSeguridad(String respuestaSeguridad) {
        this.respuestaSeguridad = respuestaSeguridad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreguntaSeguridad() {
        return preguntaSeguridad;
    }

    public void setPreguntaSeguridad(String preguntaSeguridad) {
        this.preguntaSeguridad = preguntaSeguridad;
    }

    public String getIntereses() {
        return intereses;
    }

    public void setIntereses(String intereses) {
        this.intereses = intereses;
    }

    public boolean isAutenticacion() {
        return autenticacion;
    }

    public void setAutenticacion(boolean autenticacion) {
        this.autenticacion = autenticacion;
    }
}
