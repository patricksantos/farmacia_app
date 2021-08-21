package livrokotlin.com.farmaciaesperanca.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.nex3z.notificationbadge.NotificationBadge
import kotlinx.android.synthetic.main.activity_categoria_saude.view.*
import kotlinx.android.synthetic.main.item_categoria.view.*

class LineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val title = itemView.txt_nomeProduto as TextView
    val image = itemView.imagem_produto as ImageView
    val qtd = itemView.txt_qtdProduto as TextView
    val valor = itemView.txt_preçoProduto as TextView
    val btn = itemView.comprar_umClick as ImageButton

}