// Add new Object Type
function addNewObjectType() {

    const backUrl = window.location.pathname + window.location.search;

    postRedirect('create-empty-object-type', {
        backUrl: backUrl
    });
}

// View Object Type
function viewType(typeId) {
    const params = new URLSearchParams(window.location.search);

    const backUrl = params.get('backUrl')
        || (window.location.pathname + window.location.search);

    navigateTo('/object-type', {
        mode: 'view',
        id: typeId,
        backUrl: backUrl
    });
}

// Edit Object Type
function editType() {

    const select = document.getElementById('object_type_id');

    if (!select) {
        console.error('select not found');
        return;
    }

    const typeId = select.value;

    if (!typeId || typeId === 'new_type') {
        alert('Выберите тип объекта');
        return;
    }

    const backUrl = window.location.pathname + window.location.search;

    window.location.href =
        '/object-type?id=' + typeId +
        '&backUrl=' + encodeURIComponent(backUrl);
}

// Delete Object Type
function deleteObjectType(id) {

    if (!confirm('Удалить тип объекта?')) return;

    const params = new URLSearchParams(window.location.search);

    const backUrl = params.get('backUrl')
        || (window.location.pathname + window.location.search);

    postRedirect('delete-object-type', {
        id: id,
        backUrl: backUrl
    });
}