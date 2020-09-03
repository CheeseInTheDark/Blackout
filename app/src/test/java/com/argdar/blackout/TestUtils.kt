package com.argdar.blackout

import io.mockk.mockk
import org.koin.core.context.startKoin
import org.koin.dsl.ModuleDeclaration
import kotlin.reflect.KClass

inline fun <reified T : Any> mock(): T = mockk(relaxed = true, relaxUnitFun = true)
fun koin(module: ModuleDeclaration) =
    startKoin {
        modules(
            org.koin.dsl.module {
                module()
            }
        )
    }