function confirmDelete(button) {
    const categoryName = button.getAttribute('data-category-name');
    const message = '¿Estás seguro de que deseas eliminar la categoría ' + categoryName + '? Esto eliminará todos los bloques, temas, preguntas y respuestas asociados y es irreversible.';
    return confirm(message);
}