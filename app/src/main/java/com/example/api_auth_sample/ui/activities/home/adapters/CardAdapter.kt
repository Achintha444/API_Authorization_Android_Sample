//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.api_auth_sample.R
//import com.example.api_auth_sample.ui.data.Doctor
//import com.example.api_auth_sample.ui.data.Pharmacy
//
//class CardAdapter(private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    companion object {
//        const val VIEW_TYPE_DOCTOR = 1
//        const val VIEW_TYPE_PHARMACY = 2
//    }
//
//    // ViewHolder for Doctor
//    class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val nameTextView: TextView = itemView.findViewById(R.id.doctorNameTextView)
//        val descriptionTextView: TextView = itemView.findViewById(R.id.doctorDescriptionTextView)
//        val locationTextView: TextView = itemView.findViewById(R.id.doctorLocationTextView)
//    }
//
//    // ViewHolder for Pharmacy
//    class PharmacyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val nameTextView: TextView = itemView.findViewById(R.id.pharmacyNameTextView)
//        val descriptionTextView: TextView = itemView.findViewById(R.id.pharmacyDescriptionTextView)
//        val locationTextView: TextView = itemView.findViewById(R.id.pharmacyLocationTextView)
//    }
//
//    // ViewHolder for Separator
//    class SeparatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            VIEW_TYPE_DOCTOR -> {
//                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doctor, parent, false)
//                DoctorViewHolder(view)
//            }
//            VIEW_TYPE_PHARMACY -> {
//                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pharamcy, parent, false)
//                PharmacyViewHolder(view)
//            }
//            else -> throw IllegalArgumentException("Invalid view type")
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (holder) {
//            is DoctorViewHolder -> {
//                val doctor = items[position] as Doctor
//                holder.nameTextView.text = doctor.name
//                holder.descriptionTextView.text = doctor.description
//                holder.locationTextView.text = doctor.location
//            }
//            is PharmacyViewHolder -> {
//                val pharmacy = items[position] as Pharmacy
//                holder.nameTextView.text = pharmacy.name
//                holder.descriptionTextView.text = pharmacy.description
//                holder.locationTextView.text = pharmacy.location
//            }
//            // No binding needed for SeparatorViewHolder
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return when (items[position]) {
//            is Doctor -> VIEW_TYPE_DOCTOR
//            is Pharmacy -> VIEW_TYPE_PHARMACY
//            else -> throw IllegalArgumentException("Invalid item type")
//        }
//    }
//}
