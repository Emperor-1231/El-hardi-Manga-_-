package eu.kanade.presentation.browse.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.kanade.domain.manga.model.Manga
import eu.kanade.presentation.components.Badge
import eu.kanade.presentation.components.BadgeGroup
import eu.kanade.presentation.components.MangaCover
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.ui.browse.migration.advanced.process.MigratingManga
import eu.kanade.tachiyomi.util.lang.withIOContext

@Composable
fun MigrationItem(
    modifier: Modifier,
    manga: Manga,
    sourcesString: String,
    chapterInfo: MigratingManga.ChapterInfo,
    onClick: () -> Unit,
) {
    Column(
        modifier
            .widthIn(max = 150.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .padding(4.dp),
    ) {
        val context = LocalContext.current
        Box(
            Modifier.fillMaxWidth()
                .aspectRatio(MangaCover.Book.ratio),
        ) {
            MangaCover.Book(
                modifier = Modifier
                    .fillMaxWidth(),
                data = manga,
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            1f to Color(0xAA000000),
                        ),
                    )
                    .fillMaxHeight(0.33f)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
            )
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomStart),
                text = manga.title.ifBlank { stringResource(R.string.unknown) },
                fontSize = 12.sp,
                lineHeight = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.White,
                    shadow = Shadow(
                        color = Color.Black,
                        blurRadius = 4f,
                    ),
                ),
            )
            BadgeGroup(modifier = Modifier.padding(4.dp)) {
                Badge(text = "${chapterInfo.chapterCount}")
            }
        }
        Text(
            text = sourcesString,
            modifier = Modifier.padding(top = 4.dp, bottom = 1.dp, start = 8.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.titleSmall,
        )

        val formattedLatestChapter by produceState(initialValue = "") {
            value = withIOContext {
                chapterInfo.getFormattedLatestChapter(context)
            }
        }
        Text(
            text = formattedLatestChapter,
            modifier = Modifier.padding(top = 1.dp, bottom = 4.dp, start = 8.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}