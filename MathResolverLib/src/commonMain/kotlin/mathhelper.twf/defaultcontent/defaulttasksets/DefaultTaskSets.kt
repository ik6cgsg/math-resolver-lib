package mathhelper.twf.defaultcontent.defaulttasksets

class DefaultTaskSets {
    companion object {
        val defaultTaskSets = TriginometryTaskSets.defaultTrigonometryTaskSets +
                LogicTaskSets.defaultLogicTaskSets +
                CombinatoricTaskSets.defaultCombinatoricsTaskSets

        val defaultTaskSetsMap = defaultTaskSets.associateBy { it.code!! }

        fun map() = defaultTaskSetsMap
    }
}