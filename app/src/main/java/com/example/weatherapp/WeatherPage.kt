package com.example.weatherapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import coil.compose.AsyncImage
import com.example.weatherapp.api.NetworkResponse

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun getWeather(viewModel: WeatherViewModel) {
  var city by remember {
    mutableStateOf("")
  }
  val weatherResult = viewModel.weatherResponse.observeAsState()
  val keyboardController = LocalSoftwareKeyboardController.current
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      OutlinedTextField(
        modifier = Modifier.weight(1f),
        value = city,
        onValueChange = {
          city = it
        },
        label = {
          Text(text = "Search for any location here")
        })
      IconButton(onClick = { viewModel.getData(city)
      keyboardController?.hide()}) {
        Icon(imageVector = Icons.Default.Search, contentDescription = "Search for any location")
      }
    }
    when (val result = weatherResult.value) {
      is NetworkResponse.Success -> {
        WeatherDetails(data = result.data)
      }

      is NetworkResponse.Error -> {
        Text(text = "Fail to load")
      }

      is NetworkResponse.Loading -> {
        CircularProgressIndicator()
      }

      else -> {}
    }
  }

}

@Composable
fun WeatherDetails(data: WeatherModel) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Start,
      verticalAlignment = Alignment.Bottom
    ) {
      Icon(
        imageVector = Icons.Default.LocationOn, contentDescription = "Location Icon",
        modifier = Modifier.size(40.dp)
      )
      Text(text = data.location.name, fontSize = 30.sp)
      Spacer(modifier = Modifier.width(10.dp))
      Text(text = data.location.country, fontSize = 18.sp, color = Color.Gray)
    }
    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "${data.current.temp_c} ° c",
      fontSize = 56.sp,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center
    )
    AsyncImage(
      modifier = Modifier.size(160.dp),
      model = "https:${data.current.condition.icon}".replace("64X64", "120X120"),
      contentDescription = "Condition Icon"
    )
    Text(
      text = data.current.condition.text,
      fontSize = 20.sp,
      textAlign = TextAlign.Center,
      color = Color.Gray
    )
    Spacer(modifier = Modifier.height(16.dp))
    Card(modifier = Modifier.fillMaxWidth()) {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
          WeatherKeyValue(key = "Humidity", value = data.current.humidity)
          WeatherKeyValue(key = "Wind Speed", value = data.current.wind_kph + "km/h")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
          WeatherKeyValue(key = "UV", value = data.current.uv)
          WeatherKeyValue(key = "Percipation", value = data.current.precip_in)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
          WeatherKeyValue(key = "Local Time", value = data.location.localtime.split(" ")[1])
          WeatherKeyValue(key = "Local Date", value = data.location.localtime.split(" ")[0])

        }
      }
    }
  }
}

@Composable
fun WeatherKeyValue(key: String, value: String) {
  Column(
    modifier = Modifier.padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = key, fontWeight = FontWeight.Bold,
      fontSize = 20.sp
    )
    Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
  }

}