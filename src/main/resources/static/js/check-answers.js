$(document).ready(function() {
    // Cuando se hace clic en el botón 'Comprobar Respuestas'
    $('#check-answers-btn').on('click', function() {

        let totalQuestions = 0;
        let correctQuestionsCount = 0;

        // Limpiamos estilos de comprobaciones anteriores (CORRECTO)
        $('.test-answer-option label').removeClass('incorrect-answer');

        // Iteramos sobre cada contenedor de pregunta (CORRECTO)
        $('.test-container').each(function() {
            totalQuestions++;
            // Asumimos que la pregunta es correcta hasta que se demuestre lo contrario
            let isQuestionCorrect = true;

            // Iteramos sobre cada checkbox de respuesta dentro de esta pregunta
            $(this).find('input[type="checkbox"]').each(function() {
                const isChecked = $(this).is(':checked');

                // ----------------------------------------------------------------
                // 🟢 CORRECCIÓN 1: Lectura fiable de data-correct
                // Convertimos a string para asegurar la comparación.
                // ----------------------------------------------------------------
                const dataValue = $(this).data('correct');
                const isCorrectAnswer = String(dataValue) === 'true';
                // ----------------------------------------------------------------

                // Comprobamos las dos condiciones de error
                // 1. Era correcta, pero no se marcó (FALSO NEGATIVO)
                const shouldBeChecked = isCorrectAnswer && !isChecked;
                // 2. Era incorrecta, pero se marcó (FALSO POSITIVO)
                const shouldNotBeChecked = !isCorrectAnswer && isChecked;

                if (shouldBeChecked || shouldNotBeChecked) {
                    // 🟢 CORRECCIÓN 2: Actualizar el estado de la pregunta
                    isQuestionCorrect = false;

                    // Marcamos la etiqueta correspondiente en rojo (CORRECTO)
                    $(this).next('label').addClass('incorrect-answer');
                }
            });

            // Si, después de revisar todas las respuestas, la pregunta sigue siendo correcta...
            if (isQuestionCorrect) {
                correctQuestionsCount++;
            }
        });

        // Mostramos el resultado final en el div de resumen
        const resultText = `Aciertos: ${correctQuestionsCount} / ${totalQuestions}`;
        $('#results-summary').html(`<strong>${resultText}</strong>`).show();
    });
});