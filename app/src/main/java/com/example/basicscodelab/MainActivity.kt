package com.example.basicscodelab

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basicscodelab.ui.theme.BasicsCodelabTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items // A DIFFERENT IMPORT WOULD BE USED BY DEFAULT
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.font.FontWeight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContent define's the activity's layout
        // Composable functions replace XML files
        setContent {
            BasicsCodelabTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    // "by" removes the need from typing ".value" after "shouldShowOnboarding"
    /* As opposed to remember, rememberSaveable will save each state surviving configuration
        changes (eg rotations) and process death */
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    Surface(modifier) {
        if(shouldShowOnboarding) {
            /* The callback "onContinueClicked" is a callback (function that is passed as an
                argument into another function to get executed when that event occurs */
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            Greetings()
        }
    }
}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit, // onContinueClicked state can be mutated from MyApp
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}

// Functions should include empty modifier parameter, which is forwarded to the 1st composable
// called in the function
@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    // $it represents the List index, so names 0-whatever will be created
    names: List<String> = List(1000) { "$it" }
) {
    /* LazyColumn API provides an items element to (I think) get the items that will actually be
        displayed on the screen */
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) {
            name -> Greeting(name = name)
        }
    }
}

// "@Composable" enables a function to call other "@Composable" functions within it
@Composable
fun Greeting(name: String) {
    // State/MutableState interfaces hold a value and trigger UI updates when that value changes
    /* To preserve the state across recompositions (in this case, of the Greeting composable),
        use "remember" */
    // Each Greeting composable has its own version of the expanded state, like a private variable
    var expanded by remember { mutableStateOf(false) }
    /* extraPadding is calculated each time a Greeting is recomposed, so it doesn't need to be
        remembered */
    // animateDpAsState causes the animated expansion of the composables
    // Optional animationSpec parameter gives the animation its springiness - note .coerceAtLeast(0.dp)
    val extraPadding by animateDpAsState(
        if(expanded) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    //Components nested in Surface will be drawn on top of Surface, which gives the background color
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                /* Optional modifier parameters "tell a UI element how to lay out, display,
                    or behave within the parent layout */
                /* ".fillMaxWidth()" is redundant since weight() causes the Column to grow
                    as large as it can, pushing the Elevated button to the right */
                modifier = Modifier
                    .weight(1f)
                    /* .coerceAtLeast ensures padding is never negative, since that would crash
                        the app */
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
            ) {
                Text("Hello,")
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                ))
            }
            ElevatedButton(
                onClick = { expanded = !expanded }
            ) {
                Text(if(expanded) "Show less" else "Show more")
            }
        }
    }
}

@Preview
@Composable
fun MyAppPreview() {
    BasicsCodelabTheme {
        MyApp(Modifier.fillMaxSize())
    }
}

// Any permameterless Composable functions should be marked with "@Preview" in order ot use the
// preview window
@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingsPreview() {
    BasicsCodelabTheme {
        Greetings()
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    BasicsCodelabTheme {
        OnboardingScreen(onContinueClicked = {}) // Nothing is done on click
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "Dark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    BasicsCodelabTheme {
        Greetings()
    }
}