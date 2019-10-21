package softsolutions.com.tutors.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import softsolutions.com.tutors.ui.GenericRecyclerViewAdapter

@BindingAdapter("imageResource")
fun setImage(view: ImageView, imageResource: Int){
    view.setImageResource(imageResource)
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String){
    Glide.with(view.context).load(imageUrl).into(view)
}

@BindingAdapter(value = ["imageUrl", "default"], requireAll = false)
fun loadImage(view: CircleImageView, imageUrl: String, default: Drawable?){
    if (default == null){
        Glide.with(view.context).load(imageUrl).into(view)
    }else{
        Glide.with(view.context).load(imageUrl).placeholder(default).into(view)
    }
}

@BindingAdapter(value = ["itemsList", "itemLayout", "itemClickListener", "hasFixSize"], requireAll = false)
fun <T> setItems(view: RecyclerView, itemsList: List<T>, layout: Int,
                 itemClickListener: OnListItemClickListener<T>?, hasFixSize: Boolean = false){
    val mAdapter = GenericRecyclerViewAdapter(itemsList, layout)
    view.adapter = mAdapter
    mAdapter.setItemClickListener(itemClickListener)
    view.setHasFixedSize(hasFixSize)
}

@BindingAdapter(value = ["itemsList", "isSpinner"], requireAll = false)
fun <T> setSpinnerItems(view: AutoCompleteTextView, items: List<T>, isSpinner: Boolean = false){
    val adapter = ArrayAdapter<T>(
        view.context,
        android.R.layout.simple_dropdown_item_1line, items
    )
    view.setAdapter(adapter)
    if (isSpinner){
        view.setOnClickListener { view.showDropDown() } //show drop down like spinner
    }
}

@BindingAdapter("navigation")
fun navigateUp(toolbar: Toolbar, isEnabled: Boolean){
    toolbar.setNavigationOnClickListener {v ->
        v.findNavController().navigateUp()
    }
}

@BindingAdapter("isSelected")
fun select(view: View, isSelected: Boolean){
    view.isSelected = isSelected
}
