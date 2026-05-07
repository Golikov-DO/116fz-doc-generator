// Add new Object Substance
function addNewHazardous() {

    const backUrl = window.location.pathname + window.location.search;

    postRedirect('create-empty-hazardous-substance', {
        backUrl: backUrl
    });
}

// View Object Substance
function viewSubstance(substanceId) {
    const params = new URLSearchParams(window.location.search);

    const backUrl = params.get('backUrl')
        || (window.location.pathname + window.location.search);

    navigateTo('/hazardous-substance', {
        mode: 'view',
        id: substanceId,
        backUrl: backUrl
    });
}

// Edit Object Substance
function editHazardousFromSelect() {

    const select = document.getElementById('hazardous_substance_id');

    if (!select) {
        console.error('select not found');
        return;
    }

    const hazardId = select.value;

    if (!hazardId || hazardId === 'new_hazard') {
        alert('Выберите вещество');
        return;
    }

    const backUrl = window.location.pathname + window.location.search;

    window.location.href =
        '/hazardous-substance?id=' + hazardId +
        '&backUrl=' + encodeURIComponent(backUrl);
}

// Delete Object Substance
function deleteHazardous(id) {

        if (!confirm('Удалить тип объекта?')) return;

        const params = new URLSearchParams(window.location.search);

        const backUrl = params.get('backUrl')
            || (window.location.pathname + window.location.search);

        postRedirect('delete-hazardous-substance', {
            id: id,
            backUrl: backUrl
        });
}