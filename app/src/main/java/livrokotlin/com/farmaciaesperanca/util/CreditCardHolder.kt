package livrokotlin.com.farmaciaesperanca.util

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_credit_card.view.*

class CreditCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvEnterprise = itemView.tv_enterprise as TextView
    val tvNumber = itemView.tv_number as TextView
    val tvOwnerName = itemView.tv_owner_name as TextView
    val btRemove = itemView.bt_remove as Button

}