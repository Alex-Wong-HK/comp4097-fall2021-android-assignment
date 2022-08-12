package json


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hk.edu.hkbu.comp.comp4097.qpon.Greeting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class Mall (
    val mall: String,
    val latitude: Float,
    val longitude: Float,
)
object MallViewModel: ViewModel() {
    private val _malls = MutableLiveData(mutableListOf<Mall>())
    var data: LiveData<MutableList<Mall>> = _malls
    init {
        reloadData()
    }
    private fun reloadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val json = "[\n" +
                    "  {\n" +
                    "    \"mall\": \"IFC Mall\",\n" +
                    "    \"latitude\": 22.2849,\n" +
                    "    \"longitude\": 114.158917\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"mall\": \"Pacific Place\",\n" +
                    "    \"latitude\": 22.2774985,\n" +
                    "    \"longitude\": 114.1663225\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"mall\": \"Times Square\",\n" +
                    "    \"latitude\": 22.2782079,\n" +
                    "    \"longitude\": 114.1822994\n" +
                    "  }, \n" +
                    "  {\n" +
                    "    \"mall\": \"Elements\",\n" +
                    "    \"latitude\": 22.3048708,\n" +
                    "    \"longitude\": 114.1615219\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"mall\": \"Harbour City\",\n" +
                    "    \"latitude\": 22.2950689,\n" +
                    "    \"longitude\": 114.1668661\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"mall\": \"Festival Walk\",\n" +
                    "    \"latitude\": 22.3372971,\n" +
                    "    \"longitude\": 114.1745273\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"mall\": \"MegaBox\",\n" +
                    "    \"latitude\": 22.319857,\n" +
                    "    \"longitude\": 114.208168\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"mall\": \"APM\",\n" +
                    "    \"latitude\": 22.3121738,\n" +
                    "    \"longitude\": 114.22513219999996\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"mall\": \"Tsuen Wan Plaza\",\n" +
                    "    \"latitude\": 22.370735,\n" +
                    "    \"longitude\": 114.111309\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"mall\": \"New Town Plaza\",\n" +
                    "    \"latitude\": 22.3814592,\n" +
                    "    \"longitude\": 114.1889307\n" +
                    "  }\n" +
                    "]"
            MallViewModel._malls.postValue(mutableListOf<Mall>().apply {
                addAll(Gson().fromJson<List<Mall>>(json,object : TypeToken<List<Mall>>() {}.type))
            })
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalCoilApi
@Composable
fun Mall(nav:NavHostController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Mall") }) }) {
        val data by MallViewModel.data.observeAsState(listOf())
        LazyColumn(modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 64.dp)) {
            items(data, itemContent = { item ->
                ListItem( modifier = Modifier.clickable {
                    nav.navigate("mall/${item.mall}")
                }) {

                    Text(item.mall)
                }
            })
        }
    }
}