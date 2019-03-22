package com.tekit.software.ongc.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tekit.software.ongc.Model.LocationData;
import com.tekit.software.ongc.R;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private Context context;
    private List<LocationData> mLocationDataList;
    private LocationAdapterListener listener;
    private List<LocationData> mLocationDataFilter;


    public LocationAdapter(Context context, List<LocationData> mLocationDataList, LocationAdapterListener listener) {
        this.context = context;
        this.mLocationDataList = mLocationDataList;
        this.listener = listener;
        this.mLocationDataFilter= mLocationDataList;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_well_location, parent, false);
        return new LocationViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        LocationData locationData = mLocationDataFilter.get(position);
        holder.textViewLocation.setText(locationData.getLat()+","+locationData.getLongitude()+","+ locationData.getWellId());


    }





    @Override
    public int getItemCount() {
        return mLocationDataFilter.size();
    }





    public void updateList(List<LocationData> mLocationDataListFilterd) {
        mLocationDataFilter = mLocationDataListFilterd;
        notifyDataSetChanged();

    }

    public class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textViewLocation;

        public LocationViewHolder(View itemView) {
            super(itemView);
            textViewLocation =(TextView)itemView.findViewById(R.id.tv_location_data);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listener.onLocationSelected(getAdapterPosition());
        }
    }


    public interface LocationAdapterListener {
        void onLocationSelected(int position);

    }
}
