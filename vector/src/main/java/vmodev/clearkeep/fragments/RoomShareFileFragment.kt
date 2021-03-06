package vmodev.clearkeep.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import im.vector.R
import androidx.lifecycle.Observer
import im.vector.databinding.FragmentRoomShareFileBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.matrix.androidsdk.data.RoomMediaMessage
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IRoomShareFileFragment
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomShareFileFragmentViewModel
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Named

class RoomShareFileFragment : DataBindingDaggerFragment(), IRoomShareFileFragment {

    companion object {
        @JvmStatic
        fun newInstance() =
                RoomShareFileFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }

    private lateinit var binding: FragmentRoomShareFileBinding;
    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractRoomShareFileFragmentViewModel>;

    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.SHARE_FILE)
    lateinit var listRoomAdapter: IListRoomRecyclerViewAdapter;
    private val onClickItem: PublishSubject<String> = PublishSubject.create();
    @Inject
    lateinit var applcation: IApplication;

    private var listDataDirect: List<RoomListUser>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_share_file, container, false, dataBinding.getDataBindingComponent());
        return binding!!.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        setEvent()

    }


    override fun setQuery(query: String) {
        if (!::binding.isInitialized || !::viewModelFactory.isInitialized)
            return;
        if (query.isEmpty()) {
            binding.rooms = viewModelFactory.getViewModel().getListRoomByType();
            viewModelFactory.getViewModel().setListType(arrayOf(1, 129))
        } else {
            binding.rooms = viewModelFactory.getViewModel().getSearchResult();
            viewModelFactory.getViewModel().setQueryForSearch("%" + query + "%");
        }
    }

    override fun onClickItemtRoom(): Observable<String> {
        return onClickItem
    }

    override fun getFragment(): Fragment {
        return this;
    }

    private fun initComponent() {
        binding.searchRoom.setIconifiedByDefault(false);
        binding.searchRoom.isIconified = false;
        binding.lifecycleOwner = viewLifecycleOwner;
        binding.rooms = viewModelFactory.getViewModel().getListRoomByType();
        binding.rvListRoom.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.rvListRoom.adapter = listRoomAdapter.getAdapter();
        viewModelFactory.getViewModel().getListRoomByType().observe(viewLifecycleOwner, Observer {
            listRoomAdapter.getAdapter().submitList(it?.data);
            listDataDirect = it?.data
        });
        viewModelFactory.getViewModel().setListType(arrayOf(2, 130))
    }

    private fun setEvent() {
        listRoomAdapter.setLifeCycleOwner(viewLifecycleOwner, applcation.getUserId());
        listRoomAdapter.setOnItemClick { roomListUser, i ->
            shareFile(applcation.getUserId(), roomListUser.room!!.id)
            activity!!.finish()
        }
        binding.searchRoom.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let { filterDataRoom(it) }
                return false
            }
        })


    }

    override fun onDestroy() {
        super.onDestroy()
        onClickItem.onComplete();
    }

    fun shareFile(userId: String, roomId: String) {
        val roomIntent = Intent(activity, RoomActivity::class.java);
        val cachedFiles = ArrayList(RoomMediaMessage.listRoomMediaMessages(activity!!.intent))
        putCachedFiles(roomIntent, cachedFiles)
        roomIntent.putExtra(RoomActivity.EXTRA_MATRIX_ID, userId);
        roomIntent.putExtra(RoomActivity.EXTRA_ROOM_ID, roomId)
        if (activity!!.isTaskRoot) {
            roomIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        activity!!.startActivity(roomIntent);
    }

    private fun putCachedFiles(intent: Intent, cachedFiles: List<RoomMediaMessage>) {
        if (cachedFiles.isNotEmpty()) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND_MULTIPLE
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, cachedFiles as ArrayList<RoomMediaMessage>)
            shareIntent.setExtrasClassLoader(RoomMediaMessage::class.java.classLoader)
            shareIntent.type = "*/*"
            intent.putExtra(RoomActivity.EXTRA_ROOM_INTENT, shareIntent);
        }
    }


    private fun filterDataRoom(textSearch: String) {
        var newListData = ArrayList<RoomListUser>()
        if (listDataDirect!!.isNotEmpty() && !TextUtils.isEmpty(textSearch)) {
            for (item in listDataDirect!!) {
                item.room?.name?.let {
                    if (it.indexOf(textSearch) > -1) {
                        newListData.add(item)
                    }
                }
            }
        } else {
            newListData = (listDataDirect as ArrayList<RoomListUser>?)!!
        }
        listRoomAdapter.getAdapter().submitList(newListData);

    }
}