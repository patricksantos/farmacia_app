package livrokotlin.com.farmaciaesperanca.util

import kotlinx.android.synthetic.main.item_cliente.view.*
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

class ClienteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    val txt_nome = itemView.txt_cliente_nome as TextView
    val txt_cpf = itemView.txt_cliente_cpf as TextView

}