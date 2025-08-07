package com.madinaapps.iarmasjid.composable.qibla

import android.location.Location
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.Qibla
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.viewModel.CompassViewModel

@Composable
fun CompassView(
    location: Location,
    viewModel: CompassViewModel = hiltViewModel<CompassViewModel>()
) {
    val context = LocalContext.current
    val currentOrientation by viewModel.currentOrientation.collectAsState()
    val compassAngle by viewModel.compassAngle.collectAsState()
    val percentCorrect by viewModel.percentCorrect.collectAsState()

    var wasFacingQibla by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val qibla = remember(location) { Qibla(Coordinates(location.latitude, location.longitude)) }

    val showExpandedMessage = remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (showExpandedMessage.value) 180f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    fun accuracyMessage(): String {
        if (showExpandedMessage.value) {
            return "Compass direction may not be 100% accurate when used inside or near electric or magnetic interference. Please verify with map overlay."
        } else {
            return "Compass direction may not be 100% accurate."
        }
    }

    DisposableEffect(context) {
        viewModel.startListening()
        onDispose {
            viewModel.stopListening()
        }
    }

    LaunchedEffect(location) {
        viewModel.qibla = qibla
    }

    LaunchedEffect(percentCorrect) {
        if (percentCorrect > 0 && !wasFacingQibla) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        } else if(percentCorrect <= 0f && wasFacingQibla) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
        wasFacingQibla = percentCorrect > 0
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        currentOrientation?.also { orientation ->
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = percentCorrect), shape = CircleShape)
                )
                Spacer(modifier = Modifier.height(32.dp))
                CompassArrow(compassAngle, percentCorrect)
                Row(
                    modifier = Modifier.padding(top = 8.dp).alpha(percentCorrect)
                ) {
                    Text(
                        "You're Facing ",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Makkah",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(modifier = Modifier
                .padding(bottom = 16.dp)
                .padding(top = 32.dp)
            ) {
                Button(
                    onClick = {
                        showExpandedMessage.value = !showExpandedMessage.value
                    },
                    elevation = null,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = PaddingValues(16.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.animateContentSize()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_info),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp, 20.dp))
                        Text(accuracyMessage(),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                            maxLines = if (showExpandedMessage.value) Int.MAX_VALUE else 1,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            //painter = painterResource(id = if (showExpandedMessage.value) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down),
                            painter = painterResource(id = R.drawable.ic_chevron_down),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(20.dp, 20.dp)
                                .rotate(rotationAngle)
                        )
                    }
                }
            }
        }
    }
}