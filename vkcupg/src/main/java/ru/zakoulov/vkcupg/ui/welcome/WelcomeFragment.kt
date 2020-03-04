package ru.zakoulov.vkcupg.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ru.zakoulov.vkcupg.MainActivity
import ru.zakoulov.vkcupg.R

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginBtn = view.findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            (requireActivity() as MainActivity).login()
        }
        activity?.setTitle(R.string.fragment_welcome_title)
    }

    companion object {
        val instance: WelcomeFragment by lazy { WelcomeFragment() }
        const val TAG = "WelcomeFragment"
    }
}
