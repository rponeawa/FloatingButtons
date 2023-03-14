import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rpone.floatingbuttons.R
import com.rpone.floatingbuttons.model.ButtonInfo

class ItemAdapter(
    private val context: Context,
    private val dataset: List<ButtonInfo>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val fileNameTextView: TextView = view.findViewById(R.id.file_name_text)
        val buttonsNamesTextView: TextView = view.findViewById(R.id.buttons_names_text)
        val editButton: Button = view.findViewById(R.id.edit_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.fileNameTextView.text = item.fileName
        holder.buttonsNamesTextView.text = item.buttonsNames
    }

    override fun getItemCount() = dataset.size
}