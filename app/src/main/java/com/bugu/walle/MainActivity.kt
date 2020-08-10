package com.bugu.walle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bugu.walle.core.Walle
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Walles.init(this)
        //Walles.show(this)
        //check()
        Walle.init(this@MainActivity.application)
        Walle.show(this@MainActivity)

        MainScope().launch {
            launch {
                repeat(100) {
                    delay(1000)
                    Walle.okHttp("=====> $it ")
                }
            }

            launch {
                repeat(100) {
                    delay(1000)
                    Walle.i("xxxx", "=====> $it ")
                }
            }
            launch {
                repeat(100) {
                    delay(5000)
                    Walle.e("error", "=====> $it ")
                }
            }

            delay(15000)
            //Walle.clear()
            val i = 10 / 0

            delay(30 * 1000)
            //Walle.dismiss()

        }

    }


}