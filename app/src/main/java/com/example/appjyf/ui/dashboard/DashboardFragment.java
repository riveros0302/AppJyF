package com.example.appjyf.ui.dashboard;


import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appjyf.Adapter.ProductosAdapter;
import com.example.appjyf.DetailsProdFragment;
import com.example.appjyf.MainActivity;
import com.example.appjyf.Modelo.Productos;
import com.example.appjyf.Modelo.VolleySingleton;
import com.example.appjyf.R;
import com.example.appjyf.SQLite.DBContract;
import com.example.appjyf.SQLite.DBProductos;
import com.example.appjyf.SQLite.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardFragment extends Fragment{


    SwipeRefreshLayout refreshLayout;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    //BD Remota
    public RecyclerView recyclerProd;
    static ArrayList<Productos> listProd;
    //ProgressDialog progress;
    RequestQueue request;
    JsonObjectRequest objectRequest;
    TextView ejemplo;
    ProductosAdapter adapter;

    //BD SQLite
    //static ArrayList<ProductosLite> listProd;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       View root = inflater.inflate(R.layout.fragment_dashboard, container, false);


       refreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refreshProd);
       refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               //--------------------------------------------

               ExecutorService service = Executors.newSingleThreadExecutor();
               service.execute(new Runnable() {
                   @Override
                   public void run() {
                       //onPreExecute
                       getActivity().runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
//

                           }
                       });

                       //doInBackground

                       sincronizarProductos();

                       //onPostExecute
                       getActivity().runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               Toast.makeText(getContext(), "Sincronizando...", Toast.LENGTH_SHORT).show();
                               readProductos(getContext());
                               NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                               navController.popBackStack();
                               navController.navigate(R.id.navigation_dashboard);
                               adapter.notifyDataSetChanged();
                               refreshLayout.setRefreshing(false);
                           }
                       });
                   }
               });
           }
       });

        listProd= new ArrayList<>();
        recyclerProd = root.findViewById(R.id.RviewProductos);
        recyclerProd.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerProd.setHasFixedSize(true);
        readProductos(getContext());

        DBProductos dbProductos = new DBProductos(getContext());
        //SQLite
        adapter = new ProductosAdapter(dbProductos.mostrarProductos(), getContext());
        recyclerProd.setAdapter(adapter);


        request = Volley.newRequestQueue(getContext());
        //MySql
       // MostrarProductos();

        //SQLite
       // CrearBDLite();

        adapter= new ProductosAdapter(listProd, getContext());
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //aqui se envia el dato hacia DeatailsProdFragment
                Bundle bundle = new Bundle();
                bundle.putString("key", listProd.get(recyclerProd.getChildAdapterPosition(v)).getMarca_prod());
                bundle.putString("key2", listProd.get(recyclerProd.getChildAdapterPosition(v)).getPrecio_prod().toString());
                bundle.putString("key3", listProd.get(recyclerProd.getChildAdapterPosition(v)).getDesc_prod());
                bundle.putString("key4",listProd.get(recyclerProd.getChildAdapterPosition(v)).getId_prod().toString());
                bundle.putString("key5",listProd.get(recyclerProd.getChildAdapterPosition(v)).getUnit_prod());

                DetailsProdFragment fragment = new DetailsProdFragment();
                fragment.setArguments(bundle);
                fragment.show(getChildFragmentManager(),"MyFragment");

            }
        });
        recyclerProd.setAdapter(adapter);

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

 /*   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    adapter.getFilter().filter(newText);
                    //IMPORTANTE!!! el setAdapter(adapter) siguiente hace que se actualice el recyclerView
                    //osea que muestra los productos que estamos filtrando en searchview
                   // recyclerProd.setAdapter(adapter);
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }*/


  /*  private void MostrarProductos() {
        progress = new ProgressDialog(getContext());
        progress.setMessage("Consultando...");
        progress.show();
        //String url = "https://jyfindustrial.cl/php_crud/Lista_Productos.php";
        String url = "https://jyfindustrial.cl/php_crud/ListaProductosURL.php";
        objectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(objectRequest);
    }



    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_SHORT).show();
        Log.d("Error: ", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Productos productos = null;

        JSONArray json=response.optJSONArray("productos");
        try {
        for (int i=0; i<json.length();i++){
            productos=new Productos();
            JSONObject object=null;
            object=json.getJSONObject(i);

            productos.setMarca_prod(object.optString("marca_prod"));
            productos.setDesc_prod(object.optString("desc_prod"));
            productos.setPrecio_prod(object.optInt("precio_prod"));
            productos.setRuta_img(object.optString("ruta_img"));
            productos.setId_prod(object.optInt("id_prod"));
            listProd.add(productos);
            }
            ProductosAdapter adapter= new ProductosAdapter(listProd, getContext());
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //aqui se envia el dato hacia DeatailsProdFragment
                    Bundle bundle = new Bundle();
                    bundle.putString("key", listProd.get(recyclerProd.getChildAdapterPosition(v)).getMarca_prod());
                    bundle.putString("key2", listProd.get(recyclerProd.getChildAdapterPosition(v)).getPrecio_prod().toString());
                    bundle.putString("key3", listProd.get(recyclerProd.getChildAdapterPosition(v)).getDesc_prod());
                    bundle.putString("key4",listProd.get(recyclerProd.getChildAdapterPosition(v)).getId_prod().toString());
                    DetailsProdFragment fragment = new DetailsProdFragment();
                    fragment.setArguments(bundle);
                    fragment.show(getChildFragmentManager(),"MyFragment");

                   // Toast.makeText(getContext(), "Selección: "+listProd.get(recyclerProd.getChildAdapterPosition(v)).getId_prod(), Toast.LENGTH_SHORT).show();
                }
            });
            recyclerProd.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexión con el Servidor", Toast.LENGTH_SHORT).show();
        //    progress.hide();
        }
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    public static boolean checkNetworkConection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    public void CrearBDLite(){
        Database database = new Database(getContext());
        SQLiteDatabase db = database.getWritableDatabase();
        if (db != null){
            Toast.makeText(getContext(), "Base de datos Creada", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "Fallo al crear BD", Toast.LENGTH_SHORT).show();
        }
    }

    public void sincronizarProductos(){

        if (checkNetworkConection(getContext())){
            final Database database = new Database(getContext());
            final SQLiteDatabase db = database.getWritableDatabase();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://jyfindustrial.cl/php_crud/Lista_Productos_sqlite.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    database.DeleteProductos(db);

                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i<array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            database.savedProductos(object.getInt("id_prod"),object.getString("nom_prod"),object.getString("desc_prod"),
                                    object.getString("marca_prod"),object.getInt("precio_prod"),object.getString("ruta_img"), object.getString("cant_prod"), db);
                           // readProductos(getContext());

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                  //  Toast.makeText(getContext(), "Error al sincronizar: "+error.toString(), Toast.LENGTH_SHORT).show();

                }
            });
            VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);

        }
    }

    public static void readProductos(Context context){
        listProd.clear();
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor c = database.readProductos(db);
        while (c.moveToNext()){
            listProd.add(new Productos(c.getInt(c.getColumnIndex(DBContract.productos.ID)),
                    c.getString(c.getColumnIndex(DBContract.productos.NOMBRE)),
                    c.getString(c.getColumnIndex(DBContract.productos.DESCRIPCION)),
                    c.getString(c.getColumnIndex(DBContract.productos.MARCA)),
                    c.getInt(c.getColumnIndex(DBContract.productos.PRECIO)),
                    c.getString(c.getColumnIndex(DBContract.productos.RUTA_IMG)),
                    c.getString(c.getColumnIndex(DBContract.productos.UNIT))));

        }
        ProductosAdapter adapter= new ProductosAdapter(listProd, context);
        adapter.notifyDataSetChanged();
        c.close();
        database.close();
    }

    public static Bitmap convertStringToBitmap(String string) {
        byte[] byteArray1;
        byteArray1 = Base64.decode(string, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray1, 0,
                byteArray1.length);
        return bmp;
    }


}
