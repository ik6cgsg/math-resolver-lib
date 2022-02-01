package mathhelper.utility.math_resolver_lib

class Logger {
    companion object {
        fun e(tag: String, message: Any) {
            println("MathResolver error: [$tag] $message")
        }

        fun d(tag: String, message: Any) {
            println("MathResolver debug: [$tag] $message")
        }
    }
}