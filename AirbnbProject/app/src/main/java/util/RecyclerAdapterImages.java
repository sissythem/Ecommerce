package util;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import fromRESTful.Images;
import gr.uoa.di.airbnbproject.R;

import static util.Utils.DELETE_ACTION;
import static util.Utils.SET_AS_MAIN_IMAGE_ACTION;

/** RecyclerView for Residence Images used in EditResidenceActivity **/
public class RecyclerAdapterImages extends RecyclerView.Adapter<RecyclerAdapterImages.ImagesCardHolder> {
    Context context;
    Boolean user;
    ArrayList<Images> images = new ArrayList<>();

    public void setImages(ArrayList<Images> images) {
        this.images = images;
    }

    public RecyclerAdapterImages(Context context, Boolean user, ArrayList<Images> images) {
        this.context    = context;
        this.user       = user;
        this.images     = images;
    }

    @Override
    public ImagesCardHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_images, parent, false);
        return new ImagesCardHolder(view);
    }

    @Override
    public void onBindViewHolder(ImagesCardHolder holder, final int position) {
        Utils.loadResidenceImage(context, holder.rPhoto, images.get(position).getName());
        holder.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Image Options");

                if (!user) {
                    menu.add(0, position, 0, DELETE_ACTION);
                    menu.add(0, position, 1, SET_AS_MAIN_IMAGE_ACTION);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ImagesCardHolder extends RecyclerView.ViewHolder {
        CardView rCardView;
        ImageView rPhoto;

        public ImagesCardHolder(final View itemView) {
            super(itemView);
            rCardView   = (CardView) itemView.findViewById(R.id.rImgCardView);
            rPhoto      = (ImageView) itemView.findViewById(R.id.respic);
        }

        public void setOnCreateContextMenuListener(View.OnCreateContextMenuListener listener) {
            itemView.setOnCreateContextMenuListener(listener);
        }
    }
}
