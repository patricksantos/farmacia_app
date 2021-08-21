package livrokotlin.com.farmaciaesperanca.util

import android.view.View
import com.nex3z.notificationbadge.NotificationBadge

class badgeClass(val badge: NotificationBadge){

    fun att(){
        if( produtosMinhaCesta.isNotEmpty() ) {
            badge.visibility = View.VISIBLE
            badge.setNumber(produtosMinhaCesta.size)
        }else
            badge.visibility = View.GONE
    }

}