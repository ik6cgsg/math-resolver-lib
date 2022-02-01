package mathhelper.twf.platformdependent

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Required

class JsonParser {
    companion object {
        fun parseMap(json: String): Map<String, Any> {
            val jsonObj = Klaxon().parseJsonObject(json.reader())
            return parseMapRecursive(jsonObj)
        }

        fun parseMapRecursive(jsonObj: JsonObject): Map<String, Any> {
            val res = mutableMapOf<String, Any>()
            for ((key, valueObj) in jsonObj) {
                if (valueObj != null) {
                    val value = when (valueObj) {
                        is JsonObject -> parseMapRecursive(valueObj)
                        is JsonArray<*> -> {
                            valueObj.map {
                                when (it) {
                                    is JsonObject -> parseMapRecursive(it)
                                    else -> it
                                }
                            }
                        }
                        else -> valueObj
                    }
                    res.put(key, value)
                }
            }
            return res
        }
    }
}