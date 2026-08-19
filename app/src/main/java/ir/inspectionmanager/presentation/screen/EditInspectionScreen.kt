package ir.inspectionmanager.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import ir.inspectionmanager.data.database.entity.InspectionEntity
import ir.inspectionmanager.presentation.screen.components.FormLabel
import ir.inspectionmanager.presentation.viewmodel.InspectionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun EditInspectionScreen(
    viewModel: InspectionViewModel,
    inspectionId: Long,
    onBackClick: () -> Unit
) {
    var inspection by remember {
        mutableStateOf<InspectionEntity?>(null)
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(inspectionId) {
        inspection = withContext(Dispatchers.IO) {
            viewModel.getInspectionById(inspectionId)
        }

        isLoading = false
    }

    if (isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }

        return
    }

    val insp = inspection ?: return

    var repCode by remember {
        mutableStateOf(insp.representativeCode)
    }

    var inspectionCount by remember {
        mutableStateOf(insp.inspectionCount.toString())
    }

    var problemCount by remember {
        mutableStateOf(insp.problemCount.toString())
    }

    var problemDescription by remember {
        mutableStateOf(insp.problemDescription)
    }

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
                text = "ویرایش بازرسی",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.width(40.dp)
            )
        }

        FormLabel("تاریخ (شمسی)")

        Text(
            text = insp.dateJalali,
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

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        FormLabel("شهر محل بازرسی")

        Text(
            text = insp.city,
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

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        FormLabel("کد نماینده / فروشگاه")

        OutlinedTextField(
            value = repCode,
            onValueChange = {
                repCode = it
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                textAlign = TextAlign.End
            ),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        FormLabel("تعداد بازرسی")

        OutlinedTextField(
            value = inspectionCount,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    inspectionCount = it
                }
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

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        FormLabel("تعداد مشکلات")

        OutlinedTextField(
            value = problemCount,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    problemCount = it
                }
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

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        FormLabel("شرح مشکلات")

        OutlinedTextField(
            value = problemDescription,
            onValueChange = {
                problemDescription = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            textStyle = TextStyle(
                textAlign = TextAlign.End
            ),
            maxLines = 5
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = {

                val updatedInspection = insp.copy(
                    representativeCode = repCode,
                    inspectionCount =
                        inspectionCount.toIntOrNull() ?: 0,
                    problemCount =
                        problemCount.toIntOrNull() ?: 0,
                    problemDescription = problemDescription
                )

                viewModel.updateInspection(updatedInspection)

                onBackClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            )
        ) {
            Text(
                text = "به‌روزرسانی",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )
    }
}
