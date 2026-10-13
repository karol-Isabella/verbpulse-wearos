package com.verbpulse.app.feature.learning.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VerbsRepository @Inject constructor() {
    private val verbs = listOf(
        // Tecnología
        IrregularVerb("build", "built", "built", "construir", "Tecnología", isPhrasal = false),
        IrregularVerb("run", "ran", "run", "ejecutar", "Tecnología", isPhrasal = false),
        IrregularVerb("write", "wrote", "written", "escribir código", "Tecnología", isPhrasal = false),
        IrregularVerb("break", "broke", "broken", "romper / fallar", "Tecnología", isPhrasal = false),
        IrregularVerb("send", "sent", "sent", "enviar", "Tecnología", isPhrasal = false),
        IrregularVerb("figure out", "figured out", "figured out", "descifrar, resolver", "Tecnología", isPhrasal = true),
        IrregularVerb("set up", "set up", "set up", "configurar, preparar", "Tecnología", isPhrasal = true),
        IrregularVerb("back up", "backed up", "backed up", "respaldar datos", "Tecnología", isPhrasal = true),
        IrregularVerb("log in", "logged in", "logged in", "iniciar sesión", "Tecnología", isPhrasal = true),
        IrregularVerb("roll back", "rolled back", "rolled back", "revertir versión", "Tecnología", isPhrasal = true),

        // Negocios
        IrregularVerb("deal", "dealt", "dealt", "negociar / acordar", "Negocios", isPhrasal = false),
        IrregularVerb("buy", "bought", "bought", "comprar", "Negocios", isPhrasal = false),
        IrregularVerb("spend", "spent", "spent", "invertir / gastar", "Negocios", isPhrasal = false),
        IrregularVerb("grow", "grew", "grown", "crecer / expandir", "Negocios", isPhrasal = false),
        IrregularVerb("lead", "led", "led", "liderar", "Negocios", isPhrasal = false),
        IrregularVerb("carry out", "carried out", "carried out", "llevar a cabo", "Negocios", isPhrasal = true),
        IrregularVerb("close down", "closed down", "closed down", "cerrar operaciones", "Negocios", isPhrasal = true),
        IrregularVerb("step down", "stepped down", "stepped down", "renunciar al cargo", "Negocios", isPhrasal = true),
        IrregularVerb("take over", "took over", "took over", "asumir control", "Negocios", isPhrasal = true),
        IrregularVerb("branch out", "branched out", "branched out", "diversificarse", "Negocios", isPhrasal = true),

        // Vida Diaria
        IrregularVerb("wake", "woke", "woken", "despertar", "Vida Diaria", isPhrasal = false),
        IrregularVerb("eat", "ate", "eaten", "comer", "Vida Diaria", isPhrasal = false),
        IrregularVerb("sleep", "slept", "slept", "dormir", "Vida Diaria", isPhrasal = false),
        IrregularVerb("choose", "chose", "chosen", "elegir", "Vida Diaria", isPhrasal = false),
        IrregularVerb("drink", "drank", "drunk", "beber", "Vida Diaria", isPhrasal = false),
        IrregularVerb("run out of", "ran out of", "run out of", "quedarse sin", "Vida Diaria", isPhrasal = true),
        IrregularVerb("get up", "got up", "got up", "levantarse", "Vida Diaria", isPhrasal = true),
        IrregularVerb("look for", "looked for", "looked for", "buscar", "Vida Diaria", isPhrasal = true),
        IrregularVerb("turn off", "turned off", "turned off", "apagar", "Vida Diaria", isPhrasal = true),
        IrregularVerb("give up", "gave up", "given up", "rendirse", "Vida Diaria", isPhrasal = true)
    )

    fun getAllVerbs(): List<IrregularVerb> = verbs

    fun getVerbAt(index: Int): IrregularVerb? =
        if (index in verbs.indices) verbs[index] else null

    fun getRandomVerb(): IrregularVerb = verbs.random()

    fun getRandomWrongAnswers(correctAnswer: String, category: String = "Tecnología", count: Int = 2): List<String> {
        val wrongAnswers = mutableListOf<String>()
        val categoryVerbs = getVerbsByCategory(category).shuffled()
        for (verb in categoryVerbs) {
            if (wrongAnswers.size >= count) break
            if (verb.past != correctAnswer && verb.participle != correctAnswer && !wrongAnswers.contains(verb.past)) {
                wrongAnswers.add(verb.past)
            }
        }
        if (wrongAnswers.size < count) {
            for (verb in verbs.shuffled()) {
                if (wrongAnswers.size >= count) break
                if (verb.past != correctAnswer && verb.participle != correctAnswer && !wrongAnswers.contains(verb.past)) {
                    wrongAnswers.add(verb.past)
                }
            }
        }
        return wrongAnswers.take(count)
    }

    fun getRandomWrongMeanings(correctMeaning: String, category: String = "Tecnología", count: Int = 2): List<String> {
        val wrongMeanings = mutableListOf<String>()
        val candidatePool = when {
            category.contains("tec", ignoreCase = true) -> listOf(
                "Configurar", "Respaldar", "Iniciar sesión", "Revertir", "Ejecutar", "Compilar", "Depurar", "Romper", "Enviar"
            )
            category.contains("neg", ignoreCase = true) -> listOf(
                "Llevar a cabo", "Cerrar operaciones", "Renunciar al cargo", "Asumir control", "Diversificarse", "Invertir", "Liderar", "Negociar"
            )
            else -> listOf(
                "Quedarse sin", "Levantarse", "Buscar", "Apagar", "Rendirse", "Despertar", "Elegir", "Descansar"
            )
        }.shuffled()

        for (meaning in candidatePool) {
            if (wrongMeanings.size >= count) break
            if (!meaning.equals(correctMeaning, ignoreCase = true) && !correctMeaning.contains(meaning, ignoreCase = true)) {
                wrongMeanings.add(meaning)
            }
        }
        return wrongMeanings.take(count)
    }

    fun getVerbsByCategory(category: String): List<IrregularVerb> {
        val targetCategory = when {
            category.contains("tec", ignoreCase = true) -> "Tecnología"
            category.contains("neg", ignoreCase = true) -> "Negocios"
            category.contains("vida", ignoreCase = true) || category.contains("diaria", ignoreCase = true) || category.contains("daily", ignoreCase = true) -> "Vida Diaria"
            else -> "Tecnología"
        }
        return verbs.filter { it.category == targetCategory }
    }
}
