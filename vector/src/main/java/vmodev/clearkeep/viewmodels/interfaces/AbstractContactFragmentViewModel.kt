package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractContactFragmentViewModel : ViewModel() {
    abstract fun getListRoomByType(): LiveData<Resource<List<Room>>>;
    abstract fun setListType(types: Array<Int>);
    abstract fun setIdForUpdateRoomNotify(roomId: String);
    abstract fun getUpdateRoomNotifyResult(): LiveData<Resource<Room>>;
    abstract fun getRoomUserJoinResult(roomId: String): LiveData<Resource<List<User>>>;
}