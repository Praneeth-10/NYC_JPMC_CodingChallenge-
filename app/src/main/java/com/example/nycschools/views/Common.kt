package com.example.nycschools.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nycschools.utils.Utils

@Preview
@Composable
fun Header() {
    Surface(
        modifier = Modifier.fillMaxWidth().height(56.dp),
        color = MaterialTheme.colorScheme.primary,
        contentColor = contentColorFor(MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().shadow(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = Utils.APP_NAME,
                style = MaterialTheme.typography.titleLarge.copy(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary,
//                modifier = Modifier.shadow(4.dp)
            )
        }
    }
}



fun annotatedStringBuilder(normalText:String, boldText:String): AnnotatedString {
    return buildAnnotatedString {
        append(normalText)
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,fontStyle = FontStyle.Italic)) {
            append(boldText)
        }
    }
}