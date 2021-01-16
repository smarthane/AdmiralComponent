package com.smarthane.admiralcomponent.jetpack.app

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.smarthane.admiral.core.base.AppComponent
import com.smarthane.admiral.core.base.delegate.AppLifecycles
import com.smarthane.admiral.core.base.module.GlobalConfigModule
import com.smarthane.admiral.core.integration.ConfigModule
import com.smarthane.admiral.core.integration.cache.IntelligentCache
import com.smarthane.admiralcomponent.jetpack.BuildConfig
import com.squareup.leakcanary.RefWatcher

/**
 * @author smarthane
 * @time 2019/11/10 18:16
 * @describe
 */
class GlobalConfiguration : ConfigModule {

    override fun applyOptions(context: Context, builder: GlobalConfigModule.Builder) {
    }

    override fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycles>) {
        lifecycles.add(AppLifecyclesImpl())
    }

    override fun injectActivityLifecycle(context: Context, lifecycles: MutableList<Application.ActivityLifecycleCallbacks>) {
    }

    override fun injectFragmentLifecycle(context: Context, lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>) {
        if (BuildConfig.IS_BUILD_MODULE) {
            lifecycles.add(object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                    (AppComponent.get().fetchExtras()[IntelligentCache.getKeyOfKeep(RefWatcher::class.java.name)] as RefWatcher?)
                            ?.watch(f)
                }
            })
        }
    }
}