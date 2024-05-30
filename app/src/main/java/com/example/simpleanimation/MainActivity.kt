package com.example.simpleanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.simpleanimation.ui.theme.SimpleAnimationTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleAnimationTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color.LightGray) { innerPadding ->
                    AnimationContainer(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AnimationContainer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.background(Color.Cyan).padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SimpleAnimationExample()
        Spacer(Modifier.size(20.dp))
        SpringAnimationExample()
    }
}

@Composable
fun SimpleAnimationExample(modifier: Modifier = Modifier) {

    val config = LocalConfiguration.current
    val fullScreenWidth = config.screenWidthDp.dp
    val screenWidthHalf = config.screenWidthDp.dp/2

    var currentBoxSize by remember {
        mutableStateOf(screenWidthHalf)
    }

    val boxWidth by animateDpAsState(
        targetValue = currentBoxSize,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing),
        label = "width animatedSize"
    )

    Box(
        modifier = modifier
            .width(boxWidth)
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Button(
            modifier = Modifier
                .padding(all = 18.dp)
                .align(Alignment.Center),
            onClick = {
                currentBoxSize = if (currentBoxSize == screenWidthHalf) fullScreenWidth else screenWidthHalf
            }
        ) {
            Text("Toggle Size")
        }
    }
}

@Composable
fun SpringAnimationExample(modifier: Modifier = Modifier) {
    var targetValue by remember { mutableStateOf(0f) }
    var targetValueY by remember { mutableStateOf(0f) }

    val animationValueX by animateFloatAsState(
        targetValue = targetValue,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val animationValueY by animateFloatAsState(
        targetValue = targetValue,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessVeryLow
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(100.dp)
            .background(Color.Red)
            .offset { IntOffset(animationValueX.roundToInt(), animationValueY.roundToInt()) }
            .clickable {
                targetValue = if (targetValue == 0f) 200f else 0f
                targetValueY = if (targetValueY == 0f) 150f else 0f
            },
    ) {
        Text("Click me")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimpleAnimationTheme {
        AnimationContainer()
    }
}
