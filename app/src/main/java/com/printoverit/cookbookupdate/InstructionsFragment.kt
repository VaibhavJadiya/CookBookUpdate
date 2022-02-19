package com.printoverit.cookbookupdate

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.printoverit.cookbookupdate.R
import com.printoverit.cookbookupdate.models.Result
import kotlinx.android.synthetic.main.fragment_instructions.view.*


class InstructionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_instructions, container, false)
        val args=arguments
        val myBundle = args?.getParcelable<Parcelable>("recipeBundle") as Result?
        view.instructions_webview.webViewClient=object:WebViewClient(){}
        val websitUrl=myBundle!!.sourceUrl
        view.instructions_webview.loadUrl(websitUrl)
        return view
    }

}