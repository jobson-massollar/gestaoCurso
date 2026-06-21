package model

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

val INICIO_PANDEMIA = Periodo(2020, 1)
val FIM_PANDEMIA = Periodo(2022, 2)

class Periodo(private var _ano: Int, private var _semestre: Int): Comparable<Periodo> {
    val ano: Int get() = _ano
    val semestre: Int get() = _semestre

    companion object {
        val ATUAL by lazy {
            val dt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            Periodo(dt.year, if (dt.month.ordinal <= 6) 1 else 2)
        }
    }

    operator fun inc(): Periodo {
        _semestre++
        if (_semestre > 2) {
            _semestre = 1
            _ano++
        }
        return this
    }

    operator fun dec(): Periodo {
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

    infix operator fun plus(n: Int): Periodo {
        val s = Periodo(_ano, _semestre)
        s += n
        return s
    }

    infix operator fun minus(n: Int): Periodo {
        val s = Periodo(_ano, _semestre)
        s -= n
        return s
    }

    infix operator fun minus(other: Periodo): Int {
        val n = (_ano - other._ano + 1) * 2

        return n + (if (_semestre > other._semestre) 0 else if (_semestre == other._semestre) -1 else -2)
    }

    operator fun rangeTo(other: Periodo) = PeriodoRange(Periodo(ano, semestre), Periodo(other.ano, other.semestre))
//    : ClosedRange<Semestre> {
//        return object: ClosedRange<Semestre> {
//            override val start: Semestre = this@Semestre
//            override val endInclusive: Semestre = other
//        }
//    }

    override fun compareTo(other: Periodo) =
        if (_ano == other._ano) _semestre - other._semestre else _ano - other._ano

    override fun toString() = "$_ano.$_semestre"
}

class PeriodoRange(override val start: Periodo, override val endInclusive: Periodo) : ClosedRange<Periodo> {

    operator fun iterator(): Iterator<Periodo> {
        return object: Iterator<Periodo> {
            val current = start
            override fun next(): Periodo {
                val next = Periodo(current.ano, current.semestre)
                current.inc()
                return next;
            }
            override fun hasNext() = current <= endInclusive
        }
    }
}