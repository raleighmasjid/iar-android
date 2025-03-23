package com.madinaapps.iarmasjid.composable.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.madinaapps.iarmasjid.R

@Composable
fun MoreRow(onClick: () -> Unit, content: @Composable() () -> Unit) {
    Card(
        elevation = 0.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1.0f)) {
                content()
            }
            Icon(
                painterResource(id = R.drawable.ic_nav_chevron),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(6.dp, 12.dp))
        }
    }
}