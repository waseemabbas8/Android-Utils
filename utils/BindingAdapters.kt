package softsolutions.com.tutors.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.hideProgress
import de.hdodenhof.circleimageview.CircleImageView
import softsolutions.com.tutors.R
import softsolutions.com.tutors.ui.GenericRecyclerViewAdapter

@BindingAdapter("imageResource")
fun setImage(view: ImageView, imageResource: Int){
    view.setImageResource(imageResource)
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?){
    Glide.with(view.context).load(imageUrl).into(view)
}

@BindingAdapter(value = ["imageUrl", "default"], requireAll = false)
fun loadImage(view: CircleImageView, imageUrl: String?, default: Drawable?){
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
        android.R.layout.simple_spinner_dropdown_item, items
    )
    view.setAdapter(adapter)
    if (isSpinner){
        view.setOnClickListener { view.showDropDown() } //show drop down like spinner
    }else if (view is MultiAutoCompleteTextView){
        view.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
    }
}

@BindingAdapter("spItemsList")
fun <T> setSpinnerItems(view: Spinner, spItemsList: List<T>){
    val adapter = ArrayAdapter<T>(
        view.context,
        R.layout.item_spinner, spItemsList
    )
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    view.adapter = adapter
}

@BindingAdapter("currentItem")
fun setCurrentItem(view: Spinner, currentItem: Int){
    view.setSelection(currentItem)
}

@BindingAdapter("selectedPos")
fun setSelectedItem(view: Spinner, selectedPos: Int){
    view.setSelection(selectedPos)
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

@BindingAdapter(value = ["isLoading", "newText"], requireAll =  false)
fun setLoading(view: Button, isLoading: Boolean, newText: String?){
    view.attachTextChangeAnimator()
    if (isLoading){
        view.isEnabled = false
    }
    else{
        view.isEnabled = true
        view.hideProgress(newText)
    }

}

@BindingAdapter(value = ["webUrl", "webViewClient"], requireAll = false)
fun loadWebUrl(view: WebView, webUrl: String? = null, webViewClient: WebViewClient? = WebViewClient()){
    view.loadUrl(webUrl)
    view.settings.javaScriptEnabled = true
    view.webViewClient = webViewClient
}
