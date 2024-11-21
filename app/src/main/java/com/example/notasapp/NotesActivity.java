package com.example.notasapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NotesActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private int userId;

    private EditText titleInput, contentInput;
    private Button saveNoteButton, viewNotesButton, backToHomeButton;
    private ListView notesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Aquí se inicializa la base de datos y se obtiene el ID del usuario actual
        databaseHelper = new DatabaseHelper(this);
        userId = getIntent().getIntExtra("user_id", -1);

        if (userId == -1) { // Valida que se reciba un user_id válido
            Toast.makeText(this, "Error al obtener el usuario.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Aquí se inicializan las vistas del layout
        titleInput = findViewById(R.id.note_title);
        contentInput = findViewById(R.id.note_content);
        saveNoteButton = findViewById(R.id.save_button);
        viewNotesButton = findViewById(R.id.view_notes_button);
        backToHomeButton = findViewById(R.id.back_to_home_button); // Botón para regresar al inicio
        notesListView = findViewById(R.id.notes_list);

        // Configuración de los listeners para los botones
        saveNoteButton.setOnClickListener(v -> saveNote()); // Guarda nota
        viewNotesButton.setOnClickListener(v -> viewNotes()); // Ve las notas

        // Este botón lleva al usuario de regreso a la pantalla de inicio
        backToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Cierra la actividad actual
        });
    }

    private void saveNote() {
        // Aquí se obtienen los datos ingresados por el usuario
        String title = titleInput.getText().toString().trim();
        String content = contentInput.getText().toString().trim();

        // Valida que no haya campos vacíos
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId); // Asocia la nota al usuario actual
        values.put("title", title);
        values.put("content", content);

        // Intenta guardar la nota en la base de datos
        long result = db.insert("Notes", null, values);
        if (result != -1) {
            Toast.makeText(this, "Nota guardada exitosamente.", Toast.LENGTH_SHORT).show();
            titleInput.setText(""); // Limpia el campo de título
            contentInput.setText(""); // Limpia el campo de contenido
        } else {
            Toast.makeText(this, "Error al guardar la nota.", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void viewNotes() {
        // Consulta todas las notas del usuario actual
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "Notes",
                new String[]{"_id", "title", "content"}, // Selecciona columnas relevantes
                "user_id=?", // Condición para filtrar por usuario
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        // Si hay resultados, mostrará las notas en la lista
        if (cursor != null) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2, // Layout de cada ítem en la lista
                    cursor,
                    new String[]{"title", "content"}, // Columnas que se mostrarán
                    new int[]{android.R.id.text1, android.R.id.text2}, // IDs de las vistas en el layout
                    0
            );
            notesListView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No se encontraron notas.", Toast.LENGTH_SHORT).show();
        }
    }
}
