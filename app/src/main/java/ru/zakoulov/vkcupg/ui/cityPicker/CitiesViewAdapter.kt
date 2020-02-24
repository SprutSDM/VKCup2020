package ru.zakoulov.vkcupg.ui.cityPicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupg.R
import ru.zakoulov.vkcupg.data.models.City

class CitiesViewAdapter(
    cities: List<City>
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
        }
    }

    class CityViewHolder(val cityItem: View) : RecyclerView.ViewHolder(cityItem) {

        private val cityName: TextView = cityItem.findViewById(R.id.city_name)

        fun setName(name: String) {
            cityName.text = name
        }
    }
}
