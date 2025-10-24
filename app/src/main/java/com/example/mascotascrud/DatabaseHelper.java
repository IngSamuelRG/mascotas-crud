package com.example.mascotascrud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mascotas.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MASCOTAS = "mascotas";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_EDAD = "edad";
    private static final String COLUMN_RAZA = "raza";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_MASCOTAS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOMBRE + " TEXT,"
                + COLUMN_EDAD + " INTEGER,"
                + COLUMN_RAZA + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASCOTAS);
        onCreate(db);
    }

    // AGREGAR MASCOTA
    public void agregarMascota(ClsMascotas mascota) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, mascota.getNombre());
        values.put(COLUMN_EDAD, mascota.getEdad());
        values.put(COLUMN_RAZA, mascota.getRaza());
        db.insert(TABLE_MASCOTAS, null, values);
        db.close();
    }

    // OBTENER TODAS LAS MASCOTAS
    public List<ClsMascotas> obtenerTodasLasMascotas() {
        List<ClsMascotas> listaMascotas = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MASCOTAS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ClsMascotas mascota = new ClsMascotas();
                mascota.setId(cursor.getInt(0));
                mascota.setNombre(cursor.getString(1));
                mascota.setEdad(cursor.getInt(2));
                mascota.setRaza(cursor.getString(3));
                listaMascotas.add(mascota);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaMascotas;
    }

    // ACTUALIZAR MASCOTA
    public int actualizarMascota(ClsMascotas mascota) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, mascota.getNombre());
        values.put(COLUMN_EDAD, mascota.getEdad());
        values.put(COLUMN_RAZA, mascota.getRaza());

        return db.update(TABLE_MASCOTAS, values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(mascota.getId())});
    }

    // ELIMINAR MASCOTA
    public void eliminarMascota(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MASCOTAS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}