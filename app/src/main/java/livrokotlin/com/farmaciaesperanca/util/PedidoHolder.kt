package livrokotlin.com.farmaciaesperanca.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_pedido_descricao.view.*
import kotlinx.android.synthetic.main.item_categoria.view.*
import kotlinx.android.synthetic.main.item_pedido.view.*

class PedidoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val title = itemView.txt_cliente_pedido as TextView
    val codigo = itemView.txt_numero_pedido as TextView

    val image = itemView.imagem_pedido as ImageView
    val imageChecked = itemView.imagem_pedido_checked as ImageView

}