package com.example.appjyf;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appjyf.Adapter.PedidosAdapter;
import com.example.appjyf.Adapter.ProductosAdapter;
import com.example.appjyf.Modelo.VolleySingleton;
import com.example.appjyf.SQLite.DBPedidos;

public class BorrarFragment extends DialogFragment {

    Button si, no;
    RequestQueue request;
    StringRequest stringRequest;
    PedidosAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_borrar, container, false);

        si = view.findViewById(R.id.btnSI);
        no = view.findViewById(R.id.btnNO);
        Bundle bundle = this.getArguments();
        String data = bundle.getString("keyped");

        request = Volley.newRequestQueue(getContext());

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(data);
               // borrardatos(id);
                DBPedidos pedidos = new DBPedidos(getContext());
            pedidos.EliminarPedido(id);
            ActualizarFragment();
            dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void ActualizarFragment() {

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
        navController.popBackStack();
        navController.navigate(R.id.navigation_notifications);
    }

    private void borrardatos(String id) {


        String url = "https://jyfindustrial.cl/php_crud/Borrar_Pedido.php?id_ped="+id;

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.trim().equalsIgnoreCase("eliminado")){
                  /*etiNombre.setText("");
                    txtDocumento.setText("");
                    etiProfesion.setText("");
                    campoImagen.setImageResource(R.drawable.img_base);*/
                    dismiss();

                    Toast.makeText(getContext(),"Se ha Eliminado con exito",Toast.LENGTH_SHORT).show();
                }else{
                    if (response.trim().equalsIgnoreCase("noExiste")){
                        Toast.makeText(getContext(),"No se encuentra el pedido",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getContext(),"No se ha Eliminado ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                        dismiss();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
               // pDialog.hide();
            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);
    }


}
