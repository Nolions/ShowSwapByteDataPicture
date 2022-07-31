package tw.nolions.showswapbytedatapicture

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.URL
import java.net.URLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        // 原圖
//        val urlStr =
//            "https://assets.doudoud1.com/doudou/video/cover/336b3c71329a40628f14b896dfbbc48d.png"

        // 編碼過的
        val urlStr =
            "https://assets.doudoud1.com/doudou/video/cover/7f61b176f63f4d5692487a46773028be.jpg"
        showImage()
    }

    private fun showImage(urlPath: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL(urlPath)
            val byteArrayOutputStream = getOutputStream(url)

            withContext(Dispatchers.Main) {
                try {
                    val byteArray = byteArrayOutputStream!!.toByteArray()
                    Glide.with(context)
                        .asBitmap()
                        .load(byteArrayShift(byteArray))
                        .error(
                            Glide.with(context)
                                .asBitmap()
                                .load(urlStr)
                        )
                        .into(image)
                } catch (e: Exception) {
                    Log.d("aa", "aaa")
                }
            }
        }
    }

    private fun getOutputStream(url: URL): ByteArrayOutputStream? {
        val conn: URLConnection = url.openConnection()
        conn.connectTimeout = 60000
        return try {
            val inputStream = conn.getInputStream()
            val outputStream = ByteArrayOutputStream()
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            outputStream
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 圖片ByteArray編碼
     * ============================================
     * ByteArray 奇偶內容對調
     */
    private fun byteArrayShift(byteArray: ByteArray): ByteArray {
        var evenIndex = 0
        var oddIndex = 1
        val tempArrayList = byteArray.toCollection(ArrayList())

        byteArray.forEachIndexed { index, _ ->
            if (index % 2 == 0) {
                if (oddIndex < byteArray.size) {
                    tempArrayList[index] = byteArray[oddIndex]
                    oddIndex += 2
                }
            } else {
                if (evenIndex < byteArray.size) {
                    tempArrayList[index] = byteArray[evenIndex]
                    evenIndex += 2
                }
            }
        }

        return tempArrayList.toByteArray()
    }

}