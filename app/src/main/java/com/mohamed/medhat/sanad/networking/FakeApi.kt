package com.mohamed.medhat.sanad.networking

import android.util.Log
import com.mohamed.medhat.sanad.model.*
import okhttp3.MultipartBody
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
        TODO("Not yet implemented")
    }

    override suspend fun confirmEmail(confirmationCode: String): Response<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun getMentorProfile(): Response<MentorProfile> {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
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
                Log.d("Location", "a ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}")
            }
            bInitial -> {
                bInitial = modifiedLocation
                Log.d("Location", "b ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}")
            }
            cInitial -> {
                cInitial = modifiedLocation
                Log.d("Location", "c ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}")
            }
            dInitial -> {
                dInitial = modifiedLocation
                Log.d("Location", "d ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}")
            }
            eInitial -> {
                eInitial = modifiedLocation
                Log.d("Location", "e ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}")
            }
            fInitial -> {
                fInitial = modifiedLocation
                Log.d("Location", "f ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}")
            }
            gInitial -> {
                gInitial = modifiedLocation
                Log.d("Location", "g ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}")
            }
            hInitial -> {
                hInitial = modifiedLocation
                Log.d("Location", "h ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}")
            }
            iInitial -> {
                iInitial = modifiedLocation
                Log.d("Location", "i ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}")
            }
            jInitial -> {
                jInitial = modifiedLocation
                Log.d("Location", "j ${modifiedLocation.latitude}  -  ${modifiedLocation.longitude}")
            }
        }
        return Response.success(modifiedLocation)
    }
}