package com.localization.assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.localization.assignment.helper.DownloadHelper
import com.localization.assignment.helper.LocaleHelper

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var localeHelper:LocaleHelper;

    private var tvText:TextView? = null
    private var tvWelcome:TextView? = null
    private var tvNoTranslation:TextView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Initialize locale helper with context
         */
        localeHelper = LocaleHelper(this)

        /**
         * Check if the external translation file exists or not. If exists, load the language data
         * and prepare the user interface.
         *
         * If the language file does not exists, then download it and store it on the SD card. The
         * downloaded file will be stored in the applications secure folder due to recent changes
         * in the security of storage access
         */
        if(localeHelper.isLanguageFileExists()){
            localeHelper.loadLanguageData()
            prepareUI()
        }else{
            DownloadHelper.downloadLanguage(this){_ ->
                localeHelper.loadLanguageData()
                prepareUI()
            }
        }

    }

    /**
     * Setup the user interface
     */
    private fun prepareUI(){

        localeHelper.loadLanguageData()

        val btnEnglish:Button = findViewById(R.id.btn_english)
        val btnHindi:Button = findViewById(R.id.btn_hindi)
        val btnChinese:Button = findViewById(R.id.btn_chinese)
        val btnFrench:Button = findViewById(R.id.btn_french)

        tvText = findViewById(R.id.tv_hello_world)
        tvWelcome = findViewById(R.id.tv_welcome_message)
        tvNoTranslation = findViewById(R.id.tv_no_translation)

        btnEnglish.setOnClickListener(this);
        btnHindi.setOnClickListener(this);
        btnChinese.setOnClickListener(this);
        btnFrench.setOnClickListener(this);
    }

    /**
     * Handle the click/tap of language change buttons
     */
    override fun onClick(v: View?) {

        var selectedLocale = ""

        /**
         * update the selected locale based on the button click/tap
         */
        when(v!!.id){
            R.id.btn_english->{
                selectedLocale = "en"
            }
            R.id.btn_french->{
                selectedLocale = "fr"
            }
            R.id.btn_chinese->{
                selectedLocale = "zh"
            }
            R.id.btn_hindi->{
                selectedLocale = "hi"
            }
        }

        /**
         * Load and set the texts from resource
         */
        tvText!!.text = localeHelper.getString(R.string.hello_world, selectedLocale)
        tvWelcome!!.text = localeHelper.getString(R.string.welcome_message, selectedLocale)
        tvNoTranslation!!.text = localeHelper.getString(R.string.no_translation, selectedLocale)
    }
}