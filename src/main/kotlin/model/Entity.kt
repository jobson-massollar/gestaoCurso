package model

import kotlin.uuid.Uuid

abstract class Entity {
    var id: Uuid? = null
}

abstract class EntityDTO<T:Entity>(val id: Uuid?) {
    abstract fun toEntity(): T
}