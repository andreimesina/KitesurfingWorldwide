package com.andreimesina.kitesurfingworldwide.activity.list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andreimesina.kitesurfingworldwide.R;
import com.andreimesina.kitesurfingworldwide.activity.details.DetailsActivity;
import com.andreimesina.kitesurfingworldwide.core.ServiceProvider;
import com.andreimesina.kitesurfingworldwide.data.model.Spot;

import java.util.List;

public class SpotAdapter extends RecyclerView.Adapter<SpotViewHolder> {

    private Context mContext;
    private List<Spot> mSpots;

    public SpotAdapter(Context context, List<Spot> spots) {
        mContext = context;
        mSpots = spots;
    }

    public void updateData(List<Spot> updatedSpots) {
        mSpots = updatedSpots;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_spot, parent, false);

        return new SpotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpotViewHolder spotViewHolder, int position) {
        Spot currentSpot = mSpots.get(position);

        String name = currentSpot.getName();
        String country = currentSpot.getCountry();

        TextView textViewName = spotViewHolder.itemView.findViewById(R.id.textView_name_item);
        textViewName.setText(name);

        TextView textViewCountry = spotViewHolder.itemView.findViewById(R.id.textView_country_item);
        textViewCountry.setText(country);

        ImageView star = spotViewHolder.itemView.findViewById(R.id.image_star_item);

        if(currentSpot.isFavorite() == true
                && star.getDrawable() != mContext.getResources().getDrawable(R.drawable.star_on)) {
            star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_on));
        } else if(currentSpot.isFavorite() == false
                && star.getDrawable() != mContext.getResources().getDrawable(R.drawable.star_off_list)){
            star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_off_list));
        }

        listenForItemClick(spotViewHolder.itemView, currentSpot.getId());
        listenForFavoriteClick(star, currentSpot.getId(), currentSpot.isFavorite());
    }

    private void listenForItemClick(View view, String spotId) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("spotId", spotId);

                mContext.startActivity(intent);
            }
        });
    }

    private void listenForFavoriteClick(ImageView star, String spotId, boolean isFavorite) {
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavorite) {
                    star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_off_list));
                    ServiceProvider.getInstance().getRepository().removeSpotFromFavorites(spotId);
                } else {
                    star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_on));
                    ServiceProvider.getInstance().getRepository().addSpotToFavorites(spotId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSpots.size();
    }
}
