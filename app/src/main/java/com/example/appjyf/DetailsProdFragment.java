package com.example.appjyf;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.appjyf.Modelo.VolleySingleton;
import com.example.appjyf.SQLite.DBPedidos;
import com.example.appjyf.SQLite.DBProductos;
import com.example.appjyf.ui.home.HomeFragment;
import com.example.appjyf.ui.notifications.NotificationsFragment;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DetailsProdFragment extends DialogFragment {

    TextView marca, precio, total, unit, det;
    EditText cantidad, notaProd;
    Button confirmar;
    ImageButton check;
    String numero1, cantidadString, idProd;
    Bitmap bitmap;
    int num1;

    private OnFragmentInteractionListener mListener;

    JsonObjectRequest jsonObjectRequest;

    StringRequest stringRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_prod, container, false);

        marca = view.findViewById(R.id.txtEjemplo);
        precio = view.findViewById(R.id.txtPrecioDetalle);
        det = view.findViewById(R.id.txtdet);
        cantidad = view.findViewById(R.id.EdtCantidad);
        confirmar = view.findViewById(R.id.btnNO);
        total = view.findViewById(R.id.txtTotal);
        check = view.findViewById(R.id.btnChek);
        unit = view.findViewById(R.id.tv_unit);
        notaProd = view.findViewById(R.id.edtNotasProd);


        //Bundle sirve para enviar un dato de un fragment a otro
        //En este caso, este fragment recibe el dato
        //DashboardFragment envia el dato
        Bundle bundle = this.getArguments();
        String data = bundle.getString("key");
        String data2 = bundle.getString("key2");
        String data3 = bundle.getString("key3");
        String data4 = bundle.getString("key4");
        String data5 = bundle.getString("key5");
        marca.setText(data);
        precio.setText("  Precio: $" + data2);
        det.setText("Detalle: " + data3);
        idProd = data4;
        unit.setText("  Cantidades: "+data5);


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cantidad.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Indica una cantidad", Toast.LENGTH_SHORT).show();
                }else {
                    num1 = Integer.valueOf(cantidad.getText().toString()).intValue();
                    int num2 = Integer.parseInt(data2);
                    int res = num1 * num2;
                    numero1 = String.valueOf(res);
                    cantidadString = String.valueOf(num1);
                    total.setText("  Total: $" + Integer.toString(res) + " +iva");
                    cantidad.setText("");
                    confirmar.setEnabled(true);
                }
            }
        });


        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RegistrarPedido();

                int num2 = Integer.parseInt(data2);
                int res = num1 * num2;
                cantidadString = String.valueOf(num1);

                DBPedidos pedidos = new DBPedidos(getContext());
                long id = pedidos.insertarPedidos(num1,res,Integer.parseInt(data4),notaProd.getText().toString());

                if (id > 0){
                    Toast.makeText(getContext(), "Pedido Registrado ", Toast.LENGTH_SHORT).show();
                    dismiss();
                }else {
                    Toast.makeText(getContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
                }
               // dismiss();
            }
        });

        return view;
    }


    private void RegistrarPedido() {

           /* progreso=new ProgressDialog(getContext());
            progreso.setMessage("Cargando...");
            progreso.show();*/

        //  String ip=getString(R.string.ip);

        String url = "https://jyfindustrial.cl/php_crud/RegistroPedidos.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                // progreso.hide();

                if (response.trim().equalsIgnoreCase("registra")) {
                    Toast.makeText(getContext(), "Se ha registrado con exito", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "No se ha registrado ", Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ", "" + response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                // progreso.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String cant = cantidadString.toString();
                // String total2=campoNombre.getText().toString();
                String tt = numero1.toString();
                String prod = idProd.toString();

                Map<String, String> parametros = new HashMap<>();
                parametros.put("cant_ped", cant);
                parametros.put("total_ped", tt);
                parametros.put("id_prod", prod);

                return parametros;
            }
        };
        //request.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);
    }

    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);

        return imagenString;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

