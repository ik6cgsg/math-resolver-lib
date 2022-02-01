package mathhelper.utility.math_resolver_lib

data class MatifyMultiplierSpan(val multiplier: Float = 1f)

data class SpanInfo(val span: Any, val lt: Point, val rb: Point) {
    fun getStartsEnds(len: Int, corr: Int = 0): ArrayList<Pair<Int, Int>> {
        val startEnds = ArrayList<Pair<Int, Int>>()
        var y = lt.y
        while (y <= rb.y) {
            val s = (y + corr) * len + lt.x
            val e = (y + corr) * len + rb.x + 1
            startEnds.add(Pair(s, e))
            y++
        }
        return startEnds
    }
}