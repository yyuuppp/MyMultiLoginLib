package com.example.mylibtestmod;
import com.example.myandroidlibrary.* ;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public void webviewtest() {
        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.loadUrl("http://163.26.71.106/webpac/search.cfm");
    }

    String login_url = "http://163.26.71.106/webpac/login_iframe.cfm?login_checker_kit=true&call=close_fancybox&params=";
    //String login_url = "http://192.168.0.116:8500/CFIDE/administrator/index.cfm" ;
    private void volleybody(final TextView mTextView) {


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mTextView.setText("Response is: " + response.trim());
                        mTextView.setMovementMethod(new ScrollingMovementMethod());
                        HttpURLManager.htmlParser(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });


// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    public void htmlParser_URL(String html) {
        SpannableStringBuilder spannablestringbuilder;
        spannablestringbuilder = new SpannableStringBuilder();
        Spanned spanned = fromHtml(html);
        spannablestringbuilder = new SpannableStringBuilder(spanned);
        URLSpan aurlspan[] = (URLSpan[]) spannablestringbuilder.getSpans(0, spanned.length(), URLSpan.class);
        int j = 0;
        while (j < aurlspan.length) {
            URLSpan urlspan = aurlspan[j];
            //spannablestringbuilder.setSpan(new StateURLSpan(urlspan.getURL()), spannablestringbuilder.getSpanStart(urlspan), spannablestringbuilder.getSpanEnd(urlspan), spannablestringbuilder.getSpanFlags(urlspan));
            spannablestringbuilder.removeSpan(urlspan);
            j++;
        }
    }

    @SuppressWarnings("deprecation")
    public void htmlParser_fromHtml(String html) {
        html = "<html>BIBO <s> LOP </s> IIO </html>" ;
        final TextView mTextView = (TextView) findViewById(R.id.text);
        Spanned sp = Html.fromHtml(html, null, new MyHtmlTagHandler());
        mTextView.setText (sp);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        final TextView mTextView = (TextView) findViewById(R.id.text);
        Button btn = (Button) findViewById(R.id.button);
        final EditText editText = (EditText) findViewById(R.id.editText) ;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // volleybody(mTextView);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //step1. 取cookie
                                HttpURLManager.useHttpUrlConnectionPost("2",false,false,true,true, true,   true,false,false,false,false);
                                //step2.取form ,cookie 又會重複給一次
                                HttpURLManager.useHttpUrlConnectionPost("4",true,true,false,true, true, false,true,false,false,false ) ;
                                //step3.填form
                                HttpURLManager.useHttpUrlConnectionPost("4",true,true, true,false, false,  false,false,true,true,true);

                                HttpURLManager.useHttpUrlConnectionPost("4",true,true, true,false, false,  false,false,true,true,true);
                                //step4. get user name
                                HttpURLManager.useHttpUrlConnectionPost("3",true,false,true,true, true,  false,false,false,false,false);
                                //step1. 取cookie
                                HttpURLManager.useHttpUrlConnectionPost("2",true,false,true,true, true,   true,false,false,false,false);
                    }
                }).start();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
