package vmodev.clearkeep.repositories

import androidx.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.databases.AbstractRoomUserJoinDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.repositories.wayloads.*
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodelobjects.RoomUserJoin
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomUserJoinRepository @Inject constructor(private val roomUserJoinDao: AbstractRoomUserJoinDao, private val matrixService: MatrixService, private val appExecutors: AppExecutors) {
    fun updateOrCreateRoomUserJoin(roomId: String, userId: String) {
        object : AbstractNetworkBoundSourceRx<RoomUserJoin, RoomUserJoin>() {
            override fun saveCallResult(item: RoomUserJoin) {
                roomUserJoinDao.insert(item);
            }

            override fun shouldFetch(data: RoomUserJoin?): Boolean {
                return data == null;
            }

            override fun loadFromDb(): LiveData<RoomUserJoin> {
                return roomUserJoinDao.getRoomUserJoinWithRoomIdAndUserId(roomId, userId);
            }

            override fun createCall(): Observable<RoomUserJoin> {
                return Observable.create { emitter ->
                    emitter.onNext(RoomUserJoin(roomId = roomId, userId = userId));
                    emitter.onComplete();
                }
            }
        }
    }

    fun updateOrCreateRoomUserJoinRx(roomId: String, userId: String): Observable<RoomUserJoin> {
        return object : AbstractNetworkBoundSourceReturnRx<RoomUserJoin, RoomUserJoin>() {
            override fun shouldFetch(data: RoomUserJoin?): Boolean {
                return data == null;
            }

            override fun saveCallResult(item: RoomUserJoin) {
                roomUserJoinDao.insert(item);
            }

            override fun loadFromDb(): Single<RoomUserJoin> {
                return roomUserJoinDao.getRoomUserJoinWithRoomIdAndUserIdRx(roomId, userId);
            }

            override fun createCall(): Observable<RoomUserJoin> {
                return Observable.create { emitter ->
                    emitter.onNext(RoomUserJoin(roomId = roomId, userId = userId));
                    emitter.onComplete();
                }
            }
        }.getObject();
    }

    fun insertRoomUserJoin(roomId: String, userId: String) {
        Completable.fromAction {
            roomUserJoinDao.insert(RoomUserJoin(roomId, userId))
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    fun getRoomListUser(filters: Array<Int>): LiveData<Resource<List<RoomListUser>>> {
        return object : AbstractLocalBoundSource<List<RoomListUser>>() {
            override fun loadFromDb(): LiveData<List<RoomListUser>> {
                return roomUserJoinDao.getListRoomListUser(filters);
            }
        }.asLiveData();
    }

    fun getRoomListUserSortWithName(filters: Array<Int>) : LiveData<Resource<List<RoomListUser>>>{
        return object : AbstractLocalLoadSouce<List<RoomListUser>>(){
            override fun loadFromDB(): LiveData<List<RoomListUser>> {
                return roomUserJoinDao.getListRoomListUserWithFilterSortName(filters);
            }
        }.asLiveData();
    }

    fun getListRoomListUserWithFilterAndUserId(userId: String, filters: Array<Int>): LiveData<Resource<List<RoomListUser>>> {
        return object : AbstractLocalBoundSource<List<RoomListUser>>() {
            override fun loadFromDb(): LiveData<List<RoomListUser>> {
                return roomUserJoinDao.getListRoomWithFilterAndUserId(userId, filters);
            }
        }.asLiveData();
    }

    fun getListRoomListUserWithListRoomId(filters: List<Int>, query: String): LiveData<Resource<List<RoomListUser>>> {
        return object : AbstractLocalLoadSouce<List<RoomListUser>>() {
            override fun loadFromDB(): LiveData<List<RoomListUser>> {
                return roomUserJoinDao.findRoomByNameContain(filters, "%" + query + "%");
            }
        }.asLiveData();
    }
}