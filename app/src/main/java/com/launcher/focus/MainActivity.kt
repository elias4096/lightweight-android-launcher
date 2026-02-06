package com.launcher.focus

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.launcher.focus.ui.theme.FocusTheme
import kotlin.collections.forEach

class MainActivity : ComponentActivity() {
    private lateinit var packageManager: PackageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        packageManager = applicationContext.packageManager
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val apps: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)

        setContent {
            FocusTheme {
                AppsDisplay(apps, packageManager)
            }
        }
    }
}

@Composable
fun AppsDisplay(apps: List<ResolveInfo>, pm: PackageManager) {
    Column (modifier = Modifier.fillMaxWidth().background(Color.Black).verticalScroll(rememberScrollState())) {
        apps.forEach { app ->
            AppButton(app, pm)
        }
    }
}

@Composable
fun AppButton(app: ResolveInfo, pm: PackageManager) {
    val context = LocalContext.current

    Button(
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.White),
        shape = RectangleShape,
        onClick = {
            val launchIntent = pm.getLaunchIntentForPackage(app.activityInfo.packageName)

            if (launchIntent == null) {
                Toast.makeText(context, "Failed to open app", Toast.LENGTH_SHORT).show()
            }

            context.startActivity(launchIntent)
        }) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${app.loadLabel(pm)}",
            textAlign = TextAlign.Start,
            fontSize = 45.sp,
        )
    }
}
