package apidez.com.firebase.utils.view;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.widget.ImageView;

/**
 * Created by nongdenchet on 1/5/16.
 */
public class BindingUtils {

    @BindingAdapter({"android:src"})
    public static void src(ImageView imageView, int resource) {
        imageView.setBackgroundResource(resource);
    }

    @BindingAdapter({"android:background"})
    public static void background(AppCompatImageView imageView, int resource) {
        Drawable drawable = ContextCompat.getDrawable(imageView.getContext(), resource);
        imageView.setBackgroundDrawable(drawable);
    }
}
