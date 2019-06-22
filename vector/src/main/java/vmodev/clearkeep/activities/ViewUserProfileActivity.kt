package vmodev.clearkeep.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import dagger.android.support.DaggerAppCompatActivity
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorRoomActivity
import im.vector.databinding.ActivityViewUserProfileBinding
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.rest.callback.ApiCallback
import org.matrix.androidsdk.rest.model.MatrixError
import vmodev.clearkeep.activities.interfaces.IViewUserProfileActivity
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewUserProfileActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserViewModel
import java.util.HashMap
import javax.inject.Inject

class ViewUserProfileActivity : DataBindingDaggerActivity(), IViewUserProfileActivity {

    @Inject
    lateinit var viewModelFactory: IViewUserProfileActivityViewModelFactory;

    private lateinit var binding: ActivityViewUserProfileBinding;
    private lateinit var session: MXSession;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_user_profile, dataBindingComponent);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.profile);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            kotlin.run {
                onBackPressed();
            }
        }
        val userId = intent.getStringExtra(USER_ID);
        session = Matrix.getInstance(applicationContext).defaultSession;
        binding.buttonMessage.setOnClickListener {
            viewModelFactory.getViewModel().setUserIdForCreateNewChat(userId);
        }
        binding.buttonCall.setOnClickListener {
            viewModelFactory.getViewModel().setUserIdForCreateNewChat(userId);
        }
        binding.user = viewModelFactory.getViewModel().getUserResult();
        binding.room = viewModelFactory.getViewModel().createNewDirectChatResult();
        viewModelFactory.getViewModel().createNewDirectChatResult().observe(this, Observer {
            it?.data?.let {
                joinRoom(it.id);
            }
        })
        binding.lifecycleOwner = this;
        viewModelFactory.getViewModel().setGetUser(userId);
    }

    companion object {
        const val USER_ID = "USER_ID";
    }

    private fun joinRoom(roomId: String) {
        val room = session.dataHandler.store.getRoom(roomId);
        session.joinRoom(room!!.getRoomId(), object : ApiCallback<String> {
            override fun onSuccess(roomId: String) {
                val params = HashMap<String, Any>()
                params[VectorRoomActivity.EXTRA_MATRIX_ID] = session.getMyUserId()
                params[VectorRoomActivity.EXTRA_ROOM_ID] = room!!.getRoomId()

                CommonActivityUtils.goToRoomPage(this@ViewUserProfileActivity, session, params)
                finish();
            }

            private fun onError(errorMessage: String) {
                Toast.makeText(this@ViewUserProfileActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onNetworkError(e: Exception) {
                onError(e.localizedMessage)
            }

            override fun onMatrixError(e: MatrixError) {
                if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {

                } else {
                    onError(e.localizedMessage)
                }
            }

            override fun onUnexpectedError(e: Exception) {
                onError(e.localizedMessage)
            }
        })
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }
}
