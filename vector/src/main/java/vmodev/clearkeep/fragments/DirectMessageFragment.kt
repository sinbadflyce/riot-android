package vmodev.clearkeep.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import im.vector.R
import im.vector.databinding.FragmentDirectMessageBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import vmodev.clearkeep.activities.MessageListActivity
import vmodev.clearkeep.activities.RoomSettingsActivity
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IDirectMessageFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IDirectMessageFragment
import javax.inject.Inject
import javax.inject.Named

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DirectMessageFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DirectMessageFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DirectMessageFragment : DataBindingDaggerFragment(), IDirectMessageFragment {
    @Inject
    lateinit var appExecutors: AppExecutors;
    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
    lateinit var listRoomAdapter: IListRoomRecyclerViewAdapter;
    @Inject
    lateinit var viewModelFactory: IDirectMessageFragmentViewModelFactory;

    lateinit var binding: FragmentDirectMessageBinding;

    private val onClickItem: PublishSubject<String> = PublishSubject.create();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_direct_message, container, false, dataBinding.getDataBindingComponent());
        return binding!!.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner;
//        listRoomAdapter.setDataBindingComponent(dataBindingComponent);
        listRoomAdapter.setOnItemClick { room, i ->
            when (i) {
//                3 -> onClickItem.onNext(room.id);
            }
        }
        listRoomAdapter.setOnItemLongClick { room ->
        }
//        binding.rooms = viewModelFactory.getViewModel().getListRoomByType();
        binding.buttonStartDirectChat.setOnClickListener {
            val intentNewChat = Intent(context, FindAndCreateNewConversationFragment::class.java);
            startActivity(intentNewChat);
        }
        binding.recyclerViewListConversation.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewListConversation.adapter = listRoomAdapter.getAdapter();
        viewModelFactory.getViewModel().getListRoomByType().observe(viewLifecycleOwner, Observer {
//            listRoomAdapter!!.getAdapter().submitList(it?.data);
        });
        viewModelFactory.getViewModel().getSearchResult().observe(viewLifecycleOwner, Observer {
//            listRoomAdapter!!.getAdapter().submitList(it?.data);
        })
        viewModelFactory.getViewModel().setListType(arrayOf(1, 129))
    }

    private fun onClickGoRoomPreview(id: String) {
        val intentGoRoom = Intent(activity, MessageListActivity::class.java);
        intentGoRoom.putExtra(MessageListActivity.ROOM_ID, id);
        startActivity(intentGoRoom);
    }

    private fun onClickRoomSettings(id: String) {
        val intent = Intent(this.activity, RoomSettingsActivity::class.java);
        intent.putExtra(RoomSettingsActivity.ROOM_ID, id);
        startActivity(intent);
    }

    private fun onClickAddToFavourite(roomId: String) {
        binding.roomObject = viewModelFactory.getViewModel().getAddToFavouriteResult();
        viewModelFactory.getViewModel().setAddToFavourite(roomId);
    }

    private fun onClickItemDecline(roomId: String) {
        binding.room = viewModelFactory.getViewModel().getLeaveRoom();
        viewModelFactory.getViewModel().setLeaveRoom(roomId);
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()
        onClickItem.onComplete();
    }

    override fun setQuery(query: String) {
        if (!::binding.isInitialized || !::viewModelFactory.isInitialized)
            return;
        if (query.isEmpty()) {
//            binding.rooms = viewModelFactory.getViewModel().getListRoomByType();
            viewModelFactory.getViewModel().setListType(arrayOf(1, 129))
        } else {
            binding.rooms = viewModelFactory.getViewModel().getSearchResult();
            viewModelFactory.getViewModel().setQueryForSearch("%" + query + "%");
        }
    }

    override fun onClickItemtRoom(): Observable<String> {
        return onClickItem;
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

    override fun getFragment(): Fragment {
        return this;
    }
}
