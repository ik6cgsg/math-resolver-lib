package mathhelper.twf.defaultcontent.defaultrulepacks

class DefaultRulePacks {
    companion object {
        val defaultRulePacks = DefaultStandardMathRulePacks.get() +
                DefaultCombinatoricsRulePacks.get() +
                DefaultComplexRulePacks.get() +
                LogicRulePacks.get() +
                DefaultPhysicsRulePacks.get()

        val defaultRulePacksMap = defaultRulePacks.associateBy { it.code!! }

        fun map() = defaultRulePacksMap
    }
}
