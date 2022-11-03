package com.github.toricor.currentlocationsample

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.background
import androidx.glance.text.Text

class HelloWorldWidget: GlanceAppWidget() {
    @Composable
    override fun Content() {
        Text(
            text = "Hello world",
            modifier = GlanceModifier.background(Color.Gray)
        )
    }
}

class HelloWorldWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = HelloWorldWidget()
}

