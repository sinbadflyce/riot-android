package vmodev.clearkeep.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import im.vector.databinding.FragmentShareAppBinding
import vmodev.clearkeep.fragments.Interfaces.IFragment
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import im.vector.R
import vmodev.clearkeep.ultis.OnSingleClickListener
import javax.inject.Inject


class ShareAppFragment : DataBindingDaggerFragment(), IFragment {

    private lateinit var binding: FragmentShareAppBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_share_app, container, false, dataBinding.getDataBindingComponent());

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEvent()
    }

    private fun setEvent() {
        binding.btnShare.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                shareApp()
            }
        })
    }

    private fun shareApp() {
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, resources.getString(R.string.riot_app_name))
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, resources.getString(R.string.url_share_app))
        startActivity(Intent.createChooser(sharingIntent, resources.getString(R.string.riot_app_name)))

    }

    override fun getFragment(): Fragment {
        return this
    }


}