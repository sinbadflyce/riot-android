package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.RoomUserJoinRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomUserJoin
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractListRoomFragmentViewModel
import javax.inject.Inject

class ListRoomFragmentViewModel @Inject constructor(roomRepository: RoomRepository, private val roomUserJoinRepository: RoomUserJoinRepository) : AbstractListRoomFragmentViewModel() {

    private val _directRoomFilters = MutableLiveData<Array<Int>>();
    private val _groupRoomFilters = MutableLiveData<Array<Int>>();
    private val _roomIdForLeave = MutableLiveData<String>();
    private val _roomIdForAddToFavourite = MutableLiveData<String>();
    private val _roomIdForRemoveFromFavourite = MutableLiveData<String>();
    private val _roomIdForJoinRoom = MutableLiveData<String>();
    private val _roomIdForUpdateNotify = MutableLiveData<String>();
    private val _setChangeNotificationState = MutableLiveData<ChangeNotificationStateObject>();

    private val _leaveRoomWithIdResult = Transformations.switchMap(_roomIdForLeave) { input -> roomRepository.leaveRoom(input) }
    private val _addRoomToFavouriteResult = Transformations.switchMap(_roomIdForAddToFavourite) { input -> roomRepository.addToFavourite(input) }
    private val _removeRoomFromFavouriteResult = Transformations.switchMap(_roomIdForRemoveFromFavourite) { input -> roomRepository.removeFromFavourite(input) }
    private val _joinRoomResult = Transformations.switchMap(_roomIdForJoinRoom) { input -> roomRepository.joinRoom(input) }
    private val _getListDirectRoomResult = Transformations.switchMap(_directRoomFilters) { input -> roomRepository.loadListRoomUserJoin(input) }
    private val _getListGroupRoomResult = Transformations.switchMap(_groupRoomFilters) { input -> roomRepository.loadListRoomUserJoin(input) }
    private val _updateRoomNotifyResult = Transformations.switchMap(_roomIdForUpdateNotify) { input -> roomRepository.setRoomNotify(input) }
    private val _changeNotificationStateResult = Transformations.switchMap(_setChangeNotificationState) { input -> roomRepository.changeNotificationState(input.roomId, input.state) }


    override fun setFiltersDirectRoom(filters: Array<Int>) {
        _directRoomFilters.value = filters;
    }

    override fun setFiltersGroupRoom(filters: Array<Int>) {
        _groupRoomFilters.value = filters;
    }

    override fun getListDirectRoomResult(): LiveData<Resource<List<Room>>> {
        return _getListDirectRoomResult;
    }

    override fun getListGroupRoomResult(): LiveData<Resource<List<Room>>> {
        return _getListGroupRoomResult;
    }

    override fun setLeaveRoomId(roomId: String) {
        _roomIdForLeave.value = roomId;
    }

    override fun getLeaveRoomWithIdResult(): LiveData<Resource<String>> {
        return _leaveRoomWithIdResult;
    }

    override fun setAddToFavouriteRoomId(roomId: String) {
        _roomIdForAddToFavourite.value = roomId;
    }

    override fun getAddToFavouriteResult(): LiveData<Resource<Room>> {
        return _addRoomToFavouriteResult;
    }

    override fun setRoomIdForJoinRoom(roomId: String) {
        _roomIdForJoinRoom.value = roomId;
    }

    override fun joinRoomWithIdResult(): LiveData<Resource<Room>> {
        return _joinRoomResult;
    }

    override fun setRoomIdForRemoveFromFavourite(roomId: String) {
        _roomIdForRemoveFromFavourite.value = roomId;
    }

    override fun gerRemoveRoomFromFavouriteResult(): LiveData<Resource<Room>> {
        return _removeRoomFromFavouriteResult;
    }

    override fun setIdForUpdateRoomNotify(roomId: String) {
        _roomIdForUpdateNotify.value = roomId;
    }

    override fun getUpdateRoomNotifyResult(): LiveData<Resource<Room>> {
        return _updateRoomNotifyResult;
    }

    override fun setChangeNotificationState(roomId: String, state: Byte) {
        _setChangeNotificationState.value = ChangeNotificationStateObject(roomId, state);
    }

    override fun getChangeNotificationStateResult(): LiveData<Resource<Room>> {
        return _changeNotificationStateResult;
    }

    override fun getRoomUserJoinResult(roomId: String): LiveData<Resource<List<User>>> {
        return roomUserJoinRepository.getListRoomUserJoinWithRoomId(roomId);
    }
}