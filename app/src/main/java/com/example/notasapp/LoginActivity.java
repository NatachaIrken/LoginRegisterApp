package com.example.notasapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText userInputField, passInputField;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Aquí se inicializan las vistas del layout
        userInputField = findViewById(R.id.username);
        passInputField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);

        // Aquí se crea una instancia del helper de la base de datos
        databaseHelper = new DatabaseHelper(this);

        // Aquí se asignan las acciones de los botones
        loginButton.setOnClickListener(v -> performLogin());
        registerButton.setOnClickListener(v -> createAccount());
    }

    private void performLogin() {
        String username = userInputField.getText().toString().trim();
        String password = passInputField.getText().toString().trim();

        // Aquí se valida que no haya campos vacíos
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor, llene todos los campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = databaseHelper.getReadableDatabase();

            // Aquí se verifica que la tabla 'Users' exista
            cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Users'", null);
            if (cursor == null || !cursor.moveToFirst()) {
                throw new Exception("La tabla 'Users' no existe. Verifica la base de datos.");
            }
            cursor.close(); // Cerramos este cursor porque ya no se necesita

            // Aquí se consulta si el usuario y la contraseña ingresados existen en la base de datos
            cursor = db.query(
                    "Users",
                    new String[]{"id"}, // Solo se necesita el ID del usuario
                    "username=? AND password=?", // Condiciones de búsqueda
                    new String[]{username, password},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                // Aquí se inicia NotesActivity enviando el ID del usuario
                Intent intent = new Intent(this, NotesActivity.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
                finish(); // Finaliza LoginActivity
            } else {
                Toast.makeText(this, "Credenciales incorrectas. Intente nuevamente.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al iniciar sesión: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("LoginActivity", "Error en performLogin", e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    private void createAccount() {
        String username = userInputField.getText().toString().trim();
        String password = passInputField.getText().toString().trim();

        // Aquí se valida que no haya campos vacíos
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor, llene todos los campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aquí se valida que la contraseña tenga al menos 6 caracteres
        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = databaseHelper.getWritableDatabase();

            // Aquí se verifica que la tabla 'Users' exista
            cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Users'", null);
            if (cursor == null || !cursor.moveToFirst()) {
                throw new Exception("La tabla 'Users' no existe. Verifica la base de datos.");
            }
            cursor.close(); // Cerramos este cursor porque ya no se necesita

            // Aquí se verifica si el nombre de usuario ya está registrado
            cursor = db.query(
                    "Users",
                    null,
                    "username=?", // Condición de búsqueda
                    new String[]{username},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                Toast.makeText(this, "El nombre de usuario ya está en uso. Intente con otro", Toast.LENGTH_SHORT).show();
            } else {
                // Aquí se insertan los datos del nuevo usuario en la tabla 'Users'
                ContentValues values = new ContentValues();
                values.put("username", username);
                values.put("password", password);

                long result = db.insert("Users", null, values);

                if (result != -1) {
                    Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
                    // Aquí se redirige al usuario a NotesActivity
                    Intent intent = new Intent(this, NotesActivity.class);
                    intent.putExtra("user_id", (int) result);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Error al crear la cuenta. Intente nuevamente.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al crear la cuenta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }
}
