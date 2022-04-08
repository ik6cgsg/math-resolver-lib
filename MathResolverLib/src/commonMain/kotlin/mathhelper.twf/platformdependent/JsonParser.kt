package mathhelper.twf.platformdependent

import kotlinx.serialization.json.*

class JsonParser {
    companion object {
        fun parseMap(json: String): Map<String, Any> {
            val jsonObj = Json.parseToJsonElement(json).jsonObject
            return parseMapRecursive(jsonObj)
        }

        fun parseMapRecursive(jsonObj: JsonObject): Map<String, Any> {
            val res = mutableMapOf<String, Any>()
            for ((key, valueObj) in jsonObj) {
                val value = when (valueObj) {
                    is JsonObject -> parseMapRecursive(valueObj)
                    is JsonArray -> {
                        valueObj.map {
                            when (it) {
                                is JsonObject -> parseMapRecursive(it)
                                else -> it
                            }
                        }
                    }
                    else -> valueObj
                }
                res[key] = value
            }
            return res
        }
    }
}