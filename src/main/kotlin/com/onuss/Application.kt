package org.example.com.onuss

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.example.com.onuss.firebaseNotification.sendFcmNotification

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {

    install(ContentNegotiation) {
        json()
        Gson()
    }




    install(CallLogging)
    routing {
        get("/") {
            call.respondText("Hello KTOR")

        }

        post("/person") {
            //call.respondText("Hello KTOR3")

            try {
                val segmentList = arrayListOf<Segment>()
                val homeComponents = arrayListOf<Component>()
                val mapComponents = arrayListOf<Component>()



                val imageModel = ImageModel("https://buffer.com/cdn-cgi/image/w=1000,fit=contain,q=90,f=auto/library/content/images/size/w600/2023/10/free-images.jpg","Center",40,55)
                val imageModel2 = ImageModel("https://buffer.com/cdn-cgi/image/w=1000,fit=contain,q=90,f=auto/library/content/images/size/w600/2023/10/free-images.jpg","Center",40,55)
                val imageModel3 = ImageModel("https://buffer.com/cdn-cgi/image/w=1000,fit=contain,q=90,f=auto/library/content/images/size/w600/2023/10/free-images.jpg","Center",40,55)
                val textModel = TextModel ("Fotograg Çekimleri","Center",20,1f,"#000000")
                val textModel2 = TextModel ("Fotograg Çekimleri","Center",20,1f,"#000000")


                val component = Component("image",imageModel)
                val component2 = Component("image",imageModel2)
                val component3 = Component("image",imageModel3)
                val component4 = Component("text", text =  textModel)
                val component5 = Component("text",text = textModel2)

                homeComponents.addAll(listOf(component,component4,component3))
                mapComponents.addAll(listOf(component2,component5))




                val homeSegment = Segment(segmentTitle = "ANASAYFA", segmentID = "HOME_SEGMENT",homeComponents)
                val mapSegment = Segment(segmentTitle = "HARİTA", segmentID = "MAP_SEGMENT",mapComponents)

                segmentList.addAll(listOf(homeSegment,mapSegment))


                call.respond(message = segmentList, status = HttpStatusCode.OK)

            } catch (e: Exception) {
                call.respond(message = " ${e.message}", status = HttpStatusCode.BadRequest)


            }

        }

        post("/send"){

            notifaticonSend(call)

        }





        post("/birol") {
            try {
                val segmentList = arrayListOf<Segment>()
                val homeComponents = arrayListOf<Component>()
                val mapComponents = arrayListOf<Component>()



                val imageModel = ImageModel("https://buffer.com/cdn-cgi/image/w=1000,fit=contain,q=90,f=auto/library/content/images/size/w600/2023/10/free-images.jpg","Center",40,55)
                val imageModel2 = ImageModel("https://buffer.com/cdn-cgi/image/w=1000,fit=contain,q=90,f=auto/library/content/images/size/w600/2023/10/free-images.jpg","Center",40,55)
                val imageModel3 = ImageModel("https://buffer.com/cdn-cgi/image/w=1000,fit=contain,q=90,f=auto/library/content/images/size/w600/2023/10/free-images.jpg","Center",40,55)
                val textModel = TextModel ("Fotograf Çekimleri","Center",20,1f,"#000000")
                val textModel2 = TextModel ("Fotograf2222 Çekimleri","Center",20,1f,"#6c25be")
                val textModel55 = TextModel ("Fotograf2222 Çekimleri","Center",20,1f,"#6c25be")

                val edittext = TextModel("xzczxc","",255,1f, color = "#000000")

                val component = Component("image",imageModel)
                val component2 = Component("image",edittext = edittext, image =  imageModel2)
                val component3 = Component("image",imageModel3)
                val component4 = Component("text", text =  textModel)
                val component5 = Component("text",text = textModel2)
                val component6 = Component("edittext", edittext = textModel55, text = textModel)

                //homeComponents.addAll(listOf(component,component4,component3))
                mapComponents.addAll(listOf(component2,component5,component6))




                val homeSegment = Segment(segmentTitle = "ANASAYFA", segmentID = "HOME_SEGMENT",homeComponents)
                val mapSegment = Segment(segmentTitle = "HARİTA", segmentID = "MAP_SEGMENT",mapComponents)

                segmentList.addAll(listOf(homeSegment,mapSegment))

                call.respond(message = segmentList, status = HttpStatusCode.OK)



            } catch (e: Exception) {
                call.respond(message = " ${e.message}", status = HttpStatusCode.BadRequest)


            }


        }
    }


}

suspend fun notifaticonSend(call: ApplicationCall) {
    val fcmToken = "f65L8PmoQkyykGfxSR5o9A:APA91bGq4W8p9aAeoMVCXuOMSpqEQHwUokXBTfSNT4QKLokZiYdZdWU_7TEgOtPCP-bUqajdadN2WQFJc94kifFjOYUJw4o8ltPZgf7lz0r4qM_ifzLN2K9M22f1MNtkLHmnOtNX7X4_"
    val notificationTitle = "Test Notification"
    val notificationBody = "This is a test notification from Ktor."

    print("NOTIFICATION GİTTİ")
    try {
        sendFcmNotification(fcmToken){
        }
        call.respond(message = "GÖNDERİLDİ", status = HttpStatusCode.OK)

        // FCM gönderme işlemi tamamlandıktan sonra yanıtı gönder

    } catch (e: Exception) {
        // Hata durumunu kontrol etmek için gerekli işlemleri yapabilirsiniz.
        call.respond(
            message = "İstek gönderilirken bir hata oluştu: ${e.localizedMessage}",
            status = HttpStatusCode.InternalServerError
        )
    }
}


@Serializable
data class Segment(
    val segmentTitle: String,
    val segmentID: String,
    val segmentComponents: ArrayList<Component>
)


@Serializable
data class Component(
    val type: String,
    val image : ImageModel? = null,
    val text : TextModel? = null,
    val edittext : TextModel? = null
)


@Serializable
data class ImageModel(
    val source: String,
    val alignment: String,
    val width: Int,
    val height: Int

)


@Serializable
data class TextModel(
    val text: String,
    val alignment: String,
    val fontSize: Int,
    val fontWeight: Float,
    val color: String
)


@Serializable
data class EditTextModel(
    val text: String,
    val alignment: String,
    val fontSize: Int,
    val fontWeight: Float,
    val color: String
)



@Serializable
enum class EditTextType {
    TEXT,
    NUMERIC,
    DECIMAL,
    DATE,
    TIME
}

@Serializable
enum class Colors(val hex : String){
    BLACK ("#000")
}

/*

{
    "list": {
    "rowHeight": 44,
    "dividerColor": "#979797",
    "rows": [
    {
        "padding": [7, 14, 7, 14],
        "image": {
        "source": "https://images.example.com/instinct.jpg",
        "alignment": "leading",
        "width": 30,
        "height": 30
    },
        "label": {
        "text": "Rocky Mountain Instinct",
        "alignment": "top",
        "fontSize": 11,
        "fontWeight": "semibold",
        "color": "#000000"
    },
        //...
    },
    //...
    ]
}
}

{
    "name":"Home Screen",
    "elements":[
    {
        "type":"column",
        "children":[
        {
            "type":"text",
            "text":"Welcome to the app!",
            "fontSize":24,
            "color":"#000000"
        },
        {
            "type":"text",
            "text":"Please log in to continue",
            "fontSize":18,
            "color":"#777777"
        },
        {
            "type":"row",
            "children":[
            {
                "type":"button",
                "text":"Log in",
                "backgroundColor":"#4CAF50",
                "color":"#FFFFFF",
                "onPress":"loginFunction"
            },
            {
                "type":"button",
                "text":"Sign up",
                "backgroundColor":"#3F51B5",
                "color":"#FFFFFF",
                "onPress":"signupFunction"
            }
            ]
        }
        ]
    }
    ]
}*/
