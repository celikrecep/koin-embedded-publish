import embedded.koin.core.annotation.InjectedParam
import embedded.koin.core.annotation.Provided
import embedded.koin.core.qualifier.Qualifier
import embedded.koin.mp.KoinPlatformTools
import embedded.koin.mp.generateId

@Suppress("unused")
class Others {
    class ComponentA
    class ComponentB(val a: ComponentA)
    class ComponentBParam(@InjectedParam val a: ComponentA)
    class ComponentBProvided(@Provided val a: ComponentA)
    class ComponentBProvided2(@Provided val a: ComponentA)
    class ComponentC(val b: ComponentB)
    class MyString(val s: String)

    class UUIDComponent {
        fun getUUID() = KoinPlatformTools.generateId()
    }
}

object UpperCase : Qualifier {
    override val value: String = "UpperCase"
}
