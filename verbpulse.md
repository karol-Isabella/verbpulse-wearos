# VerbPulse

**VerbPulse** es una aplicación para relojes inteligentes con Wear OS diseñada para facilitar el aprendizaje continuo de inglés mediante microlecciones distribuidas a lo largo del día.

Al comenzar la jornada, el usuario configura su meta diaria (categoría, cantidad de vocabulario y frecuencia de recordatorios). A través de suaves vibraciones periódicas, el reloj presenta tarjetas rápidas con verbos en sus tres formas (*Present, Past, Past Participle*) o *Phrasal Verbs* junto con su traducción y ejemplos contextualizados mediante inteligencia artificial.

Al finalizar el día, el usuario realiza un mini cuestionario interactivo basado exclusivamente en el contenido repasado durante la jornada.

---

# Requerimientos Funcionales

## RF-001: Configuración Matutina Diaria
El sistema debe permitir al usuario configurar su jornada al inicio del día seleccionando:

- Categoría de estudio (Tecnología, Negocios o Vida Diaria).
- Cantidad de verbos o *Phrasal Verbs* a aprender durante el día.
- Frecuencia de los recordatorios (por ejemplo: cada 1, 2 o 3 horas).

## RF-002: Notificaciones por Vibración
El reloj debe enviar recordatorios hápticos (vibración) respetando el intervalo de tiempo seleccionado por el usuario.

## RF-003: Visualización Gramatical de Vocabulario
El sistema debe presentar el vocabulario según su tipo:

### Verbos
Mostrar las tres formas conjugadas:

- *Present*
- *Past*
- *Past Participle*

Además de su traducción al español.

### Phrasal Verbs
Mostrar la combinación de palabras junto con su traducción directa al español.

## RF-004: Contextualización por Inteligencia Artificial
La inteligencia artificial debe generar una oración corta de ejemplo utilizando el verbo o *Phrasal Verb* estudiado dentro del contexto de la categoría seleccionada.

## RF-005: Evaluación Nocturna (Mini Quiz del Día)
Al finalizar la jornada, el sistema debe habilitar un mini cuestionario acumulativo con preguntas de tres opciones de respuesta, evaluando exclusivamente los verbos y *Phrasal Verbs* recibidos en las notificaciones del día.

## RF-006: Cálculo de Rendimiento
El sistema debe calcular y mostrar el porcentaje de respuestas correctas al finalizar la prueba nocturna.

## RF-007: Registro de Racha Diaria
El sistema debe mantener y actualizar una racha de días consecutivos en los que el usuario complete su meta y prueba diaria.

## RF-008: Histórico y Progreso Acumulado
El usuario debe poder consultar estadísticas de avance, incluyendo:

- Total de palabras aprendidas.
- Días en racha.
- Promedio de aciertos.

---

# Requerimientos No Funcionales

## RNF-001: Adaptabilidad de Pantalla
La interfaz debe adaptarse automáticamente a pantallas redondas y cuadradas de Wear OS.

## RNF-002: Carga Ultra Rápida
Cada tarjeta de aprendizaje o notificación interactiva debe abrirse en menos de un segundo.

## RNF-003: Eficiencia Energética
La aplicación debe mantener un bajo consumo de batería en segundo plano durante el monitoreo de los intervalos de tiempo y vibración.

## RNF-004: Soporte Dual de Tema (Modo Claro / Modo Oscuro)
La interfaz debe ofrecer la opción de alternar entre:

- **Modo Oscuro:** alto contraste y fondo negro puro para ahorro de batería OLED.
- **Modo Claro:** alta visibilidad para entornos con luz solar directa.

## RNF-005: Interacción Mínima (1-2 Toques)
Toda la navegación y respuesta a notificaciones o preguntas debe realizarse con un máximo de uno o dos toques por interacción, minimizando la interrupción de las actividades del usuario.