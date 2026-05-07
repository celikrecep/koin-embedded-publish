package embedded.koin.test.android

import embedded.koin.dsl.module

val ModuleC = module {
    factory(qualifierC) { Person(parent = get(qualifierB)) }
}