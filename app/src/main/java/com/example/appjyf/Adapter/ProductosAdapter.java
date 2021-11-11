package com.example.appjyf.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.appjyf.Modelo.Productos;
import com.example.appjyf.R;
import com.example.appjyf.ui.dashboard.DashboardFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductosHolder> implements View.OnClickListener, Filterable{

    ArrayList<Productos> listProdFull;
    ArrayList<Productos> listProd;
    RequestQueue requestQueue;
    Context context;
    private View.OnClickListener listener;

    public ProductosAdapter(ArrayList<Productos> listProd, Context context){
        this.context = context;
        this.listProdFull = listProd;
        this.listProd = new ArrayList<>(listProdFull);
        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @NotNull
    @Override
    public ProductosHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.usuarioview,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new ProductosHolder(vista);
    }

    @Override
    public void onBindViewHolder(ProductosHolder holder, int position) {

       holder.marca.setText(listProd.get(position).getMarca_prod().toString());
       holder.desc.setText(listProd.get(position).getDesc_prod().toString());
       holder.btnProd.setText(" $"+listProd.get(position).getPrecio_prod().toString());
       holder.id.setText(listProd.get(position).getId_prod().toString());

        if(listProd.get(position).getRuta_img()!=null){
           // holder.imgProd.setImageBitmap(listProd.get(position).getImg_prod());
            cargarImageURL(listProd.get(position).getRuta_img(),holder);
        }else{
            holder.imgProd.setImageResource(R.drawable.ic_home_black_24dp);
        }


    }

    private void cargarImageURL(String rutaImg, final ProductosHolder holder){
        String urlImg = "https://jyfindustrial.cl/php_crud/imagenes/"+rutaImg;
        urlImg = urlImg.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(urlImg, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                holder.imgProd.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                holder.imgProd.setImageResource(R.drawable.ic_home_black_24dp);
                Toast.makeText(context, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(imageRequest);
    }

    @Override
    public int getItemCount() {
        return listProd.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    @Override
    public Filter getFilter() {
        return newsFilter;
    }

    private final Filter newsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<Productos> filteredNewList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredNewList.addAll(listProdFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Productos productos: listProdFull){
                    if (productos.getDesc_prod().toLowerCase().contains(filterPattern)){
                        filteredNewList.add(productos);

                    }

                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredNewList;
            results.count = filteredNewList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            listProd.clear();
            listProd.addAll((ArrayList)results.values);
            //Toast.makeText(context, "publishResult", Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }
    };

    public class ProductosHolder extends RecyclerView.ViewHolder{

        TextView desc, marca, id, btnProd;
        ImageView imgProd;

        public ProductosHolder(View Itemview){
            super(Itemview);
            marca = Itemview.findViewById(R.id.txtMarca);
            desc = Itemview.findViewById(R.id.txtDescripcion);
            imgProd = Itemview.findViewById(R.id.ImgViewPedido);
            btnProd = Itemview.findViewById(R.id.textProd);
            id = Itemview.findViewById(R.id.txtID);

        }
    }
}
