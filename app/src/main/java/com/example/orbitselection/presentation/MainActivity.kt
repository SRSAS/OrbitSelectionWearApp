package com.example.orbitselection.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PickerGroup
import androidx.wear.compose.material.PickerGroupItem
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerGroupState
import androidx.wear.compose.material.rememberPickerState
import com.example.orbitselection.presentation.network.SocketRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PageManager()
        }
    }

    @Composable
    fun PageManager() {

        var ip by rememberSaveable { mutableStateOf("") }

        // State variables for pickers
        val pickerGroupState = rememberPickerGroupState(3)
        val pickerStateOne = rememberPickerState(initialNumberOfOptions = 999, initiallySelectedOption = 192)
        val pickerStateTwo = rememberPickerState(initialNumberOfOptions = 999, initiallySelectedOption = 168)
        val pickerStateThree = rememberPickerState(initialNumberOfOptions = 999)
        val pickerStateFour = rememberPickerState(initialNumberOfOptions = 999)

        // If ip hasn't been selected, or has been reset, display ip selection page
        if ( ip.isEmpty()) {
            // Page is aligned vertically and fills the whole screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                verticalArrangement = Arrangement.Center

            ) {
                Text(
                    text = "IPV4",
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                // Confirm button
                Button(
                    onClick = {
                        ip = pickerStateOne.selectedOption.toString() + "." + pickerStateTwo.selectedOption.toString() + "." + pickerStateThree.selectedOption.toString() + "." + pickerStateFour.selectedOption.toString()
                        println(ip)
                    },
                    enabled = true,
                    shape = RectangleShape,
                    colors = ButtonDefaults.primaryButtonColors(backgroundColor = Color.Blue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)

                ) {
                    Text(
                        text = "Connect"
                        //fontSize = 15.sp
                    )
                }
                // Picker for picking IP, default value of 192.168.0.0
                PickerGroup(
                    PickerGroupItem(
                        pickerState = pickerStateOne,
                        option = {optionIndex, _ -> Text(text = optionIndex.toString())},
                    ),
                    PickerGroupItem(
                        pickerState = pickerStateTwo,
                        option = {optionIndex, _ -> Text(text = optionIndex.toString())},
                    ),
                    PickerGroupItem(
                        pickerState = pickerStateThree,
                        option = {optionIndex, _ -> Text(text = optionIndex.toString())},
                    ),
                    PickerGroupItem(
                        pickerState = pickerStateFour,
                        option = {optionIndex, _ -> Text(text = optionIndex.toString())},
                    ),
                    pickerGroupState = pickerGroupState,
                    autoCenter = false,
                    separator = {_ -> Text(text = " . ")}
                )
            }
        }
        else {
            var x = "0.000000"
            var y = "0.000000"

            // All black screen that detects drag gestures and sends input coordinates through UDP socket
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .pointerInput(ip) {
                        detectDragGestures(onDragEnd = {sendCoordinates(x, y, "U", ip)}) { change, _ ->
                            x = parseCoordinate(change.position.x.toString())
                            y = parseCoordinate(change.position.y.toString())
                            sendCoordinates(x, y, "M", ip)
                        }
                    },
                verticalArrangement = Arrangement.Center

            ) {
                Text(
                    text = "",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }

    /**
     * Turn coordinates to 8 char size string by either truncating or adding trailing zeros.
     * @param num the coordinate in its original length
     * @return the 8 length string coordinate
     */
    private fun parseCoordinate(num: String): String  {
        return if(num.length >= 8) {
            num.substring(0, 8)
        } else {
            num + "0".repeat(8 - num.length)
        }
    }

    /**
     * Send coordinates through a UDP Socket asynchronously
     * @param x string of length 8 with the x coordinate
     * @param y string of length 8 with the y coordinate
     * @param action weather the user is starting the movement, moving or lifting their finger
     * @param ip the ipv4 to send the coordinates to
     */
    private fun sendCoordinates(x: String, y: String, action: String, ip: String) = runBlocking {
        launch {
            SocketRepository.sendUDP(x, y, action, ip)
        }
    }
}