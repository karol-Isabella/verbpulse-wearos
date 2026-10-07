package com.verbpulse.app.feature.learning.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VerbsRepository @Inject constructor() {
    private val verbs = listOf(
        IrregularVerb("build", "built", "built", "construir", "Tecnología"),
        IrregularVerb("buy", "bought", "bought", "comprar", "Tecnología"),
        IrregularVerb("come", "came", "come", "venir", "Negocios"),
        IrregularVerb("do", "did", "done", "hacer", "Negocios"),
        IrregularVerb("eat", "ate", "eaten", "comer", "Vida Diaria"),
        IrregularVerb("give", "gave", "given", "dar", "Negocios"),
        IrregularVerb("go", "went", "gone", "ir", "Negocios"),
        IrregularVerb("know", "knew", "known", "saber", "Vida Diaria"),
        IrregularVerb("make", "made", "made", "hacer", "Tecnología"),
        IrregularVerb("take", "took", "taken", "tomar", "Tecnología")
    )

    fun getAllVerbs(): List<IrregularVerb> = verbs

    fun getVerbAt(index: Int): IrregularVerb? =
        if (index in verbs.indices) verbs[index] else null

    fun getRandomVerb(): IrregularVerb = verbs.random()

    fun getRandomWrongAnswers(correctAnswer: String, count: Int = 2): List<String> {
        val wrongAnswers = mutableListOf<String>()
        val shuffled = verbs.shuffled()
        for (verb in shuffled) {
            if (wrongAnswers.size >= count) break
            if (verb.past != correctAnswer && verb.participle != correctAnswer) {
                wrongAnswers.add(verb.past)
            }
        }
        return wrongAnswers.take(count)
    }

    fun getVerbsByCategory(category: String): List<IrregularVerb> {
        val filtered = verbs.filter { it.category == category }
        return if (filtered.isEmpty()) verbs else filtered
    }
}
