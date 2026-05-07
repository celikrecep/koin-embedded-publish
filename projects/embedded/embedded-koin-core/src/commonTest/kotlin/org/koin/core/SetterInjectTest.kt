package embedded.koin.core

import embedded.koin.core.component.KoinComponent
import embedded.koin.core.component.KoinScopeComponent
import embedded.koin.core.component.createScope
import embedded.koin.core.component.inject
import embedded.koin.core.context.startKoin
import embedded.koin.core.context.stopKoin
import embedded.koin.core.scope.Scope
import embedded.koin.core.time.inMs
import embedded.koin.dsl.module
import embedded.koin.ext.inject
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.time.measureTime

class B : KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }
}

class C
class D

class BofA(val a: A) : KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }
}

class CofB(val b: BofA)

class A : KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }
    lateinit var b: B
    lateinit var c: C
}

class A_inj : KoinComponent {
    val b: B by inject()
    val c: C by inject()
}

class PlayTest {

    @AfterTest
    fun after(){
        stopKoin()
    }

    @Test
    fun setter_injection() {
        stopKoin()
        val koin = startKoin {
            modules(
                module {
                    single { B() }
                    single { C() }
                },
            )
        }.koin

        measureDuration("by inject") {
            val ai = A_inj()
            ai.b
            ai.c
        }

        measureDuration("prop get") {
            val a = A()
            a.b = koin.get()
            a.c = koin.get()
        }

        measureDuration("prop inject") {
            val a = A()
            a::b.inject()
            a::c.inject()
        }

        stopKoin()
    }
}

fun measureDuration(msg: String, code: () -> Unit): Double {
    val duration = measureTime(code)
    println("$msg in $duration ms")
    return duration.inMs
}
