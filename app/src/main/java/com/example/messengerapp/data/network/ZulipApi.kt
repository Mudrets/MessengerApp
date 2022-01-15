package com.example.messengerapp.data.network

import com.example.messengerapp.data.network.dto.*
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface ZulipApi {

    // Streams
    @GET("streams")
    fun getAllStreams(): Single<StreamResponse>

    @GET("users/me/subscriptions")
    fun getSubscribedStreams(): Single<SubscriptionsResponse>

    @GET("users/me/{stream_id}/topics")
    fun getTopicsByStreamId(@Path("stream_id") streamId: Long): Single<TopicResponse>

    @FormUrlEncoded
    @POST("users/me/subscriptions")
    fun subscribeToStream(
        @Field("subscriptions") subscriptions: String,
    ): Single<SubscribeChannelResponse>

    // Messages
    @GET("messages")
    fun getMessages(
        @Query("anchor") anchor: Long,
        @Query("num_before") numBefore: Long,
        @Query("num_after") numAfter: Long,
        @Query("narrow") narrow: String,
        @Query("apply_markdown") markdown: Boolean = false,
    ): Single<MessagesResponse>

    @FormUrlEncoded
    @POST("messages")
    fun sendMessage(
        @Field("type") type: String = "stream",
        @Field("content") content: String,
        @Field("to") streamId: Long,
        @Field("topic") topic: String,
    ): Single<ResultResponse>

    @DELETE("messages/{message_id}")
    fun deleteMessage(
        @Path("message_id") messageId: Long
    ): Single<ResultResponse>

    @PATCH("messages/{message_id}")
    fun editMessage(
        @Path("message_id") messageId: Long,
        @Query("content") content: String
    ): Single<ResultResponse>

    @PATCH("messages/{message_id}")
    fun changeTopic(
        @Path("message_id") messageId: Long,
        @Query("topic") topic: String
    ): Single<ResultResponse>

    //Reactions
    @FormUrlEncoded
    @POST("messages/{message_id}/reactions")
    fun addReactionToMessage(
        @Path("message_id") messageId: Long,
        @Field("emoji_name") emojiName: String,
    ): Single<ResultResponse>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "messages/{message_id}/reactions", hasBody = true)
    fun removeReactionFromMessage(
        @Path("message_id") messageId: Long,
        @Field("emoji_name") emojiName: String,
    ): Single<ResultResponse>

    //Emoji
    @GET("https://tinkoff-android-fall21.zulipchat.com/static/generated/emoji/emoji_codes.json")
    fun getAllEmoji(): Single<NetworkEmojiData>

    // Users
    @GET("users")
    fun getUsers(): Single<UsersResponse>

    @GET("users/me")
    fun getAuthorizedUser(): Single<NetworkUser>

    @GET("users/{user_id}/presence")
    fun getUserPresence(@Path("user_id") userId: Long): Single<UserPresenceResponse>

}