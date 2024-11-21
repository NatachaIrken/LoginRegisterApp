package com.example.notasapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NotesApp.db"; // Nombre de la base de datos
    private static final int DATABASE_VERSION = 4; // Incrementa la versión si cambian las tablas
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); // Constructor del helper
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Aquí se crea la tabla de usuarios
            db.execSQL("CREATE TABLE IF NOT EXISTS Users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL)");

            // Aquí se crea la tabla de notas
            db.execSQL("CREATE TABLE IF NOT EXISTS Notes (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "title TEXT NOT NULL, " +
                    "content TEXT NOT NULL, " +
                    "FOREIGN KEY(user_id) REFERENCES Users(id) ON DELETE CASCADE)");

            // Se agrega un usuario de prueba
            db.execSQL("INSERT INTO Users (username, password) VALUES ('admin', '123456')");

            Log.d(TAG, "Base de datos creada correctamente con las tablas Users y Notes.");
        } catch (Exception e) {
            Log.e(TAG, "Error al crear la base de datos: ", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            // Elimina tablas viejas si cambian las versiones
            db.execSQL("DROP TABLE IF EXISTS Notes");
            db.execSQL("DROP TABLE IF EXISTS Users");

            // Llama a onCreate para recrear las tablas
            onCreate(db);
            Log.d(TAG, "Base de datos actualizada a la versión " + newVersion);
        } catch (Exception e) {
            Log.e(TAG, "Error al actualizar la base de datos: ", e);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        try {
            db.setForeignKeyConstraintsEnabled(true); // Activa claves foráneas
            Log.d(TAG, "Claves foráneas habilitadas.");
        } catch (Exception e) {
            Log.e(TAG, "Error al configurar la base de datos: ", e);
        }
    }
}