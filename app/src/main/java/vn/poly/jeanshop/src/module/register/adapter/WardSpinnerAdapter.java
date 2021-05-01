package vn.poly.jeanshop.src.module.register.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.module.register.model.DataWard;

public class WardSpinnerAdapter implements android.widget.SpinnerAdapter {

    private List<DataWard> DataWardList;
    Context context;

    public WardSpinnerAdapter(List<DataWard> dataWardList, Context context) {
        DataWardList = dataWardList;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.spinner, parent, false);
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(DataWardList.get(position).getTitle());

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.spinner, parent, false);
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(DataWardList.get(position).getTitle());

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        return DataWardList.size();
    }

    @Override
    public DataWard getItem(int position) {
        return DataWardList.get(position);
    }






    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }



    @Override
    public int getItemViewType(int position) {
        return 0;
    }



    @Override
    public boolean isEmpty() {
        return false;
    }
}

