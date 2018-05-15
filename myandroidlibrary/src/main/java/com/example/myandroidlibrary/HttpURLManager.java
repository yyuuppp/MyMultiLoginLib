package com.example.myandroidlibrary;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Puzzle on 2018/4/11.
 */

public class HttpURLManager {

    private static class NameValuePair {
        private String mName;
        private String mValue;

        public NameValuePair(String name, String value) {
            mName = name;
            mValue = value;
        }

        public String getName() {
            return mName;
        }

        public String getValue() {
            return mValue;
        }
    }

    ;

    public static void postParams(OutputStream output, List<NameValuePair> paramsList) throws IOException {
        StringBuilder mStringBuilder = new StringBuilder();
        for (NameValuePair pair : paramsList) {
            if (!TextUtils.isEmpty(mStringBuilder)) {
                mStringBuilder.append("&");
            }

            {
                mStringBuilder.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                mStringBuilder.append("=");
                mStringBuilder.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
        writer.write(mStringBuilder.toString());
        writer.flush();
        writer.close();
    }

    public static HttpURLConnection getHttpURLConnection(String url) {
        HttpURLConnection mHttpURLConnection = null;
        try {
            URL mUrl = new URL(url);
            mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
            //设置链接超时时间
            mHttpURLConnection.setConnectTimeout(15000);
            //设置读取超时时间
            mHttpURLConnection.setReadTimeout(15000);
            //设置请求参数
            mHttpURLConnection.setRequestMethod("POST");
            //添加Header
            mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            //接收输入流
            mHttpURLConnection.setDoInput(true);
            //传递参数时需要开启
            mHttpURLConnection.setDoOutput(true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mHttpURLConnection;
    }

    private static String converStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        String respose = sb.toString();
        return respose;
    }

    private static void setCookie(CookieManager msCookieManager, HttpURLConnection mHttpURLConnection) {
        try {
            if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                mHttpURLConnection.setRequestProperty("Cookie",
                        TextUtils.join(";", msCookieManager.getCookieStore().getCookies()));
            }

            mHttpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //mHttpURLConnection.setRequestProperty("Referer", "http://163.26.71.106/webpac/search.cfm");
            //mHttpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
            //mHttpURLConnection.setRequestProperty("Host", "163.26.71.106");
            //mHttpURLConnection.setRequestProperty("Connection", "keep-alive");
            //mHttpURLConnection.setRequestProperty("Cache-Control", " max-age=0");
            //mHttpURLConnection.setRequestProperty("Accept-Language", "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6");
            //mHttpURLConnection.setRequestProperty("Accept-Encoding","gzip, deflate");
            //mHttpURLConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static CookieManager getCookie(HttpURLConnection mHttpURLConnection, Map<String, List<String>> headerFields) {
        CookieManager msCookieManager = new CookieManager();
        try {
            final String COOKIES_HEADER = "Set-Cookie";

            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

            if (cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msCookieManager;
    }

    public static HashMap<String, String> htmlParser(String html) {
        final HashMap<String, String> meMap = new HashMap<String, String>();
        try {
            Document doc = Jsoup.parse(html, "UTF-8");
            Element content = doc.getElementById("login_form");
            Elements links = content.getElementsByTag("input");

            for (Element link : links) {
                String attrName = link.attr("name");
                String attrValue = link.attr("value");
                meMap.put(attrName, attrValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return meMap;
    }

    private static List<NameValuePair> processForm(HashMap<String, String> meMap) {
        List<NameValuePair> postParams = new ArrayList<>();
        //要传递的参数
        Iterator myVeryOwnIterator = meMap.keySet().iterator();
        String hidcaptcha2 = "";
        while (myVeryOwnIterator.hasNext()) {
            String key = (String) myVeryOwnIterator.next();
            String value = (String) meMap.get(key);
            boolean bAdd = true;
            if (key.compareToIgnoreCase("hidid") == 0) {
                value = "R225274012";
            } else if (key.compareToIgnoreCase("password") == 0) {
                value = "0615";
            } else if (key.compareToIgnoreCase("hidcaptcha2") == 0) {
                //value = "4341";
                //hidcaptcha2 = "4341" ;
                hidcaptcha2 = value ;
            } else if (key.compareToIgnoreCase("hidcaptcha") == 0) {
                //value = "(%C2%AB%C3%91";
            } else if (key.compareToIgnoreCase("token") == 0) {
                //value="3F0BC1CA4A65C0CEC1C5AFF40724F008039EBEA7";
            }  else if (key.compareToIgnoreCase("code") == 0) {
                bAdd = false;
            }
            if (bAdd) {
                postParams.add(new NameValuePair(key, value));
            }
        }
        postParams.add(new NameValuePair("code", hidcaptcha2));
        return postParams;
    }

    private void InterateHashMap() {
        //           String tmp ="" ;
//            for(String key:headerFields.keySet()){
//                tmp =  key + ":" ;
//                List<String> valuesList = headerFields.get(key);
//                for(String value:valuesList){
//                    tmp = tmp+ value + "," ;
//                }
//            }
//            Log.i("demi",tmp.toString() );
    }

    static CookieManager cm = null;
    static List<NameValuePair> postParams = null;

    @SuppressWarnings("deprecation")
    public static void useHttpUrlConnectionPost(String url, boolean bSetCookie, boolean bGet, boolean bPOST,
                                                boolean bConnect, boolean bGetResponse, boolean bGetCookie, boolean bGetForm,
                                                boolean bPostForm, boolean bBackConnect, boolean bGetFormResponse) {
        InputStream mInputStream = null;
        String[] urls = {"http://163.26.71.106/webpac/search.cfm",
                "http://163.26.71.106/webpac/ajax_page/save_in_session.cfm?method=clear&name=selected_mid_in_search",
                "http://163.26.71.106/webpac/ajax_page/check_login_status.cfm",
                "http://163.26.71.106/webpac/ajax_page/get_user_name.cfm",
                "http://163.26.71.106/webpac/login_iframe.cfm?login_checker_kit=true&call=close_fancybox&params="};


        //剛建立連線 此時有cookie

        boolean bSwitch = false;
        try {
            int idx = Integer.parseInt(url.trim());
            HttpURLConnection mHttpURLConnection = HttpURLManager.getHttpURLConnection(urls[idx]);

            //回傳登入的帳號密碼 , 回傳完cookie會變動
            //有cookies
            if (bSetCookie) {
                setCookie(cm, mHttpURLConnection);
            }
            if (bGet) {
                mHttpURLConnection.setRequestMethod("GET");
            }
            if (bPOST) {
                mHttpURLConnection.setRequestMethod("POST");
            }
            if (bConnect) {
                mHttpURLConnection.connect();
            }
            String response = "";
            if (bGetResponse) {
                mInputStream = mHttpURLConnection.getInputStream();
                int code = mHttpURLConnection.getResponseCode();
                response = converStreamToString(mInputStream);
                response = response.trim();
            }
            Map<String, List<String>> headerFields = null;
            if (bGetCookie) {
                headerFields = mHttpURLConnection.getHeaderFields();
                cm = HttpURLManager.getCookie(mHttpURLConnection, headerFields);
            }

            HashMap<String, String> map = null;

            if (bGetForm) {
                map = htmlParser(response);
                postParams = processForm(map);
            }

            if (bPostForm) {
                HttpURLManager.postParams(mHttpURLConnection.getOutputStream(), postParams);
            }
            if (bBackConnect) {
                mHttpURLConnection.connect();
            }

            if (bGetFormResponse) {
                mInputStream = mHttpURLConnection.getInputStream();
                int code = mHttpURLConnection.getResponseCode();
                response = converStreamToString(mInputStream);
                response = response.trim();
            }

            mHttpURLConnection.disconnect();
            mInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
