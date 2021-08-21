package livrokotlin.com.farmaciaesperanca.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.Produto

class ProdutoCestaAdapter(context: Context): ArrayAdapter<Produto>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val v: View

        if(convertView != null){
            v = convertView
        }else{
            v = LayoutInflater.from(context).inflate(R.layout.item_cesta_compras, parent, false)
        }

        val item = getItem(position)

        //val image = v.findViewById<ImageView>(R.id.imagem_produto_cesta)
        val nome = v.findViewById<TextView>(R.id.txt_nomeProduto_cesta)
        val preco = v.findViewById<TextView>(R.id.txt_preçoProduto_cesta)
        val precoFinal = v.findViewById<TextView>(R.id.txt_preçoProdutoFinal_cesta)
        //val btn = v.findViewById<Button>(R.id.btn_remove_item_cesta)

        if(item != null){
            nome.text = item.nome
            preco.text = item.valor.toString()
            //item.btn = btn
            //Glide.with(context).load(item.fotoUrl).into(image)

            val precoF = item.valor!! * item.qtd!!.toInt()
            precoFinal.text = precoF.toString()

            /*btn.setOnClickListener {

                nomeExluirProduto.add(item.nome.toString())
                verExcluirProduto++
                somaTotalProdutosGlobal -= precoFinal.text.toString().toDouble()
                produtosMinhaCesta.remove(item)
                adapterCestaGlobal!!.remove(item)

            }*/

        }
        return v

    }

}