package com.example.appjyf.Adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.appjyf.Modelo.Productos;

import java.util.List;

public class MyDiffUtil extends DiffUtil.Callback{

    List<Productos> oldProd;
    List<Productos> newProd;

    public MyDiffUtil(List<Productos> oldProd, List<Productos> newProd){
        this.newProd = newProd;
        this.oldProd = oldProd;
    }

    @Override
    public int getOldListSize() {
        return oldProd.size();
    }

    @Override
    public int getNewListSize() {
        return newProd.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldProd.get(oldItemPosition).getId_prod() == newProd.get(newItemPosition).getId_prod();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldProd.get(oldItemPosition).equals(newProd.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
