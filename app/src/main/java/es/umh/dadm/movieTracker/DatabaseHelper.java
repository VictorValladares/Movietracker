package es.umh.dadm.movieTracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Nombre de la base de datos
    private static final String DATABASE_NAME = "UsuariosDB";
    // Tablas de la DB
    private static final String TABLE_USUARIOS = "usuarios";
    private static final String COLUMN_USUARIOS_ID = "id";
    private static final String COLUMN_USUARIOS_NOMBRE = "nombre";
    private static final String COLUMN_USUARIOS_APELLIDOS = "apellidos";
    private static final String COLUMN_USUARIOS_EMAIL = "email";
    private static final String COLUMN_USUARIOS_PASSWORD = "password";
    private static final String COLUMN_USUARIOS_FECHA_NACIMIENTO = "fecha_nacimiento";
    private static final String COLUMN_USUARIOS_RESPUESTA_SEGURIDAD = "respuesta_seguridad";
    private static final String COLUMN_USUARIOS_PREGUNTA_SEGURIDAD = "pregunta_seguridad";
    private static final String COLUMN_USUARIOS_INTERESES = "intereses";
    private static final String COLUMN_USUARIOS_AUTENTICACION = "autenticacion";

    private static final String TABLE_PLATAFORMAS = "plataformas";
    private static final String COLUMN_PLATAFORMAS_ID = "id";
    private static final String COLUMN_PLATAFORMAS_ID_USUARIO = "idUsuario";
    private static final String COLUMN_PLATAFORMAS_IMAGEN = "imagen";
    private static final String COLUMN_PLATAFORMAS_NOMBRE_PLATAFORMA = "nombrePlataforma";
    private static final String COLUMN_PLATAFORMAS_URL_ACCESO = "urlAcceso";
    private static final String COLUMN_PLATAFORMAS_USUARIO_ACCESO = "usuarioAcceso";
    private static final String COLUMN_PLATAFORMAS_PASSWORD_ACCESO = "passwordAcceso";

    private static final String TABLE_PELICULAS = "peliculas";
    private static final String COLUMN_PELICULAS_ID = "id";
    private static final String COLUMN_PELICULAS_ID_USUARIO = "idUsuario";
    private static final String COLUMN_PELICULAS_ID_PLATAFORMA = "idPlataforma";
    private static final String COLUMN_PELICULAS_CARATULA = "caratula";
    private static final String COLUMN_PELICULAS_TITULO = "titulo";
    private static final String COLUMN_PELICULAS_DURACION = "duracion";
    private static final String COLUMN_PELICULAS_GENERO = "genero";
    private static final String COLUMN_PELICULAS_CALIFICACION = "calificacion";
    private static final  String COLUMN_PELICULAS_NOMBREPLATAFORMA = "nombrePlataforma";

    // Versión de la base de datos
    private static final int DATABASE_VERSION = 3;

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USUARIOS = "CREATE TABLE " + TABLE_USUARIOS + " ("
                + COLUMN_USUARIOS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USUARIOS_NOMBRE + " TEXT,"
                + COLUMN_USUARIOS_APELLIDOS + " TEXT,"
                + COLUMN_USUARIOS_EMAIL + " TEXT,"
                + COLUMN_USUARIOS_PASSWORD + " TEXT,"
                + COLUMN_USUARIOS_FECHA_NACIMIENTO + " TEXT,"
                + COLUMN_USUARIOS_RESPUESTA_SEGURIDAD + " TEXT,"
                + COLUMN_USUARIOS_PREGUNTA_SEGURIDAD + " TEXT,"
                + COLUMN_USUARIOS_INTERESES + " TEXT,"
                + COLUMN_USUARIOS_AUTENTICACION + " INTEGER)";
        db.execSQL(CREATE_TABLE_USUARIOS);

        String CREAR_TABLA_PLATAFORMAS = "CREATE TABLE " + TABLE_PLATAFORMAS + " ("
                + COLUMN_PLATAFORMAS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PLATAFORMAS_ID_USUARIO + " INTEGER NOT NULL,"
                + COLUMN_PLATAFORMAS_IMAGEN + " TEXT,"
                + COLUMN_PLATAFORMAS_NOMBRE_PLATAFORMA + " TEXT NOT NULL,"
                + COLUMN_PLATAFORMAS_URL_ACCESO + " TEXT,"
                + COLUMN_PLATAFORMAS_USUARIO_ACCESO + " TEXT NOT NULL,"
                + COLUMN_PLATAFORMAS_PASSWORD_ACCESO + " TEXT NOT NULL,"
                + "FOREIGN KEY(" + COLUMN_PLATAFORMAS_ID_USUARIO + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_USUARIOS_ID + "))";
        db.execSQL(CREAR_TABLA_PLATAFORMAS);

        String CREAR_TABLA_PELICULAS = "CREATE TABLE " + TABLE_PELICULAS + " ("
                + COLUMN_PELICULAS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PELICULAS_ID_USUARIO + " INTEGER NOT NULL,"
                + COLUMN_PELICULAS_ID_PLATAFORMA + " INTEGER NOT NULL,"
                + COLUMN_PELICULAS_CARATULA + " TEXT,"
                + COLUMN_PELICULAS_TITULO + " TEXT NOT NULL,"
                + COLUMN_PELICULAS_DURACION + " TEXT,"
                + COLUMN_PELICULAS_GENERO + " TEXT,"
                + COLUMN_PELICULAS_CALIFICACION + " REAL,"
                + COLUMN_PELICULAS_NOMBREPLATAFORMA+ " TEXT,"
                + "FOREIGN KEY(" + COLUMN_PELICULAS_ID_USUARIO + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_USUARIOS_ID + "),"
                + "FOREIGN KEY(" + COLUMN_PELICULAS_ID_PLATAFORMA + ") REFERENCES " + TABLE_PLATAFORMAS + "(" + COLUMN_PLATAFORMAS_ID + "))";
        db.execSQL(CREAR_TABLA_PELICULAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion==1){
            String CREAR_TABLA_PELICULAS = "CREATE TABLE " + TABLE_PELICULAS + " ("
                    + COLUMN_PELICULAS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PELICULAS_ID_USUARIO + " INTEGER NOT NULL,"
                    + COLUMN_PELICULAS_ID_PLATAFORMA + " INTEGER NOT NULL,"
                    + COLUMN_PELICULAS_CARATULA + " TEXT,"
                    + COLUMN_PELICULAS_TITULO + " TEXT NOT NULL,"
                    + COLUMN_PELICULAS_DURACION + " TEXT,"
                    + COLUMN_PELICULAS_GENERO + " TEXT,"
                    + COLUMN_PELICULAS_CALIFICACION + " REAL,"
                    + COLUMN_PELICULAS_NOMBREPLATAFORMA+ " TEXT,"
                    + "FOREIGN KEY(" + COLUMN_PELICULAS_ID_USUARIO + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_USUARIOS_ID + "),"
                    + "FOREIGN KEY(" + COLUMN_PELICULAS_ID_PLATAFORMA + ") REFERENCES " + TABLE_PLATAFORMAS + "(" + COLUMN_PLATAFORMAS_ID + "))";
            db.execSQL(CREAR_TABLA_PELICULAS);
        }
        else{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLATAFORMAS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PELICULAS);
            onCreate(db);
        }
    }

    // Método para agregar un usuario a la base de datos
    public void addUsuario(Usuario usuario, String passwordHash) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIOS_NOMBRE, usuario.getNombre());
        values.put(COLUMN_USUARIOS_APELLIDOS, usuario.getApellidos());
        values.put(COLUMN_USUARIOS_EMAIL, usuario.getEmail());
        values.put(COLUMN_USUARIOS_PASSWORD, passwordHash);
        values.put(COLUMN_USUARIOS_FECHA_NACIMIENTO, usuario.getFechaNacimiento());
        values.put(COLUMN_USUARIOS_RESPUESTA_SEGURIDAD, usuario.getRespuestaSeguridad());
        values.put(COLUMN_USUARIOS_PREGUNTA_SEGURIDAD, usuario.getPreguntaSeguridad());
        values.put(COLUMN_USUARIOS_INTERESES, usuario.getIntereses());
        values.put(COLUMN_USUARIOS_AUTENTICACION, usuario.isAutenticacion() ? 1 : 0);

        db.insert(TABLE_USUARIOS, null, values);
        db.close();
    }

    public int checkUsuario(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { COLUMN_USUARIOS_ID, COLUMN_USUARIOS_PASSWORD };
        String selection = COLUMN_USUARIOS_EMAIL + " = ?";
        String[] selectionArgs = { email };
        Cursor cursor = db.query(TABLE_USUARIOS, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int passwordIndex = cursor.getColumnIndex(COLUMN_USUARIOS_PASSWORD);
            if (passwordIndex != -1) {
                String storedPasswordHash = cursor.getString(passwordIndex);
                if (verifyPasswordHash(password, storedPasswordHash)) {
                    int userIdIndex = cursor.getColumnIndex(COLUMN_USUARIOS_ID);
                    if (userIdIndex != -1) {
                        return cursor.getInt(userIdIndex);
                    }
                }
            }
        }
        cursor.close();
        db.close();
        return -1;
    }

    private boolean verifyPasswordHash(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public boolean emailExiste(String m){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { COLUMN_USUARIOS_ID };
        String selection = COLUMN_USUARIOS_EMAIL + " = ?";
        String[] selectionArgs = { m };
        Cursor cursor = db.query(TABLE_USUARIOS, columns, selection, selectionArgs, null, null, null);
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return existe;
    }

    public long agregarPlataforma(Plataforma plataforma) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PLATAFORMAS_ID_USUARIO, plataforma.getIdUsuario());
        values.put(COLUMN_PLATAFORMAS_IMAGEN, plataforma.getImagen());
        values.put(COLUMN_PLATAFORMAS_NOMBRE_PLATAFORMA, plataforma.getNombrePlataforma());
        values.put(COLUMN_PLATAFORMAS_URL_ACCESO, plataforma.getUrlAcceso());
        values.put(COLUMN_PLATAFORMAS_USUARIO_ACCESO, plataforma.getUsuarioAcceso());
        values.put(COLUMN_PLATAFORMAS_PASSWORD_ACCESO, plataforma.getPasswordAcceso());

        long id = db.insert(TABLE_PLATAFORMAS, null, values);
        db.close();
        return id;
    }

    public List<Plataforma> obtenerPlataformasUsuario(int idUsuario) {
        List<Plataforma> plataformas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String orderBy = COLUMN_PLATAFORMAS_NOMBRE_PLATAFORMA + " ASC";

        Cursor cursor = db.query(TABLE_PLATAFORMAS, new String[] {COLUMN_PLATAFORMAS_ID, COLUMN_PLATAFORMAS_ID_USUARIO, COLUMN_PLATAFORMAS_IMAGEN, COLUMN_PLATAFORMAS_NOMBRE_PLATAFORMA, COLUMN_PLATAFORMAS_URL_ACCESO, COLUMN_PLATAFORMAS_USUARIO_ACCESO, COLUMN_PLATAFORMAS_PASSWORD_ACCESO}, COLUMN_PLATAFORMAS_ID_USUARIO + " = ?", new String[] {String.valueOf(idUsuario)}, null, null, orderBy);
        if (cursor.moveToFirst()) {
            do {
                Plataforma plataforma = new Plataforma(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_PLATAFORMAS_ID)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_PLATAFORMAS_ID_USUARIO)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PLATAFORMAS_IMAGEN)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PLATAFORMAS_NOMBRE_PLATAFORMA)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PLATAFORMAS_URL_ACCESO)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PLATAFORMAS_USUARIO_ACCESO)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PLATAFORMAS_PASSWORD_ACCESO))
                );
                plataformas.add(plataforma);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return plataformas;
    }

    public Plataforma getPlataformaById(int idPlataforma) {
        SQLiteDatabase db = this.getReadableDatabase();
        Plataforma plataforma = null;

        String[] projection = {
                COLUMN_PLATAFORMAS_ID,
                COLUMN_PLATAFORMAS_ID_USUARIO,
                COLUMN_PLATAFORMAS_IMAGEN,
                COLUMN_PLATAFORMAS_NOMBRE_PLATAFORMA,
                COLUMN_PLATAFORMAS_URL_ACCESO,
                COLUMN_PLATAFORMAS_USUARIO_ACCESO,
                COLUMN_PLATAFORMAS_PASSWORD_ACCESO
        };

        String selection = COLUMN_PLATAFORMAS_ID + " = ?";
        String[] selectionArgs = { String.valueOf(idPlataforma) };

        Cursor cursor = db.query(
                TABLE_PLATAFORMAS,   // La tabla a consultar
                projection,          // Las columnas a retornar
                selection,           // La columna para la cláusula WHERE
                selectionArgs,       // Los valores para la cláusula WHERE
                null,                // No agrupar las filas
                null,                // No filtrar por grupos de filas
                null                 // El orden de la ordenación
        );

        if (cursor.moveToFirst()) {
            plataforma = new Plataforma(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PLATAFORMAS_ID)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PLATAFORMAS_ID_USUARIO)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PLATAFORMAS_IMAGEN)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PLATAFORMAS_NOMBRE_PLATAFORMA)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PLATAFORMAS_URL_ACCESO)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PLATAFORMAS_USUARIO_ACCESO)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PLATAFORMAS_PASSWORD_ACCESO))
            );
        }

        cursor.close();
        db.close();
        return plataforma;
    }

    public void eliminarPlataforma(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Asegúrate de que COLUMN_PLATAFORMAS_ID sea el nombre correcto de tu columna de ID
        db.delete(TABLE_PELICULAS, COLUMN_PELICULAS_ID_PLATAFORMA + " = ?", new String[] {String.valueOf(id)});
        db.delete(TABLE_PLATAFORMAS, COLUMN_PLATAFORMAS_ID + " = ?", new String[] {String.valueOf(id)});

        db.close();
    }

    public List<Pelicula> obtenerPeliculasUsuario(int userID) {
        List<Pelicula> peliculas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_PELICULAS_ID,
                COLUMN_PELICULAS_ID_USUARIO,
                COLUMN_PELICULAS_ID_PLATAFORMA,
                COLUMN_PELICULAS_CARATULA,
                COLUMN_PELICULAS_TITULO,
                COLUMN_PELICULAS_DURACION,
                COLUMN_PELICULAS_GENERO,
                COLUMN_PELICULAS_CALIFICACION,
                COLUMN_PELICULAS_NOMBREPLATAFORMA
        };

        // Criterio de selección y argumentos
        String selection = COLUMN_PELICULAS_ID_USUARIO + " = ?";
        String[] selectionArgs = {String.valueOf(userID)};

        // Ordenar resultados por el título de la película de forma alfabética ascendente
        String sortOrder = COLUMN_PELICULAS_TITULO + " ASC";

        Cursor cursor = db.query(
                TABLE_PELICULAS,   // La tabla a consultar
                projection,        // Las columnas a retornar
                selection,         // La columna para la cláusula WHERE
                selectionArgs,     // Los valores para la cláusula WHERE
                null,              // No agrupar las filas
                null,              // No filtrar por grupos de filas
                sortOrder          // La ordenación de los resultados
        );

        if (cursor.moveToFirst()) {
            do {
                Pelicula pelicula = new Pelicula();
                pelicula.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_PELICULAS_ID)));
                pelicula.setIdUsuario(cursor.getInt(cursor.getColumnIndex(COLUMN_PELICULAS_ID_USUARIO)));
                pelicula.setIdPlataforma(cursor.getInt(cursor.getColumnIndex(COLUMN_PELICULAS_ID_PLATAFORMA)));
                pelicula.setCaratula(cursor.getString(cursor.getColumnIndex(COLUMN_PELICULAS_CARATULA)));
                pelicula.setTitulo(cursor.getString(cursor.getColumnIndex(COLUMN_PELICULAS_TITULO)));
                pelicula.setDuracion(cursor.getString(cursor.getColumnIndex(COLUMN_PELICULAS_DURACION)));
                pelicula.setGenero(cursor.getString(cursor.getColumnIndex(COLUMN_PELICULAS_GENERO)));
                pelicula.setCalificacion(cursor.getFloat(cursor.getColumnIndex(COLUMN_PELICULAS_CALIFICACION)));
                pelicula.setNombrePlataforma(cursor.getString(cursor.getColumnIndex(COLUMN_PELICULAS_NOMBREPLATAFORMA)));

                peliculas.add(pelicula);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return peliculas;
    }

    public Pelicula getPeliculaById(int idPelicula) {
        SQLiteDatabase db = this.getReadableDatabase();
        Pelicula pelicula = null;

        String[] projection = {
                COLUMN_PELICULAS_ID,
                COLUMN_PELICULAS_ID_USUARIO,
                COLUMN_PELICULAS_ID_PLATAFORMA,
                COLUMN_PELICULAS_CARATULA,
                COLUMN_PELICULAS_TITULO,
                COLUMN_PELICULAS_DURACION,
                COLUMN_PELICULAS_GENERO,
                COLUMN_PELICULAS_CALIFICACION,
                COLUMN_PELICULAS_NOMBREPLATAFORMA
        };

        // Definir el filtro de la consulta
        String selection = COLUMN_PELICULAS_ID + " = ?";
        String[] selectionArgs = { String.valueOf(idPelicula) };

        Cursor cursor = db.query(
                TABLE_PELICULAS,   // La tabla a consultar
                projection,        // Las columnas a retornar
                selection,         // La columna para la cláusula WHERE
                selectionArgs,     // Los valores para la cláusula WHERE
                null,              // No agrupar las filas
                null,              // No filtrar por grupo de filas
                null               // El orden de la ordenación
        );

        if (cursor.moveToFirst()) {
            pelicula = new Pelicula();
            pelicula.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_PELICULAS_ID)));
            pelicula.setIdUsuario(cursor.getInt(cursor.getColumnIndex(COLUMN_PELICULAS_ID_USUARIO)));
            pelicula.setIdPlataforma(cursor.getInt(cursor.getColumnIndex(COLUMN_PELICULAS_ID_PLATAFORMA)));
            pelicula.setCaratula(cursor.getString(cursor.getColumnIndex(COLUMN_PELICULAS_CARATULA)));
            pelicula.setTitulo(cursor.getString(cursor.getColumnIndex(COLUMN_PELICULAS_TITULO)));
            pelicula.setDuracion(cursor.getString(cursor.getColumnIndex(COLUMN_PELICULAS_DURACION)));
            pelicula.setGenero(cursor.getString(cursor.getColumnIndex(COLUMN_PELICULAS_GENERO)));
            pelicula.setCalificacion(cursor.getFloat(cursor.getColumnIndex(COLUMN_PELICULAS_CALIFICACION)));
            pelicula.setNombrePlataforma(cursor.getString(cursor.getColumnIndex(COLUMN_PELICULAS_NOMBREPLATAFORMA)));
        }

        cursor.close();
        db.close();

        return pelicula;
    }


    public long agregarPelicula(Pelicula nuevaPelicula) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PELICULAS_ID_USUARIO, nuevaPelicula.getIdUsuario());
        values.put(COLUMN_PELICULAS_ID_PLATAFORMA, nuevaPelicula.getIdPlataforma());
        values.put(COLUMN_PELICULAS_CARATULA, nuevaPelicula.getCaratula());
        values.put(COLUMN_PELICULAS_TITULO, nuevaPelicula.getTitulo());
        values.put(COLUMN_PELICULAS_DURACION, nuevaPelicula.getDuracion());
        values.put(COLUMN_PELICULAS_GENERO, nuevaPelicula.getGenero());
        values.put(COLUMN_PELICULAS_CALIFICACION, nuevaPelicula.getCalificacion());
        values.put(COLUMN_PELICULAS_NOMBREPLATAFORMA, nuevaPelicula.getNombrePlataforma());

        long id = db.insert(TABLE_PELICULAS, null, values);
        db.close();
        return id;
    }

    public void eliminarPelicula(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PELICULAS, COLUMN_PELICULAS_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Pelicula> obtenerPeliculasPorPlataforma(int idPlataforma) {
        List<Pelicula> peliculas = new ArrayList<>();
        // Obtener una instancia de lectura de la base de datos
        SQLiteDatabase db = this.getReadableDatabase();

        // Definir las columnas que queremos obtener
        String[] projection = {
                COLUMN_PELICULAS_ID,
                COLUMN_PELICULAS_ID_USUARIO,
                COLUMN_PELICULAS_ID_PLATAFORMA,
                COLUMN_PELICULAS_CARATULA,
                COLUMN_PELICULAS_TITULO,
                COLUMN_PELICULAS_DURACION,
                COLUMN_PELICULAS_GENERO,
                COLUMN_PELICULAS_CALIFICACION,
                COLUMN_PELICULAS_NOMBREPLATAFORMA
        };

        // Definir el filtro de la consulta
        String selection = COLUMN_PELICULAS_ID_PLATAFORMA + " = ?";
        String[] selectionArgs = { String.valueOf(idPlataforma) };

        // Realizar la consulta
        Cursor cursor = db.query(
                TABLE_PELICULAS,   // La tabla a consultar
                projection,        // Las columnas a retornar
                selection,         // La columna para la cláusula WHERE
                selectionArgs,     // Los valores para la cláusula WHERE
                null,              // No agrupar las filas
                null,              // No filtrar por grupo de filas
                null               // El orden de la ordenación
        );

        // Iterar a través de los resultados y añadirlos a la lista de películas
        if (cursor.moveToFirst()) {
            do {
                Pelicula pelicula = new Pelicula();
                pelicula.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_PELICULAS_ID)));
                pelicula.setIdUsuario(cursor.getInt(cursor.getColumnIndex(COLUMN_PELICULAS_ID_USUARIO)));
                pelicula.setIdPlataforma(cursor.getInt(cursor.getColumnIndex(COLUMN_PELICULAS_ID_PLATAFORMA)));
                pelicula.setCaratula(cursor.getString(cursor.getColumnIndex(COLUMN_PELICULAS_CARATULA)));
                pelicula.setTitulo(cursor.getString(cursor.getColumnIndex(COLUMN_PELICULAS_TITULO)));
                pelicula.setDuracion(cursor.getString(cursor.getColumnIndex(COLUMN_PELICULAS_DURACION)));
                pelicula.setGenero(cursor.getString(cursor.getColumnIndex(COLUMN_PELICULAS_GENERO)));
                pelicula.setCalificacion(cursor.getFloat(cursor.getColumnIndex(COLUMN_PELICULAS_CALIFICACION)));
                pelicula.setNombrePlataforma(cursor.getString(cursor.getColumnIndex(COLUMN_PELICULAS_NOMBREPLATAFORMA)));
                // Añadir la película a la lista
                peliculas.add(pelicula);
            } while (cursor.moveToNext());
        }

        // Cerrar el cursor y la base de datos
        cursor.close();
        db.close();

        return peliculas;
    }

    public String getNombreApellidoUsuario(int idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = { COLUMN_USUARIOS_NOMBRE, COLUMN_USUARIOS_APELLIDOS };
        String selection = COLUMN_USUARIOS_ID + " = ?";
        String[] selectionArgs = { String.valueOf(idUsuario) };

        Cursor cursor = db.query(TABLE_USUARIOS, projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(cursor.getColumnIndex(COLUMN_USUARIOS_NOMBRE));
            String apellidos = cursor.getString(cursor.getColumnIndex(COLUMN_USUARIOS_APELLIDOS));
            cursor.close();
            db.close();
            return nombre + " " + apellidos;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }


    public static String getColumnPeliculasId() {
        return COLUMN_PELICULAS_ID;
    }
    public static String getTablePeliculas() {
        return TABLE_PELICULAS;
    }

    public static String getColumnPeliculasTitulo() {
        return COLUMN_PELICULAS_TITULO;
    }

    public static String getColumnPeliculasDuracion() {
        return COLUMN_PELICULAS_DURACION;
    }

    public static String getColumnPeliculasGenero() {
        return COLUMN_PELICULAS_GENERO;
    }

    public static String getColumnPeliculasCalificacion() {
        return COLUMN_PELICULAS_CALIFICACION;
    }

    public static String getColumnPeliculasNombrePlataforma() {
        return COLUMN_PELICULAS_NOMBREPLATAFORMA;
    }

    public static String getColumnPeliculasCaratula() {
        return COLUMN_PELICULAS_CARATULA;
    }

    public static String getColumnPlataformasId() {
        return COLUMN_PLATAFORMAS_ID;
    }
    public static String getColumnPlataformasNombre() {
        return COLUMN_PLATAFORMAS_NOMBRE_PLATAFORMA;
    }

    public static String getColumnPlataformasUrl() {
        return COLUMN_PLATAFORMAS_URL_ACCESO;
    }

    public static String getColumnPlataformasUsuario() {
        return COLUMN_PLATAFORMAS_USUARIO_ACCESO;
    }

    public static String getColumnPlataformasPassword() {
        return COLUMN_PLATAFORMAS_PASSWORD_ACCESO;
    }

    public static String getColumnPlataformasImagen() {
        return COLUMN_PLATAFORMAS_IMAGEN;
    }

    public static String getTablePlataformas() {
        return TABLE_PLATAFORMAS;
    }
}
