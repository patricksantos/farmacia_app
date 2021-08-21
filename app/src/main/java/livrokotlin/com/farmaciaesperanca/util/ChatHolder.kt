package livrokotlin.com.farmaciaesperanca.util

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val nome = itemView.txt_nomeCliente_chat as TextView
    val lastMsg = itemView.txt_ultimaMsg_chat as TextView
    val btn = itemView.btn_remover_conversa as ImageView

}