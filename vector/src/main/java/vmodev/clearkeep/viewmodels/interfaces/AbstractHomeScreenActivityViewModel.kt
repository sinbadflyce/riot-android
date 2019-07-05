package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractHomeScreenActivityViewModel : ViewModel() {
    abstract fun getUserById(): LiveData<Resource<User>>;
    abstract fun getListRoomByType(): LiveData<Resource<List<Room>>>;
    abstract fun setValueForUserById(userId: String);
    abstract fun setValueForListRoomType(filters: Array<Int>)
    abstract fun setValueForListRoomTypeFavourite(filters: Array<Int>);
    abstract fun getListRoomTypeFavouriteResult(): LiveData<Resource<List<Room>>>;
}