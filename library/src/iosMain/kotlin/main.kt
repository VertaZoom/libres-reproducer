import androidx.compose.ui.window.ComposeUIViewController
import com.example.reproducer.library.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController { App() }
}
