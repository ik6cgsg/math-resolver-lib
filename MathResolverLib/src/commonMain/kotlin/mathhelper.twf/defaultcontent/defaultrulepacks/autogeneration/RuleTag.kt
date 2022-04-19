package mathhelper.twf.defaultcontent.defaultrulepacks.autogeneration

enum class RuleTag(val code: String, val readyForUseInProduction: Boolean = true) {

    // common math
    BASIC_MATH("Базовые правила математики"),

    // trigonometry
    TRIGONOMETRY_BASIC("Базовые соотношения в тригонометрии"),
    TRIGONOMETRY_STANDARD_ANGLES("Стандартные углы тригонометрической таблицы"),
    TRIGONOMETRY_PERIODIC("Правила периодов"),
    TRIGONOMETRY_SHIFTING("Формулы приведения"),
    TRIGONOMETRY_SUM_AND_DIFF_OF_ANGLES("Сумма и разность углов"),
    TRIGONOMETRY_DOUBLE_ANGLES("Формулы с двойными углами"),
    TRIGONOMETRY_TRIPLE_ANGLES("Формулы с тройными углами"),
    TRIGONOMETRY_MULTI_ANGLES("Формулы с углами кратности более 3"),
    TRIGONOMETRY_HALF_ANGLES("Формулы половинных углов"),
    TRIGONOMETRY_POWER_REDUCING("Формулы понижения степени", false),
    TRIGONOMETRY_SUM_AND_DIFF_OF_FUNCTIONS("Сумма и разность тригонометрических функций"),
    TRIGONOMETRY_PROD_OF_FUNCTIONS("Произведение тригонометрических функций"),
    TRIGONOMETRY_INVERSE_FUNCTIONS("Аркфункции", false),
    TRIGONOMETRY_AUXILIARY_ARGUMENT("Формула вспомогательного аргумента", false),
    TRIGONOMETRY_WEIERSTRASS_SUBSTITUTION("Универсальная тригонометрическая подстановка", false),
    TRIGONOMETRY_EULER_FORMULAS("Представление тригонометрических функций в комплексной форме", false),
    TRIGONOMETRY_HYPERBOLIC_FUNCTIONS("Гиперболические функции", false)
}
