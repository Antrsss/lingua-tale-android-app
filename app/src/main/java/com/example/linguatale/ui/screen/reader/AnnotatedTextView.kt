/*
package com.example.linguatale.ui.screen.reader

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.example.linguatale.domain.dto.OccurrenceDto

@Composable
fun AnnotatedTextView(
    text: String,
    occurrences: List<OccurrenceDto>,
    modifier: Modifier = Modifier
) {
    val annotatedString = remember(text, occurrences) {
        buildAnnotatedString {
            append(text)

            for (occ in occurrences) {
                if (occ.startOffset >= text.length ||
                    occ.endOffset > text.length) continue

                val color = posColor(occ.partOfSpeech, occ.status)
                addStyle(
                    style = SpanStyle(
                        color = color,
                        background = color.copy(alpha = 0.15f)
                    ),
                    start = occ.startOffset,
                    end = occ.endOffset
                )

                // Tag the span so taps can be detected
                addStringAnnotation(
                    tag = "WORD",
                    annotation = occ.wordId,
                    start = occ.startOffset,
                    end = occ.endOffset
                )
            }
        }
    }

    ClickableText(
        text = annotatedString,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        onClick = { offset ->
            annotatedString.getStringAnnotations("WORD", offset, offset)
                .firstOrNull()
                ?.let { annotation ->
                    // TODO: open word definition bottom sheet
                }
        }
    )
}

// Color per POS + status — shown in the client, never in the server
fun posColor(pos: String, status: String): Color {
    if (status == "KNOWN") return Color.Unspecified   // no highlight for known words
    return when (pos) {
        "NOUN" -> Color(0xFF6B9FD4)    // blue
        "VERB" -> Color(0xFF7BC67E)    // green
        "ADJ"  -> Color(0xFFE8A838)    // amber
        "ADV"  -> Color(0xFFB57BCA)    // purple
        else   -> Color(0xFF9E9E9E)    // grey for everything else
    }
}*/
