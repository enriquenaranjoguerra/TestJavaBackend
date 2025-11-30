$(document).ready(function() {
    $('.delete-btn').on('click', function() {
        const id = $(this).data('id');
        const type = $(this).data('type'); // 'category', 'block', etc.

        if (confirm(`¿Estás seguro de que quieres eliminar esta ${type} (ID: ${id})?`)) {

            $.ajax({
                url: `/api/manage/${type}/${id}`,
                type: 'DELETE',
                success: function(result) {
                    alert(`Eliminación de ${type} exitosa.`);
                    // Recargar la página para actualizar el listado
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    alert(`Error al eliminar ${type}. Puede que tenga elementos asociados.`);
                    console.error("Error: ", error);
                }
            });
        }
    });
});