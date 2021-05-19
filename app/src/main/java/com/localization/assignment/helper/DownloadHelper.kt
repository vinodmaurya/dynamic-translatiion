package com.localization.assignment.helper

import alirezat775.lib.downloader.Downloader
import alirezat775.lib.downloader.core.OnDownloadListener
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File

object DownloadHelper {

    const val LANGUAGE_FILE_NAME = "lang-data"
    const val LANGUAGE_FILE_EXT = "xml"

    /**
     * Download the language file and store it on the SD Card inside the application specific
     * directory
     */
    fun downloadLanguage(context: Context, downloadCallback:(result: Boolean)->Unit){

        val url = "https://raw.githubusercontent.com/vinodmaurya/dynamic-localization-files/main/translation-data.xml"
        val downloader:Downloader = Downloader.Builder(
            context, url).downloadListener(object: OnDownloadListener{
            override fun onCancel() {}
            override fun onCompleted(file: File?) {
                downloadCallback.invoke(true)
            }
            override fun onFailure(reason: String?) {
                downloadCallback.invoke(true)
            }
            override fun onPause() {}
            override fun onProgressUpdate(percent: Int, downloadedSize: Int, totalSize: Int) {}
            override fun onResume() {}
            override fun onStart() {}
        }).fileName(LANGUAGE_FILE_NAME, LANGUAGE_FILE_EXT).build()
        downloader.download()

    }

}