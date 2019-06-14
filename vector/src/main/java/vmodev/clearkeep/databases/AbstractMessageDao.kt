package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

@Dao
abstract class AbstractMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(message: Message)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertListMessage(messages: List<Message>)

    @Query("SELECT * FROM message INNER JOIN room ON room.id = message.room_id WHERE message.room_id =:roomId")
    abstract fun getListMessageWithRoomId(roomId: String): LiveData<List<Message>>

    @Query("SELECT * FROM message WHERE id =:id")
    abstract fun findById(id: String): LiveData<Message>;

    @Query("SELECT * FROM message")
    abstract fun getAllMessage(): LiveData<List<Message>>;

    @Query("SELECT * FROM user INNER JOIN message ON user.id = message.user_id WHERE message.room_id =:roomId")
    abstract fun getUsersInRoom(roomId: String): LiveData<List<User>>;

    @Query("SELECT * FROM user INNER JOIN message ON user.id = message.id WHERE message.id =:messageId")
    abstract fun getUserByMessageId(messageId: String): LiveData<User>;

    @Query("SELECT * FROM room INNER JOIN message ON room.id = message.room_id WHERE message.room_id =:roomId")
    abstract fun getRoomByRoomId(roomId: String): LiveData<Room>;
}