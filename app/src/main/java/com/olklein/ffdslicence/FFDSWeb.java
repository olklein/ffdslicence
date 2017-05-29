package com.olklein.ffdslicence;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by olklein on 26/05/2017.
 */

public class FFDSWeb extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        WebView webView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ffdswebview);

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        // url example :"http://dansesportive.ffdanse.fr/compet-situation.php?couple_num=2145167081"
        webView.loadUrl("http://ffdanse.fr/");
    }


}