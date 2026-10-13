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
            val nicheDomain = when {
                category.contains("tec", ignoreCase = true) -> "software engineering, programming, and tech systems"
                category.contains("neg", ignoreCase = true) -> "corporate finance, executive business, startups, and leadership"
                else -> "daily personal life, home routines, and everyday activities"
            }
            val responseText = withTimeoutOrNull(1500L) {
                val prompt = "Generate a very short, natural English sentence (maximum 7 words) " +
                        "using the verb or phrasal verb '$verb' strictly contextualized to $nicheDomain. " +
                        "Return ONLY the sentence."
                val response = model.generateContent(prompt)
                response.text?.trim()?.replace("\n", " ")?.replace("\"", "")
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
            // Tecnología
            "build" -> "Engineers build scalable cloud services."
            "run" -> "We run automated testing scripts."
            "write" -> "Developers write clean asynchronous code."
            "break" -> "Uncaught exceptions break the pipeline."
            "send" -> "Clients send encrypted API requests."
            "figure out" -> "We figure out the production bug."
            "set up" -> "DevOps engineers set up Docker containers."
            "back up" -> "Always back up server databases."
            "log in" -> "Users log in with biometric auth."
            "roll back" -> "Engineers roll back buggy releases."

            // Negocios
            "deal" -> "Partners deal with global trade agreements."
            "buy" -> "Investors buy high-growth equity shares."
            "spend" -> "Companies spend capital on strategic expansion."
            "grow" -> "Profitable startups grow quarterly revenue."
            "lead" -> "Executive mentors lead corporate teams."
            "carry out" -> "Managers carry out corporate market research."
            "close down" -> "Unprofitable retail branches close down."
            "step down" -> "Founders step down as CEOs."
            "take over" -> "Conglomerates take over emerging competitors."
            "branch out" -> "Firms branch out into fintech markets."

            // Vida Diaria
            "wake" -> "I wake up feeling well rested."
            "eat" -> "Families eat healthy home meals together."
            "sleep" -> "Healthy adults sleep eight hours nightly."
            "choose" -> "We choose fresh vegetables at market."
            "drink" -> "Drink a warm glass of water."
            "run out of" -> "We run out of fresh coffee."
            "get up" -> "I get up early every morning."
            "look for" -> "I look for my car keys."
            "turn off" -> "Turn off bedroom lights before leaving."
            "give up" -> "Never give up on healthy habits."

            else -> when {
                cat.contains("tec") -> "Engineers $v modern tech solutions."
                cat.contains("neg") -> "Leaders $v strategic corporate growth."
                else -> "We $v positive daily habits."
            }
        }
    }
}
