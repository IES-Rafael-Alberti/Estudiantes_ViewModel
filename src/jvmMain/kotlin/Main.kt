import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import java.io.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    val studentsFilePath = File("src/jvmMain/kotlin/students.txt").absolutePath
    val studentsFile = File(studentsFilePath)

    val icon = painterResource("icon.png")

    val viewModel = StudentsViewModel(studentsFile)

    Window(
        onCloseRequest = ::exitApplication,
        title = "My students",
        state = rememberWindowState(width = 600.dp, height = 600.dp),
        icon = icon
    ) {
        MaterialTheme {
            StudentScreen(viewModel)
        }
    }
}

@Composable
fun TextBoxWithDeleteIcon(text: String, onDeleteClicked: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        TextBox(text = text, modifier = Modifier.weight(1f))
        IconButton(onClick = onDeleteClicked) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@Composable
fun TextBox(text: String = "Item", modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.height(32.dp)
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(start = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = text)
    }
}

@Composable
fun StudentScreen(
    viewModel: StudentsViewModel
) {
    val state = rememberLazyListState()

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.weight(1f)) {
            Column(
                Modifier.weight(1f).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.newStudent,
                    onValueChange = viewModel::newStudentChange,
                    label = { Text("New student name") },
                    modifier = Modifier.fillMaxWidth().padding(top = 100.dp)
                )
                Button(
                    onClick = viewModel::addStudent,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Add new student")
                }
            }
            Box(
                Modifier.weight(1f).padding(end = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Students: ${viewModel.students.size}",
                        modifier = Modifier.padding(8.dp)
                    )
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .border(1.dp, Color.Black)
                    ) {
                        LazyColumn(state = state) {
                            itemsIndexed(viewModel.students) { index, student ->
                                TextBoxWithDeleteIcon(student) {
                                    viewModel.removeStudent(index)
                                }
                            }
                        }
                        VerticalScrollbar(
                            adapter = rememberScrollbarAdapter(scrollState = state),
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                    Button(
                        onClick = viewModel::clearStudents,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Clear All")
                    }
                }
            }
        }
        Button(
            onClick = viewModel::saveChanges,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Save Changes")
        }
    }
}
