package com.opotromatic.services;

import com.opotromatic.DTO.UpdateNameDescriptionDTO;
import com.opotromatic.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class UpdateService {

    private final CategoryRepository categoryRepository;
    private final ThemeRepository themeRepository;
    private final BlockRepository blockRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public UpdateService(
            CategoryRepository categoryRepository,
            ThemeRepository themeRepository,
            BlockRepository blockRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository){
        this.categoryRepository = categoryRepository;
        this.blockRepository = blockRepository;
        this.themeRepository = themeRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }



    // --- MÉTODOS DE EDICIÓN (NUEVOS) ---

    /**
     * Actualiza el nombre de una Categoría.
     */
    public String updateCategory(UpdateNameDescriptionDTO updateDto, RedirectAttributes redirectAttributes) {
        return categoryRepository.findById(updateDto.getId()).map(category -> {
            category.setName(updateDto.getName());
            categoryRepository.save(category);
            redirectAttributes.addFlashAttribute("message", "Categoría actualizada correctamente.");
            return "redirect:/api/manage/category/" + category.getId();
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
            return "redirect:/api/manage/block/" + block.getId();
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
            return "redirect:/api/manage/theme/" + theme.getId();
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
            return "redirect:/api/manage/question/" + question.getId();
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
}
