package scarlet.hit.se.androidca

import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web_chart.*

class WebChartActivity : BaseActivity() {
    lateinit var target: String
    override val layoutId: Int
        get() = R.layout.activity_web_chart

    override fun init() {
        mSwipeLayout.setOnRefreshListener { initData() }
        mSwipeLayout.isRefreshing = true
        initWebView()
    }

    protected fun initWebView() {
        mWebView.settings.javaScriptEnabled = true
        mWebView.requestFocus()
        mWebView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        mWebView.settings.allowUniversalAccessFromFileURLs = true
        mWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                mSwipeLayout.isRefreshing = false
                initData()
            }
        }

        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                result.cancel()
                return true
            }
        }

        if (intent.getIntExtra("StudentId", 0) != 0) {
            mWebView.loadUrl("file:///android_asset/webCharts.html")
            target = "http://139.199.34.237:8080/v1/enrollment/?query=Student.Id%3A${intent.getIntExtra("StudentId", 0)}&fields=Grade%2CCourse"
        } else if (intent.getIntExtra("CourseId", 0) != 0) {
            mWebView.loadUrl("file:///android_asset/courseCharts.html")
            target = "http://139.199.34.237:8080/v1/enrollment/?query=Course.Id%3A${intent.getIntExtra("CourseId", 0)}&fields=Grade"
        } else {
            mWebView.loadUrl("http://139.199.34.237:8080/swagger/")
            target = ""
        }
    }

    private fun initData() {
        mSwipeLayout.isRefreshing = true
        mWebView.loadUrl("javascript:initChart('$target')")
        mSwipeLayout.isRefreshing = false
    }

}
