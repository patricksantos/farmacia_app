package livrokotlin.com.farmaciaesperanca.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.model.CreditCard
import livrokotlin.com.farmaciaesperanca.view.creditcard.CreditCardActivity
import org.jetbrains.anko.toast

class CreditCardListAdapter(private val screen: CreditCardActivity, private val items: MutableList<CreditCard>) : RecyclerView.Adapter<CreditCardHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardHolder {
        return CreditCardHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.item_credit_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return if (items.isNotEmpty()) items.size else 0
    }

    override fun onBindViewHolder(holder: CreditCardHolder, position: Int)
    {
        val item =  items.get(position)

        holder.tvEnterprise.setText( item.enterprise )
        holder.tvNumber.setText( item.getNumberAsHidden() )
        holder.tvOwnerName.setText( item.getOwnerFullNameAsHidden() )

        holder.btRemove.setOnClickListener {

            val selectedItem = position

            holder.btRemove.text = screen.getString( R.string.remove_item_going )
            holder.btRemove.isEnabled = false
            items.removeAt( selectedItem )
            notifyItemRemoved( selectedItem )

            screen.callbacksToUpdateItem(
                {
                    status ->
                    holder.btRemove.text =
                        if( status )
                            screen.getString( R.string.remove_item_going )
                        else
                            screen.getString( R.string.remove_item )
                },
                {
                    status -> holder.btRemove.isEnabled = !status
                },
                {
                    status ->
                    if( !status ){ //tirar o !
                        items.removeAt( selectedItem )
                        notifyItemRemoved( selectedItem )
                        screen.toast("aquiiiiii")
                    }
                }
            )
            screen.callPasswordDialog()
        }

    }
}