package com.example.appjyf.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.appjyf.Modelo.Pedidos;
import com.example.appjyf.Modelo.Productos;
import com.example.appjyf.R;
import com.example.appjyf.ui.notifications.NotificationsFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidosHolder> implements View.OnClickListener {

    List<Pedidos> listPed;
    private View.OnClickListener listener;
    ProductosAdapter adapter;
    Context context;
    RequestQueue requestQueue;
    NotificationsFragment fragment;

    public PedidosAdapter(List<Pedidos> listPed, Context context){
        this.listPed=listPed;
        this.context=context;
        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @NotNull
    @Override
    public PedidosHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.pedidoview,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);

        return new PedidosHolder(vista);
    }

    @Override
    public void onBindViewHolder(PedidosHolder holder, int position) {

        FragmentActivity activity = new FragmentActivity();
        FragmentManager manager = activity.getSupportFragmentManager();
        fragment = new NotificationsFragment();

        holder.cant.setText(listPed.get(position).getNota_ped());
        holder.total.setText("$"+listPed.get(position).getTotal_ped().toString());
       // holder.desc.setText(listPed.get(position).getDesc_prod());
       // holder.desc.setText(listPed.get(position).getProd().toString());
        fragment.ConsultarProducto(listPed.get(position).getProd(),holder.desc, holder.unit, context);


       // holder.btnProd.setText("$"+listProd.get(position).getPrecio_prod().toString()+"+iva");

      /*  if(listPed.get(position).getImg_ped()!=null){
            holder.imgPed.setImageBitmap(listPed.get(position).getImg_ped());
        }else{
            holder.imgPed.setImageResource(R.drawable.ic_home_black_24dp);
        }*/

    }

    @Override
    public int getItemCount() {
        return listPed.size();
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

    public class PedidosHolder extends RecyclerView.ViewHolder{

        TextView cant, total, desc, unit;
        ImageView imgPed;
        public PedidosHolder(View Itemview){
            super(Itemview);
            cant = Itemview.findViewById(R.id.txtCantidadPedido);
            total = Itemview.findViewById(R.id.txtTotalPedido);
            unit = Itemview.findViewById(R.id.txt_Unit);
            imgPed = Itemview.findViewById(R.id.ImgViewPedido);
            desc = Itemview.findViewById(R.id.txtDescripcionPedido);

        }
    }
}
