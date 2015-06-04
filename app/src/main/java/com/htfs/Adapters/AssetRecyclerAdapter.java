package com.htfs.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.htfs.assettracking.R;
import com.htfs.model.AssetLocationHistory;

import java.util.Collections;
import java.util.List;

/**
 * Created by Vinoth on 26-05-2015.
 */
public class AssetRecyclerAdapter extends RecyclerView.Adapter<AssetRecyclerAdapter.AssetViewHolder> {
    private LayoutInflater layoutInflater;
    private List<AssetLocationHistory> list = Collections.emptyList();

    public AssetRecyclerAdapter(Context context, List<AssetLocationHistory> list) {
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
        Log.d("DATA", list.toString());
    }

    @Override
    public AssetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.assetrecycler_row, parent, false);
        AssetViewHolder assetViewHolder = new AssetViewHolder(view);
        return assetViewHolder;
    }

    @Override
    public void onBindViewHolder(AssetViewHolder holder, int position) {
        AssetLocationHistory data = list.get(position);
        holder.asstID.setText(data.getAssetID());
        holder.asstName.setText(data.getAssetDesc());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AssetViewHolder extends RecyclerView.ViewHolder {
        private TextView asstID, asstName;

        public AssetViewHolder(View itemView) {
            super(itemView);
            asstID = (TextView) itemView.findViewById(R.id.vaAssetCode);
            asstName = (TextView) itemView.findViewById(R.id.vaAssetName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), list.get(getLayoutPosition()).toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
