package com.lwj.siteselector.sqlite

import android.content.Context
import android.os.Environment
import android.text.TextUtils

import com.lwj.siteselector.R
import java.io.*

class CopyDB(private var mContext: Context) {
    private val bufferSize = 1024

    private val packageName = mContext.packageName

    fun checkDB(dbResource: Int, dbName: String?, dbPath: String?): Int {

        val resource = if (dbResource == -1) {
            R.raw.gdjj_location
        } else {
            dbResource
        }
        val dBPath = if (TextUtils.isEmpty(dbPath)) {
            val path = Environment.getDataDirectory().absolutePath
            "/data$path/$packageName/databases/"
        } else {
            dbPath
        }
        if (!File(dBPath).exists()) {
            File(dBPath).mkdirs()
        }
        val dBName: String? = if (TextUtils.isEmpty(dbName)) {
            "location.db"
        } else {
            dbName
        }
        return copyDatabase(resource, File(dBPath, dBName))
    }



    private fun copyDatabase(dbResource: Int, dbFile: File): Int {

        var fos: FileOutputStream? = null
        var ins: InputStream? = null
        var result = 0
        try {
            ins = mContext.resources.openRawResource(dbResource)
            fos = FileOutputStream(dbFile)
            val buffer = ByteArray(bufferSize)
            var length: Int = ins.read(buffer)
            while (length > 0) {
                fos.write(buffer, 0, length)
                length = ins.read(buffer)
            }
            fos.flush()
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