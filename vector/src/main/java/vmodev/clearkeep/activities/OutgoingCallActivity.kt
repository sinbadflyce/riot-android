package vmodev.clearkeep.activities

import android.content.ComponentCallbacks2
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.databinding.ActivityOutgoingCallBinding
import im.vector.util.CallsManager
import org.matrix.androidsdk.call.IMXCall
import vmodev.clearkeep.activities.interfaces.IActivity

class OutgoingCallActivity : DataBindingDaggerActivity(), IActivity, ComponentCallbacks2 {

    private lateinit var binding: ActivityOutgoingCallBinding
    private lateinit var navController: NavController
    private var mxCall: IMXCall? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_outgoing_call, dataBinding.getDataBindingComponent())
        mxCall = CallsManager.getSharedInstance().activeCall
        navController = findNavController(R.id.fragment)
        mxCall?.let {
            if (it.isVideo) {
                navController.navigate(R.id.outgoingVideoCallFragment)
            } else {
                navController.navigate(R.id.outgoingVoiceCallFragment)
            }
        }
    }

    override fun getActivity(): FragmentActivity {
        return this


    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        CommonActivityUtils.onTrimMemory(this, level)
    }

    override fun onDestroy() {
        super.onDestroy()
        mxCall = null
    }

}
