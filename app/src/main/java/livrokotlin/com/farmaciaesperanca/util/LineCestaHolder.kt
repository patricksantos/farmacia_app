package livrokotlin.com.farmaciaesperanca.util

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_cesta_compras.view.*

class LineCestaHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val title = itemView.txt_nomeProduto_cesta as TextView
    val image = itemView.imagem_produto_cesta as ImageView
    val qtd = itemView.edt_quantidade_produto as TextView
    val valorFinal = itemView.txt_preçoProdutoFinal_cesta as TextView
    val valor = itemView.txt_preçoProduto_cesta as TextView

    val btnRemove = itemView.btn_remove_item_cesta as Button
    val btnMais = itemView.btn_mais as ImageButton
    val btnMenos = itemView.btn_menos as ImageButton
}