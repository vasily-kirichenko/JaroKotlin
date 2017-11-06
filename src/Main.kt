import java.lang.Math.abs
import kotlin.system.measureTimeMillis

fun existsInWin(mChar: Char, s: String, offset: Int, rad: Int) : Boolean {
    val startAt = maxOf(offset - rad, 0).toLong()
    val length = (minOf(offset + rad, s.length) - startAt).toLong()
    val mChar = mChar.toInt()
    return s.chars()
            .skip(startAt)
            .limit(length)
            .anyMatch { it == mChar }
}

fun commonChars(chars1: String, chars2: String, match_radius: Int) =
    chars1.toCharArray().filterIndexed { i, c -> existsInWin(c, chars2, i, match_radius) }

fun jaro(s1: String, s2: String) : Double {
    val matchRadius = run {
        val minLen = minOf(s1.length, s2.length)
        minLen / 2 + minLen % 2
    }

    val c1 = commonChars(s1, s2, matchRadius)
    val c2 = commonChars(s2, s1, matchRadius)
    val c1length = c1.count().toDouble()
    val c2length = c2.count().toDouble()

    val transpositions = run {
        val mismatches = c1.zip(c2).filter { (i, j) -> i != j }.count()
        (mismatches + abs(c1length - c2length)) / 2.0
    }

    val tLength = maxOf(c1length, c2length)
    val result = (c1length / s1.length.toDouble() + c2length / s2.length.toDouble() + (tLength - transpositions) / tLength) / 3.0
    return if (result.isNaN()) 0.0 else result
}

fun main(args: Array<String>) {
    val s1 = "Environment"
    val s2 = "Envronment"
    println("$s1, $s2 => ${jaro(s1, s2)}")

    val millis = measureTimeMillis {

        for (i in 0..10_000_000) {
            jaro(s1, s2)
        }
    }

    println("Elapsed $millis")
}