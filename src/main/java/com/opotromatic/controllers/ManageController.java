package com.opotromatic.controllers;

import com.opotromatic.DTO.AnswerUpdateDTO;
import com.opotromatic.DTO.AnswerListUpdateDTO;
import com.opotromatic.DTO.UpdateNameDescriptionDTO;
import com.opotromatic.entities.*;
import com.opotromatic.repositories.*;
import com.opotromatic.services.FinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/api/manage")
public class ManageController {

    private final CategoryRepository categoryRepository;
    private final ThemeRepository themeRepository;
    private final BlockRepository blockRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final FinderService finderService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ManageController(
            CategoryRepository categoryRepository,
            ThemeRepository themeRepository,
            BlockRepository blockRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository,
            FinderService finderService,
            JdbcTemplate jdbcTemplate) {
        this.categoryRepository = categoryRepository;
        this.blockRepository = blockRepository;
        this.themeRepository = themeRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.finderService = finderService;
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- MÉTODOS DE ACCIÓN (POST) MANTENIDOS ---

    /**
     * Actualiza masivamente el estado 'correct' de las respuestas de una pregunta.
     */
    @PostMapping("/answers/update-bulk")
    @Transactional
    public String updateAnswersBulk(@ModelAttribute("answersForm") AnswerListUpdateDTO answersForm,
                                    RedirectAttributes redirectAttributes) {

        Long questionId = null;

        if (answersForm.getAnswers() != null && !answersForm.getAnswers().isEmpty()) {

            // 1. Encontrar el ID de la pregunta (mejor hacerlo aquí para evitar N+1 en el bucle)
            Long firstAnswerId = answersForm.getAnswers().get(0).getId();

            // Usamos Optional para manejar el caso de que la respuesta no exista.
            Optional<Answer> firstAnswerOptional = answerRepository.findById(firstAnswerId);

            if (firstAnswerOptional.isPresent()) {
                questionId = firstAnswerOptional.get().getQuestion().getId();

                // 2. Iteramos y actualizamos las respuestas.
                for (AnswerUpdateDTO update : answersForm.getAnswers()) {
                    answerRepository.findById(update.getId()).ifPresent(answer -> {
                        answer.setCorrect(update.isCorrect());
                        answerRepository.save(answer);
                    });
                }
            }
        }

        if (questionId != null) {
            redirectAttributes.addFlashAttribute("message", "Configuración de respuestas actualizada correctamente.");
            return "redirect:/api/manage/question/" + questionId;
        } else {
            // Se asume que /api/manage/config es una página de fallback segura
            redirectAttributes.addFlashAttribute("error", "No se encontraron respuestas válidas para actualizar.");
            return "redirect:/api/manage/config";
        }
    }

    // DELETE METHODS

    /**
     * Elimina una categoría.
     */
    @PostMapping("/category/delete/{id}")
    @Transactional
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        return categoryRepository.findById(id).map(category -> {
            String categoryName = category.getName(); // Guardamos el nombre antes de la eliminación
            categoryRepository.delete(category);
            redirectAttributes.addFlashAttribute("message", "Categoría '" + categoryName + "' eliminada correctamente.");
            return "redirect:/api/manage/config";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Categoría no encontrada.");
            return "redirect:/api/manage/config";
        });
    }

    /**
     * Elimina un bloque.
     */

    @PostMapping("/block/delete/{id}")
    @Transactional
    public String deleteBlock(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return blockRepository.findById(id).map(block -> {
            // 1. Guardar la ID de la categoría padre para la redirección
            long parentCategoryId = block.getCategory().getId();

            // 2. Eliminar el bloque
            // Spring Data JPA maneja las cascadas (si están configuradas) para eliminar temas, preguntas, etc.
            blockRepository.delete(block);

            // 3. Añadir mensaje de éxito
            redirectAttributes.addFlashAttribute("message", "Bloque '" + block.getName() + "' eliminado correctamente.");

            // 4. Redirigir a la vista de la categoría padre
            return "redirect:/api/manage/category/" + parentCategoryId;
        }).orElseGet(() -> {
            // Bloque no encontrado
            redirectAttributes.addFlashAttribute("error", "Bloque no encontrado.");
            // Redirigir al inicio de configuración si falla
            return "redirect:/api/manage/config";
        });
    }

    /**
     * Elimina un tema.
     */

    @PostMapping("/theme/delete/{id}")
    @Transactional
    public String deleteTheme(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return themeRepository.findById(id).map(theme -> {
            // 1. Guardar la ID del bloque padre para la redirección
            Long parentBlockId = theme.getBlock().getId();

            // 2. Eliminar el tema
            // Spring Data JPA maneja las cascadas (si están configuradas) para eliminar preguntas, respuestas, etc.
            themeRepository.delete(theme);

            // 3. Añadir mensaje de éxito
            redirectAttributes.addFlashAttribute("message", "Tema '" + theme.getName() + "' eliminado correctamente.");

            // 4. Redirigir a la vista del bloque padre
            return "redirect:/api/manage/block/" + parentBlockId;
        }).orElseGet(() -> {
            // Tema no encontrado
            redirectAttributes.addFlashAttribute("error", "Tema no encontrado.");
            // Redirigir al inicio de configuración si falla
            return "redirect:/api/manage/config";
        });
    }

    /**
     * Elimina una pregunta.
     */

    @PostMapping("/question/delete/{id}")
    @Transactional
    public String deleteQuestion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return questionRepository.findById(id).map(question -> {
            // 1. Guardar la ID del tema padre para la redirección
            long parentThemeId = question.getTheme().getId();

            // 2. Eliminar la pregunta
            // Spring Data JPA maneja las cascadas (si están configuradas) para eliminar respuestas.
            questionRepository.delete(question);

            // 3. Añadir mensaje de éxito
            redirectAttributes.addFlashAttribute("message", "Pregunta eliminada correctamente.");

            // 4. Redirigir a la vista del tema padre
            return "redirect:/api/manage/theme/" + parentThemeId;
        }).orElseGet(() -> {
            // Pregunta no encontrada
            redirectAttributes.addFlashAttribute("error", "Pregunta no encontrada.");
            // Redirigir al inicio de configuración si falla
            return "redirect:/api/manage/config";
        });
    }

    /**
     * Elimina una respuesta.
     */
    @PostMapping("/answer/delete/{id}")
    @Transactional
    public String deleteAnswer(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        return answerRepository.findById(id).map(answer -> {
            long questionId = answer.getQuestion().getId();
            answerRepository.delete(answer);
            redirectAttributes.addFlashAttribute("message", "Respuesta eliminada correctamente.");
            return "redirect:/api/manage/question/" + questionId;
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Respuesta no encontrada.");
            return "redirect:/api/manage/config";
        });
    }

    /**
     * Crea una nueva categoría.
     */
    @PostMapping("/category/create")
    public String createCategory(@ModelAttribute Category category) {
        if (categoryRepository.findByName(category.getName()).isEmpty()) {
            categoryRepository.save(category);
        }
        return "redirect:/api/manage/config";
    }

    /**
     * Crea un nuevo bloque dentro de una categoría.
     */
    @PostMapping("/block/create")
    public String createBlock(@RequestParam String name, @RequestParam String description, @RequestParam Long categoryId) {
        Category category = finderService.findCategoryById(categoryId);
        if (blockRepository.findByNameAndCategory(name, category).isEmpty()) {
            blockRepository.save(new Block(name, category, description));
        }
        return "redirect:/api/manage/category/" + categoryId;
    }

    /**
     * Crea un nuevo tema dentro de un bloque.
     */
    @PostMapping("/theme/create")
    public String createTheme(@RequestParam String name, @RequestParam String description, @RequestParam Long blockId) {
        Block block = finderService.findBlockById(blockId);
        if (themeRepository.findByNameAndBlock(name, block).isEmpty()) {
            themeRepository.save(new Theme(name, block, description));
        }
        return "redirect:/api/manage/block/" + blockId;
    }

    /**
     * Crea una nueva pregunta dentro de un tema.
     */
    @PostMapping("/question/create")
    public String createQuestion(@RequestParam String name, @RequestParam Long themeId) {
        Theme theme = finderService.findThemeById(themeId);

        if (questionRepository.findByNameAndTheme(name, theme).isEmpty()) {
            questionRepository.save(new Question(name, theme));
        }
        return "redirect:/api/manage/theme/" + themeId;
    }

    /**
     * Crea una nueva respuesta para una pregunta.
     */
    @PostMapping("/answer/create")
    public String createAnswer(
            @RequestParam String name,
            @RequestParam(defaultValue = "false") boolean correct,
            @RequestParam Long questionId) {

        Question question = finderService.findQuestionById(questionId);

        Answer newAnswer = new Answer(name, question, null, correct);
        answerRepository.save(newAnswer);

        return "redirect:/api/manage/question/" + questionId;
    }

// --- MÉTODOS DE EDICIÓN (NUEVOS) ---

    /**
     * Actualiza el nombre de una Categoría.
     */
    @PostMapping("/category/update")
    @Transactional
    public String updateCategory(@ModelAttribute UpdateNameDescriptionDTO updateDto, RedirectAttributes redirectAttributes) {
        return categoryRepository.findById(updateDto.getId()).map(category -> {
            category.setName(updateDto.getName());
            categoryRepository.save(category);
            redirectAttributes.addFlashAttribute("message", "Categoría actualizada correctamente.");
            return "redirect:/api/manage/config";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Categoría no encontrada.");
            return "redirect:/api/manage/config";
        });
    }

    /**
     * Actualiza el nombre y la descripción de un Bloque.
     */
    @PostMapping("/block/update")
    @Transactional
    public String updateBlock(@ModelAttribute UpdateNameDescriptionDTO updateDto, RedirectAttributes redirectAttributes) {
        return blockRepository.findById(updateDto.getId()).map(block -> {
            block.setName(updateDto.getName());
            block.setDescription(updateDto.getDescription());
            blockRepository.save(block);
            redirectAttributes.addFlashAttribute("message", "Bloque actualizado correctamente.");
            return "redirect:/api/manage/category/" + block.getCategory().getId();
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Bloque no encontrado.");
            return "redirect:/api/manage/config";
        });
    }

    /**
     * Actualiza el nombre y la descripción de un Tema.
     */
    @PostMapping("/theme/update")
    @Transactional
    public String updateTheme(@ModelAttribute UpdateNameDescriptionDTO updateDto, RedirectAttributes redirectAttributes) {
        return themeRepository.findById(updateDto.getId()).map(theme -> {
            theme.setName(updateDto.getName());
            theme.setDescription(updateDto.getDescription());
            themeRepository.save(theme);
            redirectAttributes.addFlashAttribute("message", "Tema actualizado correctamente.");
            return "redirect:/api/manage/block/" + theme.getBlock().getId();
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Tema no encontrado.");
            return "redirect:/api/manage/config";
        });
    }

    /**
     * Actualiza el enunciado de una Pregunta.
     */
    @PostMapping("/question/update")
    @Transactional
    public String updateQuestion(@ModelAttribute UpdateNameDescriptionDTO updateDto, RedirectAttributes redirectAttributes) {
        return questionRepository.findById(updateDto.getId()).map(question -> {
            question.setName(updateDto.getName());
            questionRepository.save(question);
            redirectAttributes.addFlashAttribute("message", "Pregunta actualizada correctamente.");
            return "redirect:/api/manage/theme/" + question.getTheme().getId();
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Pregunta no encontrada.");
            // Redirige al índice de preguntas si falla
            return "redirect:/api/manage/config";
        });
    }

    /**
     * Actualiza el nombre de una Respuesta.
     * Nota: El campo 'correct' se actualiza en 'updateAnswersBulk'.
     */
    @PostMapping("/answer/update")
    @Transactional
    public String updateAnswer(@ModelAttribute UpdateNameDescriptionDTO updateDto, RedirectAttributes redirectAttributes) {
        return answerRepository.findById(updateDto.getId()).map(answer -> {
            answer.setName(updateDto.getName());
            answerRepository.save(answer);
            redirectAttributes.addFlashAttribute("message", "Respuesta actualizada correctamente.");
            return "redirect:/api/manage/question/" + answer.getQuestion().getId();
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Respuesta no encontrada.");
            return "redirect:/api/manage/config";
        });
    }

    @PostMapping("/db/export")
    @Transactional
    public void exportToSql(@ModelAttribute String path) {
        // Ejecuta el comando nativo de H2 para volcar todo a un fichero
        if(path.isEmpty()){
            path = "C:/backup/backup.sql";
        }
        jdbcTemplate.execute("SCRIPT TO '" + path + "'");
    }

}