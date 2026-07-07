package kg.edu.yjut.morseinputmethod.services

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.compose.ui.platform.ComposeView
import kg.edu.yjut.morseinputmethod.view.MorseKeyboardScreen
import kg.edu.yjut.morseinputmethod.viewmodel.MorseViewModel

class MorseInputService : InputMethodService() {

    private var viewModel: MorseViewModel? = null
    private var composeView: ComposeView? = null

    override fun onCreateInputView(): View {
        viewModel = MorseViewModel(this)
        composeView = ComposeView(this).apply {
            setContent {
                MorseKeyboardScreen(
                    viewModel = viewModel!!,
                    onHideKeyboard = {
                        hideWindow()
                    }
                )
            }
        }
        return composeView!!
    }

    override fun onStartInput(attribute: EditorInfo?, restarting: Boolean) {
        super.onStartInput(attribute, restarting)
        viewModel?.inputConnection = currentInputConnection
        viewModel?.clearInput()
    }

    override fun onFinishInput() {
        super.onFinishInput()
        viewModel?.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.release()
        viewModel = null
        composeView = null
    }
}