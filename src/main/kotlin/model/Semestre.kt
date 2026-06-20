package model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

val INICIO_PANDEMIA = Semestre(2020, 1)
val FIM_PANDEMIA = Semestre(2022, 2)

class Semestre(private var _ano: Int, private var _semestre: Int): Comparable<Semestre> {
    val ano: Int get() = _ano
    val semestre: Int get() = _semestre

    companion object {
        val ATUAL by lazy {
            val dt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            Semestre(dt.year, if (dt.month.ordinal <= 6) 1 else 2)
        }
    }

    operator fun inc(): Semestre {
        _semestre++
        if (_semestre > 2) {
            _semestre = 1
            _ano++
        }
        return this
    }

    operator fun dec(): Semestre {
        _semestre--
        if (_semestre < 1) {
            _semestre = 2
            _ano--
        }
        return this
    }

    operator fun plusAssign(n: Int) {
        repeat(n) {
            this.inc()
        }
    }

    operator fun minusAssign(n: Int) {
        repeat(n) {
            this.dec()
        }
    }

    infix operator fun plus(n: Int): Semestre {
        val s = Semestre(_ano, _semestre)
        s += n
        return s
    }

    infix operator fun minus(n: Int): Semestre {
        val s = Semestre(_ano, _semestre)
        s -= n
        return s
    }

    infix operator fun minus(other: Semestre): Int {
        val n = (_ano - other._ano + 1) * 2

        return n + (if (_semestre > other._semestre) 0 else if (_semestre == other._semestre) -1 else -2)
    }

    operator fun rangeTo(other: Semestre) = SemestreRange(Semestre(ano, semestre), Semestre(other.ano, other.semestre))
//    : ClosedRange<Semestre> {
//        return object: ClosedRange<Semestre> {
//            override val start: Semestre = this@Semestre
//            override val endInclusive: Semestre = other
//        }
//    }

    override fun compareTo(other: Semestre) =
        if (_ano == other._ano) _semestre - other._semestre else _ano - other._ano

    override fun toString() = "$_ano.$_semestre"
}

class SemestreRange(override val start: Semestre, override val endInclusive: Semestre) : ClosedRange<Semestre> {

    operator fun iterator(): Iterator<Semestre> {
        return object: Iterator<Semestre> {
            val current = start
            override fun next(): Semestre {
                val next = Semestre(current.ano, current.semestre)
                current.inc()
                return next;
            }
            override fun hasNext() = current <= endInclusive
        }
    }
}