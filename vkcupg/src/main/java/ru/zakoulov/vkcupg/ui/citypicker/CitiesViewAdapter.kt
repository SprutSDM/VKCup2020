package ru.zakoulov.vkcupg.ui.citypicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.callbackFlow
import ru.zakoulov.vkcupg.R
import ru.zakoulov.vkcupg.data.models.City

class CitiesViewAdapter(
    cities: List<City>,
    private val selectedCity: City,
    private val callback: CityPickerCallback
) : RecyclerView.Adapter<CitiesViewAdapter.CityViewHolder>() {

    var cities: List<City> = cities
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val cityView = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_item, parent, false) as View
        return CityViewHolder(cityView)
    }

    override fun getItemCount() = cities.size

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        holder.apply {
            setName(city.name)
            setSelected(city.id == selectedCity.id)
        }
        holder.cityItem.setOnClickListener {
            callback.pickCity(city)
        }
    }

    class CityViewHolder(val cityItem: View) : RecyclerView.ViewHolder(cityItem) {

        private val cityName: TextView = cityItem.findViewById(R.id.city_name)
        private val citySelectedImg: ImageView = cityItem.findViewById(R.id.city_selected_img)

        fun setName(name: String) {
            cityName.text = name
        }

        fun setSelected(selected: Boolean) {
            citySelectedImg.visibility = if (selected) View.VISIBLE else View.GONE
        }
    }
}
