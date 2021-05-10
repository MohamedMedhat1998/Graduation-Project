package com.mohamed.medhat.sanad.networking

import android.util.Log
import com.google.gson.Gson
import com.mohamed.medhat.sanad.model.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.util.*
import javax.inject.Inject

/**
 * This fake api is supposed to override the functions of the [WebApi] for testing purposes.
 * It should be used when some function of the [WebApi] is not operable for some reason.
 */
class FakeApi @Inject constructor() : WebApi {

    val a = "213d6626-378f-4233-bdd4-4d6246a93d1b"
    val b = "24f9a97b-6180-43a9-a5ee-06cb0320b115"
    val c = "26ee53af-38b5-487c-a66e-ba7610029fd8"
    val d = "72e8f5a8-9361-4f94-8e0e-58ab751cb7bf"
    val e = "768a1487-a4ed-4615-abef-f982a2ae6b9c"
    val f = "7db5a7c6-2b56-412a-96b3-f321006b2eee"
    val g = "839687b3-76c4-4e9b-a60b-e935d8c36431"
    val h = "8667102f-4a93-420b-81b8-eff2f2ffa20e"
    val i = "b20345fc-ec4b-4b5a-822f-f4809ec9106c"
    val j = "e06c87e7-7495-401e-bdda-d98fac92159f"

    private var aInitial = GpsNode(31.042722568106434f, 31.35745120887591f)
    private var bInitial = GpsNode(31.042722568106434f, 31.35745120887591f)
    private var cInitial = GpsNode(31.042722568106434f, 31.35745120887591f)
    private var dInitial = GpsNode(31.042722568106434f, 31.35745120887591f)
    private var eInitial = GpsNode(31.042722568106434f, 31.35745120887591f)
    private var fInitial = GpsNode(31.042722568106434f, 31.35745120887591f)
    private var gInitial = GpsNode(31.042722568106434f, 31.35745120887591f)
    private var hInitial = GpsNode(31.042722568106434f, 31.35745120887591f)
    private var iInitial = GpsNode(31.042722568106434f, 31.35745120887591f)
    private var jInitial = GpsNode(31.042722568106434f, 31.35745120887591f)

    override suspend fun register(newUser: NewUser): Response<Token> {
        TODO("Not yet implemented")
    }

    override suspend fun login(loginUser: LoginUser): Response<Token> {
        val token = Gson().fromJson(
            "{\n" +
                    "  \"token\": \"dfgwqesfasadas\",\n" +
                    "  \"expiration\": \"2025-05-08T21:09:41.943Z\",\n" +
                    "  \"refreshToken\": \"asdfwfaddsawawefasdd\",\n" +
                    "  \"refreshTokenExpiration\": \"2025-05-08T21:09:41.943Z\"\n" +
                    "}", Token::class.java
        )
        return Response.success(token)
    }

    override suspend fun confirmEmail(confirmationCode: String): Response<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun getMentorProfile(): Response<MentorProfile> {
        val profile = Gson().fromJson(
            "{\n" +
                    "  \"firstName\": \"Mohamed\",\n" +
                    "  \"lastName\": \"Medhat\",\n" +
                    "  \"profilePicture\": \"https://i2.wp.com/batman-news.com/wp-content/uploads/2021/02/Superman-Shield-Featured.jpg\",\n" +
                    "  \"gender\": 1,\n" +
                    "  \"age\": 23,\n" +
                    "  \"dateCreated\": \"2021-05-08T21:16:04.820Z\",\n" +
                    "  \"dateModified\": \"2021-05-08T21:16:04.820Z\",\n" +
                    "  \"email\": \"abomed7at55@gmail.com\",\n" +
                    "  \"emailConfirmed\": true,\n" +
                    "  \"phoneNumber\": \"01063863298\",\n" +
                    "  \"phoneNumberConfirmed\": true,\n" +
                    "  \"twoFactorEnabled\": true\n" +
                    "}", MentorProfile::class.java
        )
        return Response.success(profile)
    }

    override suspend fun addBlind(
        firstName: MultipartBody.Part,
        lastName: MultipartBody.Part,
        gender: MultipartBody.Part,
        age: MultipartBody.Part,
        phoneNumber: MultipartBody.Part,
        serialNumber: MultipartBody.Part,
        emergencyPhoneNumber: MultipartBody.Part,
        bloodType: MultipartBody.Part,
        illnesses: List<String>,
        profilePicture: MultipartBody.Part
    ): Response<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun getBlinds(): Response<List<BlindMiniProfile>> {
        val blinds = mutableListOf<BlindMiniProfile>()
        val blindA = Gson().fromJson(
            "  {\n" +
                    "    \"firstName\": \"Ahmed\",\n" +
                    "    \"lastName\": \"Yousef\",\n" +
                    "    \"profilePicture\": \"https://scontent.fcai1-2.fna.fbcdn.net/v/t1.18169-9/20258460_347482965681417_6805177405156473465_n.jpg?_nc_cat=103&ccb=1-3&_nc_sid=09cbfe&_nc_eui2=AeFpYNoSZdljbzm6wK21vMxteTg6SZhjFYh5ODpJmGMViDDwnqQYO1CQsXkgXrelR9Ze9dKOk5VYh1rF65niyqpf&_nc_ohc=pz_r3sWDJUMAX_Lad7Z&_nc_ht=scontent.fcai1-2.fna&oh=c1c145efb4761ee2fc2dadfc9a522916&oe=60BB9CC8\",\n" +
                    "    \"userName\": \"$a\"\n" +
                    "  }\n", BlindMiniProfile::class.java
        )
        val blindB = Gson().fromJson(
            "  {\n" +
                    "    \"firstName\": \"Sherif\",\n" +
                    "    \"lastName\": \"Eldeeb\",\n" +
                    "    \"profilePicture\": \"https://scontent.fcai1-2.fna.fbcdn.net/v/t1.6435-9/102437671_2606665179551391_742514619700266850_n.jpg?_nc_cat=110&ccb=1-3&_nc_sid=09cbfe&_nc_eui2=AeF2GBC8Oh6i7P0h1raUFjV7-oQx8XhGKAv6hDHxeEYoC5mWMbIhOgFhZa4396Juu4a8jCt56EcqmYcxbDQIr30B&_nc_ohc=iNTzFeBsqHYAX901sBo&_nc_ht=scontent.fcai1-2.fna&oh=14d7740ad3b2f7c36c6fd5c1fafef2eb&oe=60BAC57E\",\n" +
                    "    \"userName\": \"$b\"\n" +
                    "  }\n", BlindMiniProfile::class.java
        )
        blinds.apply {
            add(blindA)
            add(blindB)
        }
        return Response.success(blinds)
    }

    override suspend fun getLastNode(serialNumber: String): Response<GpsNode> {
        val min = 0.000010f
        val max = 0.000100f
        val deltaLat = min + Random().nextFloat() * (max - min)
        val deltaLong = min + Random().nextFloat() * (max - min)
        val latSign = if (Random().nextInt() % 2 == 0) -1 else +1
        val longSign = if (Random().nextInt() % 2 == 0) -1 else +1
        val tempInitial = when (serialNumber) {
            a -> aInitial
            b -> bInitial
            c -> cInitial
            d -> dInitial
            e -> eInitial
            f -> fInitial
            g -> gInitial
            h -> hInitial
            i -> iInitial
            j -> jInitial
            else -> aInitial
        }
        val modifiedLocation =
            GpsNode(
                tempInitial.latitude + deltaLat * latSign,
                tempInitial.longitude + deltaLong * longSign
            )
        when (tempInitial) {
            aInitial -> {
                aInitial = modifiedLocation
                Log.d(
                    "Location",
                    "a ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}"
                )
            }
            bInitial -> {
                bInitial = modifiedLocation
                Log.d(
                    "Location",
                    "b ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}"
                )
            }
            cInitial -> {
                cInitial = modifiedLocation
                Log.d(
                    "Location",
                    "c ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}"
                )
            }
            dInitial -> {
                dInitial = modifiedLocation
                Log.d(
                    "Location",
                    "d ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}"
                )
            }
            eInitial -> {
                eInitial = modifiedLocation
                Log.d(
                    "Location",
                    "e ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}"
                )
            }
            fInitial -> {
                fInitial = modifiedLocation
                Log.d(
                    "Location",
                    "f ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}"
                )
            }
            gInitial -> {
                gInitial = modifiedLocation
                Log.d(
                    "Location",
                    "g ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}"
                )
            }
            hInitial -> {
                hInitial = modifiedLocation
                Log.d(
                    "Location",
                    "h ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}"
                )
            }
            iInitial -> {
                iInitial = modifiedLocation
                Log.d(
                    "Location",
                    "i ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}"
                )
            }
            jInitial -> {
                jInitial = modifiedLocation
                Log.d(
                    "Location",
                    "j ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}"
                )
            }
        }
        val rand = Random().nextInt()
        Log.d("Fake API", "rand location response: $rand")
        when {
            rand % 5 == 0 -> {
                return Response.error(
                    422,
                    "Tracking Error".toResponseBody("text/plain".toMediaType())
                )
            }
            rand % 2 == 0 -> {
                return Response.error(
                    404,
                    "Device Error".toResponseBody("text/plain".toMediaType())
                )
            }
        }
        return Response.success(modifiedLocation)
    }
}