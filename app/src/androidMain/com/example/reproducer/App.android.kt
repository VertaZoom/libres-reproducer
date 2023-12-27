package uk.co.swfy.grc

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import uk.co.swfy.grc.core.util.initAnalytics

class GRCApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        initAnalytics()
    }
}