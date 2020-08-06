package com.bugu.walle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bugu.walle.core.Walle
import com.bugu.walle.core.WalleOverlayWindow
import com.bugu.walle.log.Message
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Walle.init(this)
        Walle.show(this)
        //check()

        MainScope().launch {
            launch {
                repeat(100) {
                    delay(1000)
                    Walle.i(
                        TAG,
                        "test --------------------------adadada----------------------asasasa---$it"
                    )

                }
            }

            delay(5000)
            Walle.clear()

            delay(30 * 1000)
            //Walle.dismiss()
        }

    }
}