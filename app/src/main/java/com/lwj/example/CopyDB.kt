package com.lwj.example


import android.content.Context
import android.os.Environment


import java.io.*


/**
 * @author   LWJ
 * @date on 2018/4/25
 * @describe 添加描述
 */
class CopyDB(private var mContext: Context) {
    private val bufferSize = 1024
    private val dBName = "location.db"
    private val packageName = mContext.packageName

    fun checkDB(cover: Boolean): Int {
        val path = Environment.getDataDirectory().absolutePath
        val dBPath = "/data$path/$packageName/databases/"
        if (!File(dBPath).exists()) {
            File(dBPath).mkdirs()
        }
        return copyDatabase("$dBPath$dBName", cover)
    }


    private fun copyDatabase(dbFile: String, cover: Boolean): Int {
        var fos: FileOutputStream? = null
        var ins: InputStream? = null
        var result = 0
        try {
            if (cover || !File(dbFile).exists()) {
                ins = mContext.resources.openRawResource(R.raw.gdjj_location)
                fos = FileOutputStream(dbFile)
                val buffer = ByteArray(bufferSize)
                var length: Int = ins.read(buffer)
                while (length > 0) {
                    fos.write(buffer, 0, length)
                    length = ins.read(buffer)
                }
                fos.flush()
            }
            result = 1
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fos?.close()
            ins?.close()
        }
        return result
    }
}