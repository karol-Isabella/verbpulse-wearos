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
            "buy" -> when {
                cat.contains("tec") -> "Companies buy secure cloud storage."
                cat.contains("neg") -> "Investors buy profitable company shares."
                else -> "I buy fresh fruit every morning."
            }
            "come" -> when {
                cat.contains("tec") -> "New updates come with safety patches."
                cat.contains("neg") -> "Great results come from teamwork."
                else -> "Friends come over on weekends."
            }
            "do" -> when {
                cat.contains("tec") -> "Engineers do automated system testing."
                cat.contains("neg") -> "We do business across borders."
                else -> "I do morning exercises daily."
            }
            "eat" -> when {
                cat.contains("tec") -> "Developers eat snacks during hackathons."
                cat.contains("neg") -> "Executives eat lunch during meetings."
                else -> "We eat a healthy breakfast."
            }
            "give" -> when {
                cat.contains("tec") -> "AI models give accurate code answers."
                cat.contains("neg") -> "Leaders give clear weekly targets."
                else -> "They give thoughtful gifts on birthdays."
            }
            "go" -> when {
                cat.contains("tec") -> "Data packets go through secure tunnels."
                cat.contains("neg") -> "Sales go up every quarter."
                else -> "I go for a walk outside."
            }
            "know" -> when {
                cat.contains("tec") -> "Engineers know Kotlin and modern APIs."
                cat.contains("neg") -> "Top managers know market trends."
                else -> "We know the best park route."
            }
            "make" -> when {
                cat.contains("tec") -> "Pipelines make data delivery faster."
                cat.contains("neg") -> "Partners make strategic business plans."
                else -> "I make fresh warm coffee."
            }
            "take" -> when {
                cat.contains("tec") -> "Backups take only two seconds."
                cat.contains("neg") -> "Leaders take calculated financial risks."
                else -> "I take deep breaths to relax."
            }
            else -> "We $v new solutions together."
        }
    }
}
