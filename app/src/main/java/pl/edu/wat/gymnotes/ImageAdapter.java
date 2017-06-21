package pl.edu.wat.gymnotes;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
//            imageView.setPadding(5,5,5,5);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position%4]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.pompka, R.drawable.przysiad,
            R.drawable.wspinaczka,R.drawable.after_1,
            R.drawable.pompka, R.drawable.przysiad,
            R.drawable.wspinaczka,R.drawable.after_1,
            R.drawable.pompka, R.drawable.przysiad,
            R.drawable.wspinaczka,R.drawable.after_1,
            R.drawable.pompka, R.drawable.przysiad,
            R.drawable.wspinaczka,R.drawable.after_1,
            R.drawable.pompka, R.drawable.przysiad,
            R.drawable.wspinaczka,R.drawable.after_1
    };
}
