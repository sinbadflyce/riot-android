package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * Entity for store Room Data
 * @property id Int
 * @property name String
 * @property type Int
 * @property updatedDate Long
 * @property avatarUrl String
 * @constructor
 */
@Entity(primaryKeys = ["id"])
data class Room(
        @field:SerializedName("id") val id: String,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("topic") val topic: String,
        @field:SerializedName("type") val type: Int,
        @field:SerializedName("updatedDate") val updatedDate: Long,
        @field:SerializedName("avatarUrl") val avatarUrl: String,
        @field:SerializedName("notifyCount") val notifyCount: Int,
        @field:SerializedName("roomMemberId") val roomMemberId: String,
        @field:SerializedName("roomMemberStatus") val roomMemberStatus: Byte?,
        @field:SerializedName("version") val version: Int,
        @field:SerializedName("highlightCount") val highlightCount: Int,
        @field:SerializedName("lastMessage") val lastMessage: String,
        @field:SerializedName("encrypted") val encrypted: Byte
)