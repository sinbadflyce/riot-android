package vmodev.clearkeep.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.databases.AbstractUserDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.repositories.interfaces.IUserRepository
import vmodev.clearkeep.repositories.wayloads.*
import vmodev.clearkeep.rests.ClearKeepApis
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User
import java.io.InputStream
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val abstractUserDao: AbstractUserDao
        , private val matrixService: MatrixService
        , private val executors: AppExecutors) : IUserRepository {

    override fun loadUser(userId: String): LiveData<Resource<User>> {
        return object : AbstractNetworkBoundSourceRx<User, User>() {
            override fun saveCallResult(item: User) {
                abstractUserDao.insert(item)
            }

            override fun shouldFetch(data: User?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<User> {
                return abstractUserDao.findById(userId)
            }

            override fun createCall(): Observable<User> {
                return matrixService.getUser()
            }
        }.asLiveData()
    }

    override fun updateUser(userId: String, name: String, avatarUrl: String) {
        Completable.fromAction {
            abstractUserDao.updateUser(userId, name, avatarUrl)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    override fun findUserFromNetwork(keyword: String): LiveData<Resource<List<User>>> {
        return object : AbstractLoadFromNetworkRx<List<User>>() {
            override fun createCall(): Observable<List<User>> {
                return matrixService.findListUser(keyword)
            }

            override fun saveCallResult(item: List<User>) {
                abstractUserDao.insertUsers(item)
            }
        }.asLiveData()
    }


    override fun getListUserInRoomFromNetworkRx(roomId: String): Observable<List<User>> {
        return object : AbstractLoadFromNetworkReturnRx<List<User>>() {
            override fun createCall(): Observable<List<User>> {
                return matrixService.getUsersInRoom(roomId)
            }

            override fun saveCallResult(item: List<User>) {
                abstractUserDao.insertUsers(item)
            }
        }.getObject()
    }

    override fun updateUser(userId: String, name: String, avatarImage: InputStream?): LiveData<Resource<User>> {
        return object : AbstractNetworkBoundSource<User, User>() {
            override fun saveCallResult(item: User) {
                if (item.avatarUrl.isNullOrEmpty())
                    abstractUserDao.updateUserName(item.id, item.name)
                else
                    abstractUserDao.updateUserNameAndAvatarUrl(item.id, item.name, item.avatarUrl)
            }

            override fun shouldFetch(data: User?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<User> {
                return abstractUserDao.findById(userId)
            }

            override fun createCall(): LiveData<User> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.updateUser(name, avatarImage)
                        .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData()
    }

    override fun updateUserStatus(userId: String, status: Byte) {
        Observable.create<Int> { emitter ->
            val value = abstractUserDao.updateStatus(userId, status)
            emitter.onNext(value)
            emitter.onComplete()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    override fun getListUserInRoomFromNetwork(roomId: String): LiveData<Resource<List<User>>> {
        return object : AbstractLoadFromNetworkRx<List<User>>() {
            override fun createCall(): Observable<List<User>> {
                return matrixService.getUsersInRoom(roomId)
            }

            override fun saveCallResult(item: List<User>) {
                abstractUserDao.insertUsers(item)
            }
        }.asLiveData()
    }

    override fun updateOrCreateNewUserFromRemote(roomId: String): LiveData<Resource<List<User>>> {
        return object : AbstractNetworkCreateAndUpdateSourceRx<List<User>, List<User>>() {
            override fun insertResult(item: List<User>) {
                abstractUserDao.insertUsers(item)
            }

            override fun updateResult(item: List<User>) {
                abstractUserDao.updateUsers(item)
            }

            override fun loadFromDb(): LiveData<List<User>> {
                return abstractUserDao.getUsersWithRoomId(roomId)
            }

            override fun createCall(): Observable<List<User>> {
                return matrixService.getUsersInRoom(roomId)
            }

            override fun getItemInsert(localData: List<User>?, remoteData: List<User>?): List<User> {
                val users = ArrayList<User>()
                remoteData?.let { r ->
                    localData?.let { l ->
                        users.addAll(r)
                    } ?: run {
                        users.addAll(r)
                    }
                } ?: run {
                    localData?.let {
                        users.addAll(it)
                    }
                }
                return users
            }

            override fun getItemUpdate(localData: List<User>?, remoteData: List<User>?): List<User> {
                val users = ArrayList<User>()
                remoteData?.let { r ->
                    localData?.let { l ->
                        users.addAll(r)
                    } ?: run {
                        users.addAll(r)
                    }
                } ?: run {
                    localData?.let {
                        users.addAll(it)
                    }
                }
                return users
            }
        }.asLiveData()
    }

    fun updateOrCreateNewUserFromRemoteRx(roomId: String): Observable<List<User>> {
        return object : AbstractNetworkCreateAndUpdateSourceReturnRx<List<User>, List<User>>() {
            override fun insertResult(item: List<User>) {
                abstractUserDao.insertUsers(item)
            }

            override fun updateResult(item: List<User>) {
                abstractUserDao.updateUsers(item)
            }

            override fun loadFromDb(): Single<List<User>> {
                return abstractUserDao.getUsersWithRoomIdRx(roomId)
            }

            override fun createCall(): Observable<List<User>> {
                return matrixService.getUsersInRoom(roomId)
            }

            override fun getInsertItem(remoteItem: List<User>, localItem: List<User>?): List<User> {
                val users = ArrayList<User>()
                remoteItem.let { r ->
                    localItem?.let { l ->
                        users.addAll(r)
                    } ?: run {
                        users.addAll(r)
                    }
                }
                return users
            }

            override fun getUpdateItem(remoteItem: List<User>, localItem: List<User>?): List<User> {
                val users = ArrayList<User>()
                remoteItem.let { r ->
                    localItem?.let { l ->
                        users.addAll(r)
                    } ?: run {
                        users.addAll(r)
                    }
                }
                return users
            }
        }.getObject()
    }

    override fun getUsersWithId(userIds: Array<String>): LiveData<Resource<List<User>>> {
        return object : AbstractLocalLoadSource<List<User>>(executors) {
            override fun loadFromDB(): LiveData<List<User>> {
                return abstractUserDao.getUsersWithId(userIds)
            }
        }.asLiveData()
    }

    fun getListUserSuggested(): LiveData<Resource<List<User>>> {
        return object : AbstractLocalLoadSource<List<User>>(executors) {
            override fun loadFromDB(): LiveData<List<User>> {
                return abstractUserDao.getListUserSuggested()
            }
        }.asLiveData()
    }

    override fun updateUser(userId: String): Observable<User> {
        return Observable.create { emmiter ->
            var user: User? = abstractUserDao.getUserById(userId)
            if (user == null) {
                matrixService.getUserProfile(userId).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe({
                    abstractUserDao.insert(it)
                    emmiter.onNext(it)
                    emmiter.onComplete()
                }, {
                    emmiter.onError(it)
                    emmiter.onComplete()
                })
            } else {
                emmiter.onNext(user)
                emmiter.onComplete()
            }
        }
    }
}