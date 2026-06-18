package model

class Semestre(private var _ano: Int, private var _semestre: Int): Comparable<Semestre> {
    val ano: Int get() = _ano
    val semestre: Int get() = _semestre

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

//    operator fun rangeTo(other: Semestre): ClosedRange<Semestre> {
//        return object: ClosedRange<Semestre> {
//            override val start: Semestre = this@Semestre
//            override val endInclusive: Semestre = other
//        }
//    }

    operator fun rangeTo(other: Semestre): Iterator<Semestre> {
        return object: Iterator<Semestre> {
            val current = this@Semestre - 1
            override fun next() = current.inc()
            override fun hasNext() = current < other
        }
    }

    override fun compareTo(other: Semestre) =
        if (_ano == other._ano) _semestre - other._semestre else _ano - other._ano

    override fun toString() = "$_ano.$_semestre"
}