package com.example.minigames.gameBasic.memoryGame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.Random;

import com.example.minigames.R;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    int count = 0;
    boolean check = false;
    private Integer[] androidPhotos;

    private int sizeOfSolved;

    public ImageAdapter(Context context, boolean shuffle) {
        this.context = context;

        if (check == false) {
            androidPhotos = new Integer[]{R.drawable.android,
                    R.drawable.androideclair,
                    R.drawable.androidfroyo,
                    R.drawable.androidgingerbread,
                    R.drawable.androidicecream,
                    R.drawable.androidjellybean,
                    R.drawable.androidkitkat,
                    R.drawable.androidlollipop,
                    R.drawable.androidmarshmallow,
                    R.drawable.androidnougat,
                    R.drawable.android,
                    R.drawable.androideclair,
                    R.drawable.androidfroyo,
                    R.drawable.androidgingerbread,
                    R.drawable.androidicecream,
                    R.drawable.androidjellybean,
                    R.drawable.androidkitkat,
                    R.drawable.androidlollipop,
                    R.drawable.androidmarshmallow,
                    R.drawable.androidnougat};
        }
        if (shuffle) {
            shuffleArray();
        }
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return androidPhotos.length;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView card;
        if (convertView == null) {
            card = new ImageView(context);
            card.setLayoutParams(new GridView.LayoutParams(85, 85));
            card.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } else {
            card = (ImageView) convertView;
        }
        if (check) {
            if (position < sizeOfSolved) {
                card.setImageResource(androidPhotos[position]);
                card.setTag(R.drawable.checkmark);
            } else {
                card.setImageResource(R.drawable.placeholder);
                card.setTag(R.drawable.placeholder);
            }
        } else {
            card.setImageResource(R.drawable.placeholder);
            card.setTag(R.drawable.placeholder);
        }
        ViewGroup.LayoutParams imagelayout = card.getLayoutParams();
        imagelayout.width = 200;
        imagelayout.height = 200;
        card.setLayoutParams(imagelayout);
        return card;
    }

    public void shuffleArray() {
        Random random = new Random();
        for (int i = androidPhotos.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int a = androidPhotos[index];
            androidPhotos[index] = androidPhotos[i];
            androidPhotos[i] = a;
        }
    }

    public Integer[] getArray() {
        return androidPhotos;
    }

    public void setArray(Integer[] newArray) {
        androidPhotos = newArray;
    }

    public void updateAdapter(Integer[] newArray, int newSize) {
        androidPhotos = null;
        androidPhotos = newArray;
        check = true;
        sizeOfSolved = newSize;
        notifyDataSetChanged();
    }
}
