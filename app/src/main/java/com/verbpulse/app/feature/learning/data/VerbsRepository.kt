package com.verbpulse.app.feature.learning.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VerbsRepository @Inject constructor() {
    private val verbs = listOf(
        IrregularVerb("build", "built", "built", "construir"),
        IrregularVerb("buy", "bought", "bought", "comprar"),
        IrregularVerb("come", "came", "come", "venir"),
        IrregularVerb("do", "did", "done", "hacer"),
        IrregularVerb("eat", "ate", "eaten", "comer"),
        IrregularVerb("give", "gave", "given", "dar"),
        IrregularVerb("go", "went", "gone", "ir"),
        IrregularVerb("know", "knew", "known", "saber"),
        IrregularVerb("make", "made", "made", "hacer"),
        IrregularVerb("take", "took", "taken", "tomar")
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
}
