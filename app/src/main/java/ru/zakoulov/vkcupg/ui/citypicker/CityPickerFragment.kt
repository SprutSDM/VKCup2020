package ru.zakoulov.vkcupg.ui.citypicker

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.zakoulov.vkcupg.R

class CityPickerFragment : BottomSheetDialogFragment() {

    private lateinit var butCloseCityPicker: View
    private lateinit var recyclerView: RecyclerView

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: CitiesViewAdapter
    private lateinit var bsBehavior: BottomSheetBehavior<FrameLayout>

    override fun getTheme() = R.style.VkCupTheme_BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_city_picker, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view_cities)
            butCloseCityPicker = findViewById(R.id.close_city_picker)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewManager = LinearLayoutManager(this.context)
        viewAdapter = CitiesViewAdapter(emptyList())
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bsBehavior = bottomSheetDialog.behavior
        bsBehavior.skipCollapsed = true
        bottomSheetDialog.setOnShowListener {
            bsBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }
}
