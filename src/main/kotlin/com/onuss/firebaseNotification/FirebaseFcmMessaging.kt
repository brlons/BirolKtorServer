package org.example.com.onuss.firebaseNotification


import com.google.gson.annotations.SerializedName
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class FcmRequest(
    val to: String,
    val data: FcmNotification
)

@Serializable
data class FcmNotification(
    @SerializedName("Type"                    ) var Type                   : Int    ,
    @SerializedName("MxRouteId"               ) var MxRouteId              : Int?    = -1,
    @SerializedName("MxBlockId"               ) var MxBlockId              : Int?    = -1,
    @SerializedName("BlockStatusId"           ) var BlockStatusId          : Int?    = -1,
    @SerializedName("BlockExecution_StatusId" ) var BlockExecutionStatusId : Int?    = -1,
    @SerializedName("BlockActionDesc"         ) var BlockActionDesc        : String? = "",
    @SerializedName("PointStatusId"           ) var PointStatusId          : Int?    = -1,
    @SerializedName("PointId"                 ) var PointId                : Int?    = -1,
    @SerializedName("Show"                    ) var Show                   : Int?    = -1,
    @SerializedName("ActionDesc"              ) var ActionDesc             : String? = "",
    @SerializedName("UseSandBox"              ) var UseSandBox             : Int?    = -1,
    @SerializedName("ExecutionType"           ) var ExecutionType          : Int?    = -1
)

suspend fun     sendFcmNotification(fcmToken: String, finish: () -> Unit) {
    val fcmRequest = FcmRequest(
        to = fcmToken,
        data = FcmNotification(
            Type = 100,
            ActionDesc = "DENEMEEMEE"
        )
    )

    val json = Json { ignoreUnknownKeys = true }
    val requestBody = TextContent(
        text = json.encodeToString(FcmRequest.serializer(), fcmRequest),
        contentType = ContentType.Application.Json
    )

    val httpClient = HttpClient (CIO)


    val fcmApiUrl = "https://fcm.googleapis.com/fcm/send"
    val serverKey = "AAAAvsmAzrQ:APA91bFry63C3NYIXGgd6QPPO1fUmuMmMe6l5NVAg0PndTfEF0MxbDLAIZCSer54c5bj6trY0Wk9jPuME-2DYv3uLdZSdCSVaxb0l7JqhqFIqtcpufEaYqZ0jQPTzoVW_XZRUD9As9kP"

    try {
        val response = httpClient.post(fcmApiUrl) {
            method = HttpMethod.Post
            setBody(requestBody)
            headers {
                append("Authorization", "key=$serverKey")
                append("Content-Type", "application/json")
            }

        }
        println("FCM API Response: ${response.headers}")
        println("FCM API Response: ${response.request.url}")
        println("FCM API Response: ${response.request.headers}")
        println("FCM API Response: ${response.request.content}")
    } finally {
        println("FCM API CLOSED: ")
        finish()
        httpClient.close()
    }
}