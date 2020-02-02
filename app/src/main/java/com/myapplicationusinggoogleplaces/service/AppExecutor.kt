package com.myapplicationusinggoogleplaces.service

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class AppExecutor(
    private val diskIO: Executor,
    private val mainThread: Executor,
    private val networkIO: Executor
) {
    fun diskIO(): Executor {
        return diskIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    fun networkIO(): Executor {
        return networkIO
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(runnable: Runnable) {
            mainThreadHandler.post(runnable)
        }
    }

    companion object {
        private val LOCK = Any()
        private var sInstance: AppExecutor? = null
        val instance: AppExecutor?
            get() {
                if (sInstance == null) {
                    synchronized(
                        LOCK
                    ) {
                        sInstance =
                            AppExecutor(
                                Executors.newSingleThreadExecutor(),
                                Executors.newFixedThreadPool(10),
                                MainThreadExecutor()
                            )
                    }
                }
                return sInstance
            }
    }

}