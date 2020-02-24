package ru.zakoulov.vkcupf.ui.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ru.zakoulov.vkcupf.MainActivity
import ru.zakoulov.vkcupf.R

class ErrorFragment : Fragment() {

    private lateinit var butRetry: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_error, container, false)
        with (root) {
            butRetry = findViewById(R.id.but_reload)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        butRetry.setOnClickListener {
            (requireActivity() as MainActivity).navigateToGroups(true)
        }
    }

    companion object {
        val INSTANCE: ErrorFragment by lazy { ErrorFragment() }

        const val TAG = "ErrorFragment"
    }
}
