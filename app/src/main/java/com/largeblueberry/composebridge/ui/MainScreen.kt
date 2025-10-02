package com.largeblueberry.composebridge.ui


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.largeblueberry.composebridge.ui.theme.ComposeBridgeTheme

// 하단 네비게이션 아이템을 위한 데이터 클래스
data class NavItem(val label: String, val icon: ImageVector, val route: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedItem by remember { mutableStateOf(0) }
    val navItems = listOf(
        NavItem("최근 작품", Icons.Default.Home, "recent"),
        NavItem("화면 탐색", Icons.Default.Search, "explore"),
        NavItem("설정", Icons.Default.Settings, "settings")
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            // 선택된 탭에 따라 다른 화면을 보여줌
            when (selectedItem) {
                0 -> RecentWorksScreen() // 최근 작품 화면
                1 -> ScreenExplorer(
                    screens = uiState.screenList,
                    onScreenClick = { screenName ->
                        // TODO: 화면 클릭 시 동작 (예: 편집 화면으로 이동)
                        println("$screenName 클릭됨")
                    },
                    onEditClick = { screenName ->
                        // TODO: 편집 버튼 클릭 시 동작
                        println("$screenName 편집 시작")
                    }
                ) // 화면 탐색
                2 -> SettingsScreen() // 설정 화면
            }
        }
    }
}

@Composable
fun ScreenExplorer(
    screens: List<String>,
    onScreenClick: (String) -> Unit,
    onEditClick: (String) -> Unit
) {
    // 주석에서 요청하신 리사이클러 뷰 -> LazyColumn으로 구현
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(screens) { screenName ->
            ScreenItem(
                screenName = screenName,
                onItemClick = { onScreenClick(screenName) },
                onEditClick = { onEditClick(screenName) }
            )
        }
    }
}

@Composable
fun ScreenItem(screenName: String, onItemClick: () -> Unit, onEditClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = screenName, style = MaterialTheme.typography.bodyLarge)
            Button(onClick = onEditClick) {
                Text("편집")
            }
        }
    }
}

// 임시로 만든 더미 화면들
@Composable
fun RecentWorksScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("최근 작품 화면")
    }
}

@Composable
fun SettingsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("설정 화면")
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun MainScreenPreview() {
    // 앱 테마를 적용하고, isPreview 플래그로 ViewModel을 생성하여 전달합니다.
    ComposeBridgeTheme {
        MainScreen(viewModel = MainViewModel(isPreview = true))
    }
}