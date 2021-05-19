package com.localization.assignment.helper

import android.content.Context
import android.content.res.Configuration
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.File
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.HashMap





/**
 * Class for translating string to the provided locale. This will act as the main helper class
 * to access all the strings defined in the application's string resource package.
 *
 * When any string resource is accessed, it is first checked in the external language translation
 * file if the resource is available in the provided locale (language). If the translation is
 * available, it is returned. If the translation is not available, it will be checked if it is
 * available in the Android string resources.
 */
class LocaleHelper(ctx: Context) {

    private val context:Context = ctx

    /**
     * Create a hash-map for storing the locale specific contexts in the map to avoid creating
     * context every time. We create the context once and use it in future. This is adds efficiency
     * when we have to support multiple language translation in a single session
     */
    private val localeConfigurationMap = HashMap<String, Context>();

    private val languageFile:File = File(
        context.getExternalFilesDir(null)!!.absolutePath + File.separator
                + DownloadHelper.LANGUAGE_FILE_NAME + "." + DownloadHelper.LANGUAGE_FILE_EXT)


    private val languageDocumentFactory:DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
    private val languageDocumentBuilder:DocumentBuilder = languageDocumentFactory.newDocumentBuilder()
    private lateinit var languageDocument: Document;





    /**
     * Check if the external language file exists.
     *
     * @return true if exists otherwise false
     */
    fun isLanguageFileExists():Boolean{
        return  languageFile.exists()
    }





    /**
     * Load the language's file stream into the memory making sure it is available when accessing
     * the string data
     */
    fun loadLanguageData(){
        languageDocument = languageDocumentBuilder.parse(languageFile)
        languageDocument.documentElement.normalize()
    }





    /**
     * Return the localized string associated with the provided string resource id for the given
     * locale. If the requested string is not found in external language data file or internal
     * locale (language) specific string resource file then the default string will be returned
     *
     * @param stringRes String resource id
     * @param locale Language locale value
     *
     * @return Locale specific string associated with the resource id
     */
    fun getString(stringRes: Int, locale: String):String{
        var string = getLocaleStringFromExternalSource(getResNameFromId(stringRes), locale)

        if(string != null){
            return string
        }

        string = getStringByLocaleFromAsset(stringRes, locale)

        if(string != null){
            return string
        }

        return context.getString(stringRes)
    }





    /**
     * Returns the string name of integer resource id
     *
     * @param stringRes resource id of string
     *
     * @return String value of resource name
     */
    private fun getResNameFromId(stringRes: Int):String{
        return context.resources.getResourceEntryName(stringRes)
    }





    /**
     * Get the locale (language) specific string value from the external language file
     *
     * @param stringRes String resource name
     * @param locale Locale in which string to be returned
     *
     * @return String value in the given locale otherwise null if not found
     */
    private fun getLocaleStringFromExternalSource(stringRes: String, locale: String):String?{
        val nodes:NodeList = languageDocument.getElementsByTagName(stringRes)

        if(nodes != null && nodes.length >0 && nodes.item(0).hasChildNodes()){
            val element:Element = nodes.item(0) as Element
            return getLocaleValue(element, locale)
        }

        return null
    }





    /**
     * Get the string value from the element of the given locale
     *
     * @param element Document element containing translations
     * @param locale Locale for which translation to be fetched
     *
     * @return String value of the given locale, null otherwise
     */
    private fun getLocaleValue(element: Element, locale: String): String?{
        if(element.getElementsByTagName(locale) != null
            && element.getElementsByTagName(locale).length > 0){
            val nodeList:NodeList = element.getElementsByTagName(locale).item(0).childNodes
            return nodeList.item(0).nodeValue
        }
        return null
    }





    /**
     * Get the string value of the resource for the given locale from application's internal
     * resource file
     *
     * @param stringRes String resource id
     * @param locale Locale for which string to be fetched
     *
     * @return String value in the given locale for provided resource id
     */
    private fun getStringByLocaleFromAsset(stringRes: Int, locale: String): String {

        var localeContext:Context?

        if(localeConfigurationMap.containsKey(locale)){
            localeContext = localeConfigurationMap[locale]
        }else{
            val configuration = Configuration(context.resources.configuration)
            configuration.setLocale(Locale(locale))
            localeContext = context.createConfigurationContext(configuration)
            localeConfigurationMap[locale] = localeContext
        }

        return localeContext!!.resources.getString(stringRes)
    }

}