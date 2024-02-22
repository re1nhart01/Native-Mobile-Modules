package com.example.arvr

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.arvr.ui.theme.ARVRTheme
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


@Composable
fun RenderSome(content: @Composable () -> Unit) {
    content()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppLayout()
        }
    }
}



class Functions {
    companion object {
        @Composable
        fun GetWidth(): Map<String, Dp> {
            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp.dp
            val screenWidth = configuration.screenWidthDp.dp
            return mapOf("w" to screenWidth, "h" to screenHeight)
        }
    }
    fun onCounterButtonPress(state: MutableState<Int>) {
        state.value++;
    }

    fun performHTTP(value: String, state: MutableState<String>) {
        val request: Request = Request.Builder()
            .url(value)
            .build()

        OkHttpClient().newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    e.printStackTrace()
                }

                @Throws(IOException::class)
                override fun onResponse(call: okhttp3.Call, response: Response) {
                    val res: String? = response.body?.string()
                    state.value = res ?: "EMPTY VALUE"
                }
            })
    }

    fun performHTTP2(value: String, state: MutableState<String>) {
        val url = URL(value)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = connection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuilder()

            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                response.append(line)
            }

            bufferedReader.close()
            inputStream.close()
            state.value = response.toString()
        } else {
            throw Exception("HTTP request failed with code: $responseCode")
        }
    }

}

@Composable
fun AppLayout() {
    var counter = remember { mutableStateOf(0) }
    val functions = Functions()
    ARVRTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Greeting("Android", modifier = Modifier.padding(Dp(10F), Dp(20F), Dp(30F), Dp(40F)))
            Text(text = "${counter.value}", modifier = Modifier.padding(Dp(120f)))
            RenderSome {
                Text(text = "Hello From Some")
            }
            Box(modifier = Modifier.padding(Dp(40f))) {
                Button(onClick = { functions.onCounterButtonPress(state = counter) }) {
                    Text("Bruh")
                }
            }
                Box(modifier = Modifier.padding(Dp(0f), Dp(220f), Dp(0f), Dp(0f))) {
                    PerformedHTTPRequest()
                }
                Box(modifier = Modifier.padding(Dp(0f), Dp(220f), Dp(0f), Dp(0f))) {
                    ResizableSquare()
                    RotateLoader()
                }
        }
    }
}
@Composable
fun PerformedHTTPRequest() {
    val functions = Functions()
    var response = remember { mutableStateOf("Hello World!") }
    val currentTextValue = remember { mutableStateOf("") }
    val zxc by remember { mutableStateOf("") }
    Box {
        Column {
            Column(modifier = Modifier
                .padding(Dp(0f), Dp(40f), Dp(0f), Dp(0f))
                .weight(weight = 1f, fill = false)
                .fillMaxWidth()
                .size(200.dp)
                .verticalScroll(rememberScrollState(0))
            ) {
                Text(text = response.value, modifier = Modifier.padding(Dp(50f)))
            }
            Text(text = currentTextValue.value, modifier = Modifier.padding(Dp(50f)))
            OutlinedTextField(
                value = currentTextValue.value,
                onValueChange = { currentTextValue.value = it },
                label = { Text("Some Text") }
            )
            Button(onClick = { functions.performHTTP(currentTextValue.value, response) }) {
                Text(text = "Perform Request")
            }
        }
    }
}

@Composable
fun RotateLoader() {
    val animation = rememberInfiniteTransition()
    val angle = animation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )
    Text(text = "1234!", modifier = Modifier.rotate(angle.value))
}

@Composable
fun ResizableSquare() {
    var isExpanded by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val scaleFactor by animateFloatAsState(
        if (isExpanded) 2f else 1f, label = ""
    )
    val scaleWidth by animateDpAsState(
        if (isExpanded) screenWidth else 100.dp, label = ""
    )
    Box(
        modifier = Modifier
            .height(scaleFactor * 100.dp)
            .width(scaleWidth)
            .background(Color.Red)
            .clickable {
                isExpanded = !isExpanded
            }
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ARVRTheme {
        Greeting("Android")
    }
}