package vmodev.clearkeep.fragments

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import im.vector.R
import im.vector.databinding.FragmentDirectMessageBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import vmodev.clearkeep.adapters.DirectMessageRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomRecyclerViewAdapter
import vmodev.clearkeep.binding.FragmentBindingAdapters
import vmodev.clearkeep.binding.FragmentDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.ultis.RoomType
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.RoomViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val MX_SESSION = "MX_SESSION"
private const val ROOMS = "ROOMS"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DirectMessageFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DirectMessageFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DirectMessageFragment : DaggerFragment() {
    private var listener: OnFragmentInteractionListener? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;
    @Inject
    lateinit var appExecutors: AppExecutors;

    var dataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent(this);

    var binding: FragmentDirectMessageBinding? = null;

    var roomViewModel: AbstractRoomViewModel? = null;
    var listRoomAdapter: ListRoomRecyclerViewAdapter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_direct_message, container, false, dataBindingComponent);
        return binding!!.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractRoomViewModel::class.java);
        binding!!.lifecycleOwner = viewLifecycleOwner;
        listRoomAdapter = ListRoomRecyclerViewAdapter(appExecutors = appExecutors, dataBindingComponent = dataBindingComponent, diffCallback = object : DiffUtil.ItemCallback<vmodev.clearkeep.viewmodelobjects.Room>() {
            override fun areItemsTheSame(p0: vmodev.clearkeep.viewmodelobjects.Room, p1: vmodev.clearkeep.viewmodelobjects.Room): Boolean {
                return p0.id == p1.id;
            }

            override fun areContentsTheSame(p0: vmodev.clearkeep.viewmodelobjects.Room, p1: vmodev.clearkeep.viewmodelobjects.Room): Boolean {
                return p0.name == p1.name && p0.updatedDate == p1.updatedDate && p0.avatarUrl == p1.avatarUrl
                    && p0.notifyCount == p1.notifyCount && p0.roomMemberStatus == p1.roomMemberStatus;
            }
        }) { room, i ->
            kotlin.run {
                when (i) {
                    3 -> onClickGoRoom(room.id);
                    0 -> onClickItemPreview(room.id);
                    1 -> onClickJoinRoom(room.id);
                    2 -> onClickItemDecline(room.id);
                }
            }
        };
        binding!!.rooms = roomViewModel!!.getRoomsData();
        binding!!.recyclerViewListConversation.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding!!.recyclerViewListConversation.adapter = listRoomAdapter;
        roomViewModel!!.getRoomsData().observe(viewLifecycleOwner, Observer { t ->
            kotlin.run {
                listRoomAdapter!!.submitList(t?.data);
            }
        });
        roomViewModel!!.setFilter(arrayOf(1, 65))
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onGetMxSession(): MXSession? {
        return listener?.onGetMXSession();
    }

    fun onGetListDirectMessage(): List<Room> {
        return listener?.onGetListDirectMessage()!!;
    }

    fun onGetListDirectMessageInvitation(): List<Room> {
        return listener?.onGetListDirectMessageInvitation()!!;
    }

    fun onClickItem(room: Room) {
        listener?.onClickItem(room);
    }

    fun handleDataChange(): PublishSubject<Status> {
        return listener?.handleDataChange()!!;
    }

    fun onClickJoinRoom(roomId: String) {
        listener?.onClickItemJoin(roomId);
    }

    fun onClickItemDecline(roomId: String) {
        listener?.onClickItemDecline(roomId);
    }

    fun onClickItemPreview(roomId: String) {
        listener?.onClickItemPreview(roomId);
    }

    fun onClickGoRoom(roomId: String) {
        listener?.onClickGoRoom(roomId);
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onGetMXSession(): MXSession

        fun onGetListDirectMessage(): List<Room>;
        fun onGetListDirectMessageInvitation(): List<Room>;
        fun onClickItem(room: Room);
        fun handleDataChange(): PublishSubject<Status>;
        fun onClickItemJoin(roomId: String);
        fun onClickItemDecline(roomId: String);
        fun onClickItemPreview(roomId: String);
        fun onClickGoRoom(roomId: String);
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DirectMessageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            DirectMessageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
