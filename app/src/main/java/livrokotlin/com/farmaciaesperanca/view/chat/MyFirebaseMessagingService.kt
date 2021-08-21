package livrokotlin.com.farmaciaesperanca.view.chat

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.User

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        sendNotification(p0)
    }

    private fun sendNotification(remoteMessage: RemoteMessage?){

        val data = remoteMessage!!.data

        if( data["sender"] == null ) return

        val ii = Intent( this, ChatActivity::class.java )

        FirebaseFirestore.getInstance().collection("/Contas")
            .document(data.get("sender")!!)
            .get()
            .addOnSuccessListener {
                val sender = it.toObject(User::class.java)
                /*
                ii.putExtra("nome", sender!!.nome)
                ii.putExtra("token", sender.token)
                ii.putExtra("telefone", sender.telefone)
                ii.putExtra("rg", sender.rg)
                ii.putExtra("email", sender.email)
                ii.putExtra("cpf", sender.cpf)
                ii.putExtra("senha", sender.password)
                ii.putExtra("endereco", sender.endereco)
                ii.putExtra("validador", "sim")
                */

                val pendingIntent = PendingIntent.getActivity(applicationContext, 0, ii, 0)

                val notificantionManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val NOTIFICATION_CHANNEL_ID = "Farmacia Esperança"

                if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O )
                {
                    val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Menssagem Chat", NotificationManager.IMPORTANCE_DEFAULT )

                    notificationChannel.description = "Messagem Descrição"
                    notificationChannel.lightColor = R.color.colorPrimary
                    notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 500)
                    notificationChannel.enableVibration(true)

                    notificantionManager.createNotificationChannel(notificationChannel)

                }

                val notificationBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

                notificationBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.icone)
                    .setContentTitle(data.get("title"))
                    .setContentText(data.get("body"))
                    .setContentIntent(pendingIntent)

                notificantionManager.notify(1, notificationBuilder.build())

            }

    }

}