package ir.inspectionmanager.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.inspectionmanager.presentation.screen.components.DatePickerDialog
import ir.inspectionmanager.presentation.screen.components.FormLabel
import ir.inspectionmanager.presentation.viewmodel.InspectionViewModel
import ir.inspectionmanager.util.PersianDateHelper

@Composable
fun AddInspectionScreen(
    viewModel: InspectionViewModel,
    onBackClick: () -> Unit
) {
    var selectedDate by remember {
        mutableStateOf(PersianDateHelper.todayJalali())
    }

    var selectedCity by remember {
        mutableStateOf("")
    }

    var citySearchQuery by remember {
        mutableStateOf("")
    }

    var repCode by remember {
        mutableStateOf("")
    }

    var inspectionCount by remember {
        mutableStateOf("")
    }

    var problemCount by remember {
        mutableStateOf("")
    }

    var problemDescription by remember {
        mutableStateOf("")
    }

    var showCityDropdown by remember {
        mutableStateOf(false)
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val cities by viewModel.cities.collectAsState()

    val filteredCities = if (citySearchQuery.isEmpty()) {
        cities.take(5)
    } else {
        cities.filter {
            it.name.contains(
                citySearchQuery,
                ignoreCase = true
            )
        }
    }

    val inspectorName by viewModel.inspectorName.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick
            ) {
                Text(
                    text = "←",
                    fontSize = 24.sp
                )
            }

            Text(
                text = "ثبت بازرسی جدید",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.width(40.dp)
            )
        }

        FormLabel("نام بازرس")

        Text(
            text = inspectorName,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFFE8E8E8),
                    RoundedCornerShape(8.dp)
                )
                .padding(12.dp),
            fontSize = 16.sp,
            textAlign = TextAlign.End
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormLabel("تاریخ (شمسی)")

        Button(
            onClick = {
                showDatePicker = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Text(
                text = selectedDate,
                color = Color.Black,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showDatePicker) {
            DatePickerDialog(
                selectedDate = selectedDate,
                onDateSelected = {
                    selectedDate = it
                    showDatePicker = false
                },
                onDismiss = {
                    showDatePicker = false
                }
            )
        }

        FormLabel("شهر محل بازرسی")

        OutlinedTextField(
            value = if (showCityDropdown) {
                citySearchQuery
            } else {
                selectedCity
            },
            onValueChange = {
                citySearchQuery = it
                showCityDropdown = true
            },
            placeholder = {
                Text("شهر را انتخاب کنید")
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                textAlign = TextAlign.End
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "جستجوی شهر"
                )
            },
            singleLine = true
        )

        if (showCityDropdown) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        RoundedCornerShape(8.dp)
                    )
                    .border(
                        1.dp,
                        Color(0xFFBBBBBB),
                        RoundedCornerShape(8.dp)
                    )
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {

                    items(filteredCities) { city ->

                        Text(
                            text = city.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCity = city.name
                                    citySearchQuery = ""
                                    showCityDropdown = false
                                }
                                .padding(12.dp),
                            fontSize = 16.sp,
                            textAlign = TextAlign.End
                        )
                    }

                    if (
                        citySearchQuery.isNotEmpty() &&
                        filteredCities.none {
                            it.name.equals(
                                citySearchQuery,
                                ignoreCase = true
                            )
                        }
                    ) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedCity = citySearchQuery
                                        citySearchQuery = ""
                                        showCityDropdown = false
                                    }
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "➕ افزودن: $citySearchQuery",
                                    fontSize = 16.sp,
                                    color = Color(0xFF4CAF50),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        FormLabel("کد نماینده / فروشگاه")

        OutlinedTextField(
            value = repCode,
            onValueChange = {
                repCode = it
            },
            placeholder = {
                Text("مثال: 1254")
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                textAlign = TextAlign.End
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormLabel("تعداد بازرسی")

        OutlinedTextField(
            value = inspectionCount,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    inspectionCount = it
                }
            },
            placeholder = {
                Text("تعداد")
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                textAlign = TextAlign.End
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormLabel("تعداد مشکلات")

        OutlinedTextField(
            value = problemCount,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    problemCount = it
                }
            },
            placeholder = {
                Text("تعداد")
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                textAlign = TextAlign.End
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormLabel("شرح مشکلات")

        OutlinedTextField(
            value = problemDescription,
            onValueChange = {
                problemDescription = it
            },
            placeholder = {
                Text("شرح مشکلات (اختیاری)")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            textStyle = TextStyle(
                textAlign = TextAlign.End
            ),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (
                    selectedCity.isNotEmpty() &&
                    repCode.isNotEmpty() &&
                    inspectionCount.isNotEmpty() &&
                    problemCount.isNotEmpty()
                ) {
                    viewModel.addInspection(
                        city = selectedCity,
                        repCode = repCode,
                        inspectionCount = inspectionCount.toInt(),
                        problemCount = problemCount.toInt(),
                        problemDescription = problemDescription,
                        dateJalali = selectedDate
                    )

                    onBackClick()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Text(
                text = "ثبت بازرسی",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
