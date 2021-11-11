package com.example.appjyf.ui.home;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appjyf.Adapter.PedidosAdapter;
import com.example.appjyf.Modelo.Pedidos;
import com.example.appjyf.Modelo.Usuarios;
import com.example.appjyf.R;
import com.example.appjyf.SQLite.DBContract;
import com.example.appjyf.SQLite.DBProductos;
import com.example.appjyf.SQLite.DBUsuarios;
import com.example.appjyf.SQLite.Database;
import com.example.appjyf.databinding.FragmentHomeBinding;
import com.example.appjyf.ui.notifications.NotificationsFragment;

import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    public EditText nom, ape, empresa, correo, fono, comuna;

    public Button guardar;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    DBUsuarios usuarios;
    int ID = 1;





    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);


        nom = root.findViewById(R.id.edtNombre);
        ape = root.findViewById(R.id.edtApe);
        empresa = root.findViewById(R.id.edtEmpresa);
        correo = root.findViewById(R.id.edtCorreo);
        comuna = root.findViewById(R.id.edtComuna);
        fono = root.findViewById(R.id.edtFono);
        guardar = root.findViewById(R.id.btnGuardar);

        usuarios = new DBUsuarios(getContext());

        tablavacia();


        request = Volley.newRequestQueue(getContext());


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id;

               // DBUsuarios usuarios = new DBUsuarios(getContext());

                if (guardar.getText().equals("Actualizar")){

                   usuarios.updateUsuario(nom.getText().toString(), ape.getText().toString(), empresa.getText().toString(),
                            correo.getText().toString(), fono.getText().toString(), comuna.getText().toString(), ID);

                    Toast.makeText(getContext(), "Datos Actualizados: "+nom.getText().toString(), Toast.LENGTH_SHORT).show();


                }else{

                 id = usuarios.insertarUsuario(nom.getText().toString(), ape.getText().toString(), empresa.getText().toString(),
                        correo.getText().toString(), fono.getText().toString(), comuna.getText().toString());

                if (id > 0){
                    Toast.makeText(getContext(), "Datos guardados", Toast.LENGTH_SHORT).show();
                    guardar.setText("Actualizar");
                }else {
                    Toast.makeText(getContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            }}
        });


        return root;
    }


    public void tablavacia(){
        Database database = new Database(getContext());
        SQLiteDatabase dbLite = database.getWritableDatabase();

        String count = "SELECT count(*) FROM "+DBContract.TABLE_USER;

        Cursor mcursor = dbLite.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0) {
            usuarios.mostrarUsuario(nom, ape, empresa, correo, fono, comuna, ID);
            //Toast.makeText(getContext(), "Mostrando usuario", Toast.LENGTH_SHORT).show();
            guardar.setText("Actualizar");
        }else{
            Toast.makeText(getContext(), "Completa el formulario con tus datos", Toast.LENGTH_SHORT).show();
        }}


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

   /* @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(getContext(), "No se pudo hacer la consulta", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {

        Toast.makeText(getContext(), "Cargando: "+response, Toast.LENGTH_SHORT).show();
        Productos productos = new Productos();
        JSONArray array= response.optJSONArray("productos");
        JSONObject object= null;

        object = array.optJSONObject(0);
        productos.setNom_prod(object.optString("nom_prod"));
        productos.setDesc_prod(object.optString("desc_prod"));

        txtnom.setText(productos.getNom_prod());
        txtdesc.setText(productos.getDesc_prod());
    }

       private void CargarDatos() {
        String url = "https://jyfindustrial.cl/php_crud/Consulta_Producto.php?id_prod="+id.getText().toString();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,this,this);
        request.add(jsonObjectRequest);
    }*/
}