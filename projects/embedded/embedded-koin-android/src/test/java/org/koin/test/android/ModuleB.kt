package embedded.koin.test.android

import embedded.koin.dsl.module

val ModuleB = module {
    includes(ModuleC)
    factory(qualifierB) { Person(parent = get(qualifierA)) }
}