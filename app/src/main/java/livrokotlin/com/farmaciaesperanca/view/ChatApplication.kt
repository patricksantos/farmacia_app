package livrokotlin.com.farmaciaesperanca.view

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import livrokotlin.com.farmaciaesperanca.util.cpfDataBase

class ChatApplication: Application(), Application.ActivityLifecycleCallbacks {

    private fun setOnline( bool: Boolean)
    {
        val uid = FirebaseAuth.getInstance().uid
        if( uid != null )
        {
            FirebaseFirestore.getInstance()
                .collection("Contas")
                .document(cpfDataBase.toString())
                .update("online", bool)
        }
    }

    override fun onActivityPaused(activity: Activity?) {
        this.setOnline(false)
    }

    override fun onActivityResumed(activity: Activity?) {
        this.setOnline(true)
    }

    override fun onActivityStarted(activity: Activity?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityDestroyed(activity: Activity?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityStopped(activity: Activity?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}