package com.lwj.siteselector.sqlite

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import java.io.*

class CopyDB(private var mContext: Context) {
    private val bufferSize = 1024

    private val packageName = mContext.packageName

    fun checkDB(dbResource: Int, dbPath: String?, cover: Boolean = false): Int {


        val dBPath = if (TextUtils.isEmpty(dbPath)) {
            val path = Environment.getDataDirectory().absolutePath
            "/data$path/$packageName/databases/location.db"
        } else {
            dbPath
        }

        if (!File(dBPath).exists()) {
            File(dBPath).mkdirs()
        }

        return copyDatabase(dbResource, dBPath!!, cover)
    }


    private fun copyDatabase(dbResource: Int, dbFile: String, cover: Boolean): Int {
        var fos: FileOutputStream? = null
        var ins: InputStream? = null
        var result = 0
        try {
            if (cover || !File(dbFile).exists()) {
                ins = mContext.resources.openRawResource(dbResource)
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