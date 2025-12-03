$(document).ready(function () {

    // Marcar categoría → marca bloques y temas
    $('.category-checkbox').on('change', function () {
        const categoryId = $(this).data('id');
        if ($(this).is(':checked')) {
            $(`.block-checkbox[data-category-id=${categoryId}]`).prop('checked', true).trigger('change');
        }
    });

    // Marcar bloque → marca sus temas
    $('.block-checkbox').on('change', function () {
        const blockId = $(this).data('id');
        if ($(this).is(':checked')) {
            $(`.theme-checkbox[data-block-id=${blockId}]`).prop('checked', true);
        } else {
            // Desmarcar categoría si se desmarca el bloque
            const categoryId = $(this).data('category-id');
            $(`.category-checkbox[data-id=${categoryId}]`).prop('checked', false);
        }
    });

    // Desmarcar tema → desmarcar bloque y categoría
    $('.theme-checkbox').on('change', function () {
        if (!$(this).is(':checked')) {
            const blockId = $(this).data('block-id');
            const categoryId = $(this).data('category-id');

            $(`.block-checkbox[data-id=${blockId}]`).prop('checked', false);
            $(`.category-checkbox[data-id=${categoryId}]`).prop('checked', false);
        }
    });

});

$("#questions-form").on("submit", function(e) {
    e.preventDefault();

    const categoryIds = $(".category-checkbox:checked").map(function() {
        return $(this).data("id");
    }).get();

    const blockIds = $(".block-checkbox:checked").map(function() {
        return $(this).data("id");
    }).get();

    const themeIds = $(".theme-checkbox:checked").map(function() {
        return $(this).data("id");
    }).get();

    $.ajax({
        url: "/api/questions/by_ids", // Ojo: esta es tu ruta correcta
        method: "GET",
        data: {
            categoryIds: categoryIds,
            blockIds: blockIds,
            themeIds: themeIds
        },
        success: function(response) {
            // Si devuelves una vista:
            document.open();
            document.write(response);
            document.close();

            // Si devuelves JSON:
            // console.log("Preguntas devueltas:", response);
        },
        error: function() {
            console.error("Error al obtener las preguntas");
        }
    });
});
