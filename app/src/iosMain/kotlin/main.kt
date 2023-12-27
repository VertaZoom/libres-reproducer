import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import com.example.reproducer.library.App

fun MainViewController(): UIViewController {
    return ComposeUIViewController { App() }
}