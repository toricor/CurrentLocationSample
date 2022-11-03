package com.github.toricor.currentlocationsample

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle

class HelloWorldWidget : GlanceAppWidget() {
    @Composable
    override fun Content() {
        Text(
            text = "Hello world",
            modifier = GlanceModifier
                .clickable(actionStartActivity<MainActivity>())
                .fillMaxSize()
                .background(Color(0, 200, 0)),
            style = TextStyle(textAlign = TextAlign.Center, )
        )
    }
}

class HelloWorldWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = HelloWorldWidget()
}

