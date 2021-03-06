package vmodev.clearkeep.databases

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Single
import vmodev.clearkeep.viewmodelobjects.*
import vmodev.clearkeep.viewmodelobjects.Room

@Dao
abstract class AbstractRoomUserJoinDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(roomUserJoin: RoomUserJoin): Long

    @Query("DELETE FROM roomUserJoin WHERE user_id =:userId AND room_id =:roomId ")
    abstract fun deleteRoomUserJoin(userId: String, roomId: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertRoomUserJoins(roomUserJoins: List<RoomUserJoin>): List<Long>

    @Query("SELECT user.* FROM user INNER JOIN RoomUserJoin ON user.id = RoomUserJoin.user_id INNER JOIN room ON room.id = RoomUserJoin.room_id WHERE room.id =:roomId")
    abstract fun getUsersWithRoomId(roomId: String): LiveData<List<User>>

    @Query("SELECT room.* FROM room INNER JOIN RoomUserJoin ON room.id = RoomUserJoin.room_id INNER JOIN user ON user.id = RoomUserJoin.user_id WHERE user.id =:userId")
    abstract fun getRoomsWithUserId(userId: String): LiveData<List<Room>>

    @Update
    abstract fun updateRoomUserJoin(items: List<RoomUserJoin>): Int

    @Query("SELECT room.* FROM room INNER JOIN RoomUserJoin ON room.id = RoomUserJoin.room_id INNER JOIN user ON user.id = RoomUserJoin.user_id WHERE user.id =:userId AND (room.type == 1 OR room.type == 65 OR room.type == 129)")
    abstract fun getDirectChatRoomWithUserId(userId: String): LiveData<List<Room>>

    @Query("SELECT room.* FROM room INNER JOIN RoomUserJoin ON room.id = RoomUserJoin.room_id INNER JOIN user ON user.id = RoomUserJoin.user_id WHERE RoomUserJoin.user_id =:userId AND (room.type == 2 OR room.type == 66 OR room.type == 130)")
    abstract fun getRoomChatRoomWithUserId(userId: String): LiveData<List<Room>>

    @Query("SELECT RoomUserJoin.* FROM RoomUserJoin INNER JOIN room ON room.id = RoomUserJoin.room_id WHERE room.id =:roomId")
    abstract fun getListRoomUserJoinWithRoomId(roomId: String): LiveData<List<RoomUserJoin>>

    @Query("DELETE FROM RoomUserJoin")
    abstract fun delete()

    @Query("SELECT RoomUserJoin.* FROM RoomUserJoin INNER JOIN room ON room.id = RoomUserJoin.room_id INNER JOIN user ON user.id = RoomUserJoin.user_id WHERE room.id =:roomId AND user.id =:userId")
    abstract fun getRoomUserJoinWithRoomIdAndUserId(roomId: String, userId: String): LiveData<RoomUserJoin>

    @Query("SELECT RoomUserJoin.* FROM RoomUserJoin INNER JOIN room ON room.id = RoomUserJoin.room_id INNER JOIN user ON user.id = RoomUserJoin.user_id WHERE room.id =:roomId AND user.id =:userId")
    abstract fun getRoomUserJoinWithRoomIdAndUserIdRx(roomId: String, userId: String): Single<RoomUserJoin>

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.*, Message.*, User.id as user__id, User.name as user__name, User.status as user__status, User.avatarUrl as user__avatarUrl FROM Room LEFT JOIN RoomUserJoin ON RoomUserJoin.room_id = Room.id LEFT JOIN Message ON Room.last_message_id = Message.message_id LEFT JOIN User ON User.id = Message.user_id WHERE room.type =:typeOne ORDER BY room.type DESC, Message.created_at DESC")
    abstract fun getListRoomListUserWithFilter(typeOne: Int): LiveData<List<RoomListUser>>

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.*, Message.*, User.id as user__id, User.name as user__name, User.status as user__status, User.avatarUrl as user__avatarUrl FROM Room LEFT JOIN RoomUserJoin ON RoomUserJoin.room_id = Room.id LEFT JOIN Message ON Room.last_message_id = Message.message_id LEFT JOIN User ON User.id = Message.user_id WHERE room.type =:typeOne OR room.type =:typeTwo ORDER BY room.type DESC, message.created_at DESC")
    abstract fun getListRoomListUserWithFilter(typeOne: Int, typeTwo: Int): LiveData<List<RoomListUser>>

    @Query("SELECT DISTINCT RoomUserJoin.room_id, room.* FROM RoomUserJoin INNER JOIN room ON RoomUserJoin.room_id = room.id WHERE room.type =:typeOne OR room.type =:typeTwo")
    abstract fun getListRoomUserListTwo(typeOne: Int, typeTwo: Int): LiveData<List<RoomUserList>>

    @Query("SELECT DISTINCT User.* FROM User WHERE User.id IN (:ids)")
    abstract fun getListUserInRoom(ids: List<String>): LiveData<List<User>>

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.*, Message.* FROM RoomUserJoin INNER JOIN Room ON RoomUserJoin.room_id = Room.id INNER JOIN User ON RoomUserJoin.user_id = User.id LEFT JOIN Message ON Message.message_id = Room.last_message_id WHERE User.id =:userId AND type =:filterOne")
    abstract fun getListRoomListUserWithFilterAndUserId(userId: String, filterOne: Int): LiveData<List<RoomListUser>>

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.*, Message.* FROM RoomUserJoin INNER JOIN Room ON RoomUserJoin.room_id = Room.id INNER JOIN User ON RoomUserJoin.user_id = User.id LEFT JOIN Message ON Message.message_id = Room.last_message_id WHERE User.id =:userId AND (Room.type =:filterOne OR Room.type =:filterTwo)")
    abstract fun getListRoomListUserWithFilterAndUserId(userId: String, filterOne: Int, filterTwo: Int): LiveData<List<RoomListUser>>

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.*, Message.* FROM RoomUserJoin INNER JOIN Room ON RoomUserJoin.room_id = Room.id INNER JOIN User ON RoomUserJoin.user_id = User.id LEFT JOIN Message ON Message.message_id = Room.last_message_id WHERE User.id =:userId AND (Room.type =:filterOne OR Room.type =:filterTwo OR Room.type =:filterThree)")
    abstract fun getListRoomListUserWithFilterAndUserId(userId: String, filterOne: Int, filterTwo: Int, filterThree: Int): LiveData<List<RoomListUser>>

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.*, Message.* FROM RoomUserJoin INNER JOIN Room ON RoomUserJoin.room_id = Room.id LEFT JOIN Message ON Message.message_id = Room.last_message_id WHERE room.id IN (:roomIds)")
    abstract fun getListRoomListUserWithListRoomId(roomIds: List<String>): LiveData<List<RoomListUser>>

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.*, Message.*,User.id as user__id, User.name as user__name , User.status as user__status, User.avatarUrl as user__avatarUrl FROM Room LEFT JOIN RoomUserJoin ON RoomUserJoin.room_id = Room.id LEFT JOIN Message ON Room.last_message_id = Message.message_id LEFT JOIN User ON User.id = Message.user_id WHERE Room.name LIKE :query AND type IN(:filters)")
    abstract fun findRoomByNameContain(filters: List<Int>, query: String): LiveData<List<RoomListUser>>

//    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.*, Message.*,User.id as user__id, User.name as user__name , User.status as user__status, User.avatarUrl as user__avatarUrl FROM Room LEFT JOIN RoomUserJoin ON RoomUserJoin.room_id = Room.id LEFT JOIN Message ON Room.last_message_id = Message.message_id LEFT JOIN User ON User.id = Message.user_id WHERE Room.name LIKE :query AND Room.encrypted =:encrypted AND type IN(:filters)")
//    abstract fun findRoomDirectoryByNameContain(filters: List<Int>,encrypted : Byte, query: String): LiveData<List<RoomListUser>>

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.*, Message.* FROM Room LEFT JOIN RoomUserJoin ON RoomUserJoin.room_id = Room.id LEFT JOIN Message ON Message.message_id = Room.last_message_id WHERE room.type =:typeOne OR room.type =:typeTwo ORDER BY room.type DESC, Message.created_at DESC")
    abstract fun getListRoomListUserWithFilterTest(typeOne: Int, typeTwo: Int): List<RoomListUser>

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.*, Message.* FROM Room LEFT JOIN RoomUserJoin ON RoomUserJoin.room_id = Room.id LEFT JOIN Message ON Message.message_id = Room.last_message_id WHERE Room.type IN (:filters) ORDER BY Room.name ASC")
    abstract fun getListRoomListUserWithFilterSortName(filters: Array<Int>): LiveData<List<RoomListUser>>

    @Query("SELECT RoomUserJoin.*, Room.*, User.* From Room INNER JOIN RoomUserJoin ON RoomUserJoin.room_id = Room.id INNER JOIN User ON User.id = RoomUserJoin.user_id WHERE Room.type =:type AND User.id !=:userId")
    abstract fun getListUserSuggested(type: Int, userId: String): LiveData<List<User>>

    @Query("SELECT RoomUserJoin.*, Room.*, User.* From Room INNER JOIN RoomUserJoin ON RoomUserJoin.room_id = Room.id INNER JOIN User ON User.id = RoomUserJoin.user_id WHERE Room.type =:typeOne AND User.id !=:userId OR Room.type =:typeTwo AND User.id !=:userId")
    abstract fun getListUserMatrixContact(typeOne: Int, typeTwo: Int, userId: String): LiveData<List<User>>

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.*, Message.*, User.id as user__id, User.name as user__name, User.status as user__status, User.avatarUrl as user__avatarUrl FROM Room LEFT JOIN RoomUserJoin ON RoomUserJoin.room_id = Room.id LEFT JOIN Message ON Room.last_message_id = Message.message_id LEFT JOIN User ON User.id = Message.user_id WHERE room.id = :roomId")
    abstract fun getRoomListUserFindByID(roomId: String): LiveData<RoomListUser>

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.*, Message.* FROM Room LEFT JOIN RoomUserJoin ON RoomUserJoin.room_id = Room.id LEFT JOIN Message ON Message.message_id = Room.last_message_id ")
    abstract fun getListRoomList(): LiveData<List<RoomListUser>>

    fun getListRoomWithUsers(typeOne: Int, typeTwo: Int): LiveData<List<RoomUserList>> {
        val list = getListRoomUserListTwo(typeOne, typeTwo)

        list.value?.forEach {
            val usersIdTmp = mutableListOf<String>()
            it.roomUserJoin?.let {
                it.forEach { usersIdTmp.add(it.userId) }
            }
            it.users = getListUserInRoom(usersIdTmp)
        }
        return list
    }

    fun getListRoomListUser(filters: Array<Int>): LiveData<List<RoomListUser>> {
        when (filters.size) {
            1 -> return getListRoomListUserWithFilter(filters[0])
            2 -> return getListRoomListUserWithFilter(filters[0], filters[1])
            else -> return getListRoomListUserWithFilter(0)
        }
    }

    fun getListRoomWithFilterAndUserId(userId: String, filters: Array<Int>): LiveData<List<RoomListUser>> {
        when (filters.size) {
            1 -> return getListRoomListUserWithFilterAndUserId(userId, filters[0])
            2 -> return getListRoomListUserWithFilterAndUserId(userId, filters[0], filters[1])
            3 -> return getListRoomListUserWithFilterAndUserId(userId, filters[0], filters[1], filters[2])
            else -> return getListRoomListUserWithFilterAndUserId(userId, 0)
        }
    }

    fun getListRoomDirectory(): LiveData<List<RoomListUser>> {
        return getListRoomList()
    }
}