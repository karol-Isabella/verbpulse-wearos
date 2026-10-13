package com.verbpulse.app.feature.learning.data

import com.google.ai.client.generativeai.GenerativeModel
import com.verbpulse.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio para la contextualización de verbos mediante Gemini AI (RF-004).
 * Incorpora un caché en memoria y un sistema de fallback estático ultrarrápido (< 1s)
 * para garantizar cumplimiento con RNF-002 y eficiencia energética en Wear OS.
 */
@Singleton
class GeminiRepository @Inject constructor() {

    private val cache = ConcurrentHashMap<String, String>()

    private val generativeModel: GenerativeModel? by lazy {
        val apiKey = BuildConfig.GEMINI_API_KEY.trim()
        if (apiKey.isBlank() || apiKey.contains("dummy", ignoreCase = true)) {
            Timber.w("Gemini API Key no configurada o en modo dummy. Se utilizará fallback estático.")
            null
        } else {
            try {
                GenerativeModel(
                    modelName = "gemini-1.5-flash",
                    apiKey = apiKey
                )
            } catch (e: Exception) {
                Timber.e(e, "Error al inicializar GenerativeModel de Gemini")
                null
            }
        }
    }

    /**
     * Obtiene una oración corta contextualizada en inglés para el verbo y categoría seleccionados.
     * Retorna inmediatamente (< 1s) mediante fallback estático si no hay red, si excede el tiempo límite o si la API key no está disponible.
     */
    suspend fun getContextualSentence(verb: String, category: String): String = withContext(Dispatchers.IO) {
        val cacheKey = "$verb:$category".lowercase()

        // 1. Verificar si ya fue generada previamente en caché en memoria
        cache[cacheKey]?.let { return@withContext it }

        val model = generativeModel
        if (model == null) {
            val fallback = getStaticFallback(verb, category)
            cache[cacheKey] = fallback
            return@withContext fallback
        }

        // 2. Intentar llamar a Gemini AI con un tiempo límite estricto de 1.5s
        try {
            val responseText = withTimeoutOrNull(1500L) {
                val prompt = "Generate a very short, simple, single English sentence (maximum 7 words) " +
                        "using the verb '$verb' in the context of '$category'. Return only the sentence."
                val response = model.generateContent(prompt)
                response.text?.trim()?.replace("\n", " ")
            }

            if (!responseText.isNullOrBlank()) {
                cache[cacheKey] = responseText
                return@withContext responseText
            }
        } catch (e: Exception) {
            Timber.e(e, "Fallo al generar oración con Gemini AI para '$verb' en '$category'")
        }

        // 3. Fallback estático garantizado (RNF-002: < 1s)
        val fallback = getStaticFallback(verb, category)
        cache[cacheKey] = fallback
        return@withContext fallback
    }

    /**
     * Fallback estático contextualizado por categoría para cada verbo irregular.
     */
    fun getStaticFallback(verb: String, category: String): String {
        val v = verb.lowercase().trim()
        val cat = category.lowercase().trim()

        return when (v) {
            "build" -> when {
                cat.contains("tec") -> "We build scalable cloud services."
                cat.contains("neg") -> "They build strong client partnerships."
                else -> "We build healthy daily habits."
            }
            "run" -> when {
                cat.contains("tec") -> "We run automated testing scripts."
                cat.contains("neg") -> "They run profitable enterprises."
                else -> "I run every morning."
            }
            "write" -> when {
                cat.contains("tec") -> "Engineers write clean Kotlin code."
                cat.contains("neg") -> "We write financial proposals."
                else -> "I write in my journal."
            }
            "break" -> when {
                cat.contains("tec") -> "Bugs break the continuous build."
                cat.contains("neg") -> "Do not break formal contracts."
                else -> "Take a break to rest."
            }
            "send" -> when {
                cat.contains("tec") -> "Servers send encrypted payloads."
                cat.contains("neg") -> "We send quarterly reports."
                else -> "I send messages to family."
            }
            "figure out" -> when {
                cat.contains("tec") -> "We figure out the root cause."
                cat.contains("neg") -> "Managers figure out the budget."
                else -> "I figure out the solution."
            }
            "set up" -> when {
                cat.contains("tec") -> "We set up cloud environments."
                cat.contains("neg") -> "We set up an executive meeting."
                else -> "I set up my morning desk."
            }
            "back up" -> when {
                cat.contains("tec") -> "Always back up server databases."
                cat.contains("neg") -> "Auditors back up their findings."
                else -> "I back up personal photos."
            }
            "log in" -> when {
                cat.contains("tec") -> "Users log in using biometric auth."
                cat.contains("neg") -> "Employees log in to company portals."
                else -> "I log in to check news."
            }
            "roll back" -> when {
                cat.contains("tec") -> "Engineers roll back buggy releases."
                cat.contains("neg") -> "We roll back unfavorable terms."
                else -> "We roll back old decisions."
            }
            "deal" -> "Leaders deal with complex negotiations."
            "spend" -> "Companies spend capital on expansion."
            "grow" -> "Strong startups grow rapidly."
            "lead" -> "Experienced mentors lead tech teams."
            "carry out" -> "Teams carry out corporate strategies."
            "close down" -> "Unprofitable divisions close down."
            "step down" -> "Founders step down as CEOs."
            "take over" -> "Conglomerates take over smaller firms."
            "branch out" -> "Firms branch out into new markets."
            "wake" -> "I wake up feeling refreshed."
            "sleep" -> "Adults sleep eight hours nightly."
            "choose" -> "We choose healthy nutritious food."
            "drink" -> "Always drink plenty of fresh water."
            "run out of" -> "We run out of fresh coffee."
            "get up" -> "I get up early every day."
            "look for" -> "I look for my house keys."
            "turn off" -> "Turn off lights when leaving."
            "give up" -> "Never give up on your goals."
            "buy" -> when {
                cat.contains("tec") -> "Companies buy secure cloud storage."
                cat.contains("neg") -> "Investors buy profitable company shares."
                else -> "I buy fresh fruit every morning."
            }
            "eat" -> when {
                cat.contains("tec") -> "Developers eat snacks during hackathons."
                cat.contains("neg") -> "Executives eat lunch during meetings."
                else -> "We eat a healthy breakfast."
            }
            else -> "We $v new solutions together."
        }
    }
}
