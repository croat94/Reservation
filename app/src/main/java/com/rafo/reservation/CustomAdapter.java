package com.rafo.reservation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Rezervacija>{

    protected Context mContext;

    public CustomAdapter(Context context, Rezervacija[] values) {
        super(context, R.layout.predlozak_my_reservations, values);
        mContext = context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.predlozak_my_reservations, null);
            holder = new ViewHolder();
            holder.nazivPredmeta = (TextView) convertView.findViewById(R.id.nazivPredmeta);
            holder.inventarskiBroj = (TextView) convertView.findViewById(R.id.inventarskiBroj);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.linearLayout);
            holder.startDate = (TextView) convertView.findViewById(R.id.textStartDate);
            holder.endDate = (TextView) convertView.findViewById(R.id.textEndDate);
            holder.vraceno = (CheckBox) convertView.findViewById(R.id.checkBoxReturned);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Rezervacija rezervacija = getItem(position);
        holder.nazivPredmeta.setText(rezervacija.getNaziv());
        holder.inventarskiBroj.setText(rezervacija.getInventoryNumber());
        holder.startDate.setText(rezervacija.getStartDate());
        holder.endDate.setText(rezervacija.getEndDate());
        holder.vraceno.setChecked(rezervacija.getVraceno());
        holder.vraceno.setClickable(false);
        return convertView;
    }


    static class ViewHolder{
        TextView nazivPredmeta;
        TextView inventarskiBroj;
        TextView startDate;
        TextView endDate;
        CheckBox vraceno;
        LinearLayout layout;
    }

}