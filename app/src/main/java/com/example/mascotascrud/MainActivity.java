package com.example.mascotascrud;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ListView listViewMascotas;
    private Button btnAgregar;
    private ArrayAdapter<String> adapter;
    private List<ClsMascotas> listaMascotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);

        // Conectar con los elementos del layout
        listViewMascotas = findViewById(R.id.listViewMascotas);
        btnAgregar = findViewById(R.id.btnAgregarMascota);

        // Cargar las mascotas
        cargarMascotas();

        // Botón para agregar mascota
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoAgregarMascota();
            }
        });

        // Click largo para eliminar mascota
        listViewMascotas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                eliminarMascota(position);
                return true;
            }
        });

        // Click normal para editar mascota
        listViewMascotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editarMascota(position);
            }
        });
    }

    private void cargarMascotas() {
        listaMascotas = databaseHelper.obtenerTodasLasMascotas();
        String[] mascotasArray = new String[listaMascotas.size()];

        for (int i = 0; i < listaMascotas.size(); i++) {
            ClsMascotas mascota = listaMascotas.get(i);
            mascotasArray[i] = mascota.getNombre() + " - " + mascota.getEdad() + " años - " + mascota.getRaza();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mascotasArray);
        listViewMascotas.setAdapter(adapter);
    }

    private void mostrarDialogoAgregarMascota() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Nueva Mascota");

        // Inflar el diseño del diálogo
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_mascota, null);
        builder.setView(view);

        final EditText etNombre = view.findViewById(R.id.etNombre);
        final EditText etEdad = view.findViewById(R.id.etEdad);
        final EditText etRaza = view.findViewById(R.id.etRaza);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre = etNombre.getText().toString();
                String edadStr = etEdad.getText().toString();
                String raza = etRaza.getText().toString();

                if (!nombre.isEmpty() && !edadStr.isEmpty() && !raza.isEmpty()) {
                    int edad = Integer.parseInt(edadStr);
                    ClsMascotas mascota = new ClsMascotas();
                    mascota.setNombre(nombre);
                    mascota.setEdad(edad);
                    mascota.setRaza(raza);

                    databaseHelper.agregarMascota(mascota);
                    cargarMascotas();
                    Toast.makeText(MainActivity.this, "Mascota agregada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void editarMascota(int position) {
        ClsMascotas mascota = listaMascotas.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Mascota");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_mascota, null);
        builder.setView(view);

        final EditText etNombre = view.findViewById(R.id.etNombre);
        final EditText etEdad = view.findViewById(R.id.etEdad);
        final EditText etRaza = view.findViewById(R.id.etRaza);

        // Llenar con datos actuales
        etNombre.setText(mascota.getNombre());
        etEdad.setText(String.valueOf(mascota.getEdad()));
        etRaza.setText(mascota.getRaza());

        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre = etNombre.getText().toString();
                String edadStr = etEdad.getText().toString();
                String raza = etRaza.getText().toString();

                if (!nombre.isEmpty() && !edadStr.isEmpty() && !raza.isEmpty()) {
                    mascota.setNombre(nombre);
                    mascota.setEdad(Integer.parseInt(edadStr));
                    mascota.setRaza(raza);

                    databaseHelper.actualizarMascota(mascota);
                    cargarMascotas();
                    Toast.makeText(MainActivity.this, "Mascota actualizada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void eliminarMascota(int position) {
        ClsMascotas mascota = listaMascotas.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Eliminar Mascota")
                .setMessage("¿Estás seguro de eliminar a " + mascota.getNombre() + "?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.eliminarMascota(mascota.getId());
                        cargarMascotas();
                        Toast.makeText(MainActivity.this, "Mascota eliminada", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}