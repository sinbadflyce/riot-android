package vmodev.clearkeep.rests

import io.reactivex.Observable
import org.matrix.androidsdk.RestClient
import retrofit2.http.*
import vmodev.clearkeep.rests.models.requests.EditMessageRequest
import vmodev.clearkeep.rests.models.responses.EditMessageResponse
import vmodev.clearkeep.rests.models.responses.PassphraseResponse
import vmodev.clearkeep.rests.models.responses.UserProfileResponse
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodelobjects.User

interface ClearKeepApis {
    @Headers("Content-Type: application/json")
    @PUT("/" + RestClient.URI_API_PREFIX_PATH_R0 + "rooms/{roomId}/send/{eventType}/{txId}")
    fun editMessage(@Header("Authorization") token: String
                    , @Path("roomId") roomId: String
                    , @Path("eventType") eventType: String
                    , @Path("txId") eventId: String
                    , @Body data: EditMessageRequest): Observable<EditMessageResponse>

    @GET("/api/user/get-passphrase")
    fun getPassphrase(@Header("Authorization") token: String): Observable<PassphraseResponse>;

    @FormUrlEncoded
    @POST("/api/user/create-passphrase")
    fun createPassphrase(@Header("Authorization") token: String, @Field("passphrase") passphrase: String): Observable<PassphraseResponse>;

    @GET("/" + RestClient.URI_API_PREFIX_PATH_R0 + "profile/{userId}")
    fun getUserProfile(@Path("userId") userId : String) : Observable<UserProfileResponse>

    @GET("/" + RestClient.URI_API_PREFIX_PATH_R0 + "publicRooms")
    fun getRoomPublic(@Path("limit") limit : Int) : Observable<List<RoomListUser>>
}