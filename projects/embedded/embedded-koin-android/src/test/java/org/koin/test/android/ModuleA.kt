package embedded.koin.test.android

import embedded.koin.dsl.module

val ModuleA = module {
    includes(ModuleB)
    factory(qualifierA) { Person(parent = null) }
}