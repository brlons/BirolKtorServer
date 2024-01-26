package org.example.com.onuss.firebaseNotification

import kotlinx.serialization.Serializable

@Serializable
data class NotificationDetail(
    val token:String?=null,
    val topic:String?=null,
    val title:String,
    val body:String,
    val deepLink:String?=null,
    val imageUrl:String?=null
)