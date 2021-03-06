package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractFindAndCreateNewConversationActivityViewModel : ViewModel() {
    abstract fun getUsers(): LiveData<Resource<List<User>>>
    abstract fun setQuery(query: String)
    abstract fun getInviteUserToDirectChat(): LiveData<Resource<Room>>
    abstract fun setInviteUserToDirectChat(userId: String)
    abstract fun joinRoomResult(): LiveData<Resource<Room>>
    abstract fun setJoinRoom(roomId: String)
    abstract fun getListUserSuggested(type: Int, userID: String): LiveData<Resource<List<User>>>
    abstract fun getRoomByID(roomId: String): LiveData<Resource<Room>>
}