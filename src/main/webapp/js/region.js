// Add new Region
function addNewRegion() {

    const backUrl = window.location.pathname + window.location.search;

    postRedirect('create-empty-region', {
        backUrl: backUrl
    });
}

// View Region
function viewRegion(cityId) {
    const params = new URLSearchParams(window.location.search);

    const backUrl = params.get('backUrl')
        || (window.location.pathname + window.location.search);

    navigateTo('/region', {
        mode: 'view',
        cityId: cityId,
        backUrl: backUrl
    });
}

// Edit Region from select
function editRegionFromSelect() {

    const select = document.getElementById('object_city_id');

    if (!select) {
        console.error('select not found');
        return;
    }

    const cityId = select.value;

    if (!cityId || cityId === 'new_region') {
        alert('Выберите район расположения');
        return;
    }

    const backUrl = window.location.pathname + window.location.search;

    window.location.href =
        '/region?cityId=' + cityId +
        '&backUrl=' + encodeURIComponent(backUrl);
}

// Delete Region
function deleteRegion(cityId) {

    if (!confirm('Удалить район?')) return;

    const params = new URLSearchParams(window.location.search);

    const backUrl = params.get('backUrl')
        || (window.location.pathname + window.location.search);

    postRedirect('delete-region', {
        cityId: cityId,
        backUrl: backUrl
    });
}