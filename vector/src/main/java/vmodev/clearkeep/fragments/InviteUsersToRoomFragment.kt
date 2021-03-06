package vmodev.clearkeep.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.jakewharton.rxbinding3.widget.textChanges
import im.vector.R
import im.vector.databinding.ActivityInviteUsersToRoomBinding
import im.vector.extensions.hideKeyboard
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.adapters.ListUserToInviteRecyclerViewAdapter
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractInviteUsersToRoomActivityViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2

class InviteUsersToRoomFragment : DataBindingDaggerFragment(), IFragment {


    @Inject
    lateinit var applcation: IApplication

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractInviteUsersToRoomActivityViewModel>
    @Inject
    lateinit var appExecutors: AppExecutors
    private var listUserSuggested: List<User>? = null
    private val listSelected = HashMap<String, User>()
    private lateinit var binding: ActivityInviteUsersToRoomBinding
    private val args: InviteUsersToRoomFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_invite_users_to_room, container, false, dataBinding.getDataBindingComponent())
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listUserAdapter = ListUserToInviteRecyclerViewAdapter(appExecutors = appExecutors, listSelected = listSelected, dataBindingComponent = dataBinding.getDataBindingComponent()
                , diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id
            }

            override fun areContentsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id
            }
        }) { user, status ->
            if (listSelected.size > 0) {
                binding.btnCreate.isEnabled = true
                binding.btnCreate.isSelected = true

            } else {
                binding.btnCreate.isEnabled = false
                binding.btnCreate.isSelected = false
            }
        }
        args.listUser?.let { listUserAdapter.setKeySelected(it) }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.users = viewModelFactory.getViewModel().getUsers()
        binding.recyclerViewListUser.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewListUser.adapter = listUserAdapter

        viewModelFactory.getViewModel().getListUserSuggested(1, application.getUserId()).observe(viewLifecycleOwner, Observer {
            listUserAdapter.submitList(it?.data)
            listUserSuggested = it?.data
        })

        viewModelFactory.getViewModel().getUsers().observe(viewLifecycleOwner, Observer
        { t ->
            if (!TextUtils.isEmpty(binding.editTextQuery.text.toString())) {
                listUserAdapter.submitList(t?.data)
//                listSelected.clear()
            }
        })
        viewModelFactory.getViewModel().joinRoomResult().observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it.status) {
                    Status.ERROR -> {
                        Toast.makeText(this.activity, it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        it.data?.let {
                            val roomIntent = Intent(this.activity, RoomActivity::class.java)
                            roomIntent.putExtra(RoomActivity.EXTRA_MATRIX_ID, application.getUserId())
                            roomIntent.putExtra(RoomActivity.EXTRA_ROOM_ID, it.id)
                            roomIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(roomIntent)
                            this.activity?.finish()
                        }
                    }
                    else -> {
                    }
                }
            }
        })
        viewModelFactory.getViewModel().getInviteUsersToRoomResult().observe(viewLifecycleOwner, Observer
        { t ->
            t?.let {
                when (it.status) {
                    Status.ERROR -> {
                        Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        it.data?.let {
                            binding.room = viewModelFactory.getViewModel().joinRoomResult()
                            viewModelFactory.getViewModel().setJoinRoom(it.id)
                        }
                    }
                    else -> {
                    }
                }
            }
        })
        var disposable: Disposable? = null
        binding.editTextQuery.textChanges().subscribe({
            disposable?.let { disposable -> disposable.dispose() }
            disposable = Observable.timer(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers
                    .mainThread()).subscribe { time: Long? ->
                if (it.length < 1 && TextUtils.isEmpty(it.toString())) {
                    listUserAdapter.submitList(listUserSuggested)
                } else {
                    viewModelFactory.getViewModel().setQuery(it.toString())
                }
            }
        }, {

        })

        binding.btnCreate.setOnClickListener { v ->
            val userIds: ArrayList<String> = ArrayList()
            for ((key, value) in listSelected) {
                userIds.add(key)
            }
            args.roomId?.let { rooId ->
                binding.room = viewModelFactory.getViewModel().getInviteUsersToRoomResult()
                viewModelFactory.getViewModel().setInviteUsersToRoom(rooId, userIds)

//                viewModelFactory.getViewModel().getInviteUsersToRoomResult().observe(viewLifecycleOwner, Observer
//                { t ->
//                    t?.let {
//                        when (it.status) {
//                            Status.SUCCESS -> {
//                                val intentRoom = Intent(this.context, RoomActivity::class.java)
//                                intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, applcation.getUserId())
//                                intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, rooId)
//                                intentRoom.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                                startActivity(intentRoom)
//                                activity?.finish()
//                            }
//                            else -> {
//                            }
//                        }
//                    }
//                })
            }

        }

        binding.editTextQuery.setOnEditorActionListener { p0, p1, p2 ->
            if (p1 == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
            }
            return@setOnEditorActionListener false

        }
        binding.btnCreate.isEnabled = false

    }

    override fun getFragment(): Fragment {
        return this
    }
}
