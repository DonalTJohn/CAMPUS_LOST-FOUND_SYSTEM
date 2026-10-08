/**
 * Campus Lost & Found Portal - Live AJAX Search Component
 * Demonstrates fetch API, JSON handling, and DOM rendering.
 */

document.addEventListener('DOMContentLoaded', function () {
    const searchForm = document.getElementById('ajaxSearchForm');
    const keywordInput = document.getElementById('ajaxKeyword');
    const categorySelect = document.getElementById('ajaxCategory');
    const typeSelect = document.getElementById('ajaxType');
    const locationInput = document.getElementById('ajaxLocation');
    const resultsContainer = document.getElementById('searchResultsGrid');
    const resultsCountText = document.getElementById('resultsCount');

    if (!resultsContainer) return;

    let debounceTimer;

    function performSearch() {
        const keyword = keywordInput ? keywordInput.value : '';
        const categoryId = categorySelect ? categorySelect.value : '';
        const itemType = typeSelect ? typeSelect.value : '';
        const location = locationInput ? locationInput.value : '';

        const params = new URLSearchParams({
            format: 'json',
            keyword: keyword,
            categoryId: categoryId,
            itemType: itemType,
            location: location
        });

        // Fetch JSON data from SearchItemServlet
        fetch(contextPath + '/item/search?' + params.toString(), {
            headers: {
                'Accept': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) throw new Error('Search network response failed');
            return response.json();
        })
        .then(items => {
            renderItems(items);
        })
        .catch(err => {
            console.error('Search error:', err);
        });
    }

    function renderItems(items) {
        if (!items || items.length === 0) {
            resultsContainer.innerHTML = `
                <div style="grid-column: 1 / -1; text-align: center; padding: 4rem 1rem;">
                    <div style="font-size: 3rem; margin-bottom: 1rem;">🔍</div>
                    <h3 style="font-size: 1.5rem; margin-bottom: 0.5rem;">No Matching Items Found</h3>
                    <p style="color: var(--text-muted);">Try adjusting your keywords, category, or location filter.</p>
                </div>
            `;
            if (resultsCountText) resultsCountText.textContent = '0 items found';
            return;
        }

        if (resultsCountText) resultsCountText.textContent = items.length + (items.length === 1 ? ' item found' : ' items found');

        let html = '';
        items.forEach(item => {
            const badgeClass = item.itemType === 'LOST' ? 'badge-lost' : 'badge-found';
            const imgHtml = item.imagePath 
                ? `<img src="${contextPath}/${item.imagePath}" alt="${escapeHtml(item.itemName)}" class="item-img">`
                : `<div class="item-img-placeholder">${item.itemType === 'LOST' ? '❓' : '🎁'}</div>`;

            html += `
                <div class="item-card">
                    <div class="item-img-container">
                        <span class="card-type-badge ${badgeClass}">${item.itemType}</span>
                        <span class="badge-status">${item.status}</span>
                        ${imgHtml}
                    </div>
                    <div class="item-card-body">
                        <span class="item-category-tag">${escapeHtml(item.categoryName || 'General')}</span>
                        <h4 class="item-card-title">${escapeHtml(item.itemName)}</h4>
                        <p class="item-card-desc">${escapeHtml(item.description)}</p>
                        
                        <div class="item-meta-list">
                            <div class="item-meta-item">
                                <span>📍</span>
                                <span>${escapeHtml(item.location)}</span>
                            </div>
                            <div class="item-meta-item">
                                <span>📅</span>
                                <span>${item.incidentDate || 'Recent'}</span>
                            </div>
                        </div>
                    </div>
                    <div class="item-card-footer">
                        <a href="${contextPath}/item/details?id=${item.itemId}" class="btn btn-primary btn-sm">
                            View Details →
                        </a>
                        ${item.itemType === 'FOUND' && item.status === 'ACTIVE' 
                            ? `<a href="${contextPath}/claim/submit?itemId=${item.itemId}" class="btn btn-secondary btn-sm">Claim</a>`
                            : ''}
                    </div>
                </div>
            `;
        });

        resultsContainer.innerHTML = html;
    }

    function escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    // Attach event listeners for dynamic real-time querying
    if (keywordInput) {
        keywordInput.addEventListener('input', function () {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(performSearch, 300);
        });
    }

    if (locationInput) {
        locationInput.addEventListener('input', function () {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(performSearch, 300);
        });
    }

    if (categorySelect) {
        categorySelect.addEventListener('change', performSearch);
    }

    if (typeSelect) {
        typeSelect.addEventListener('change', performSearch);
    }

    if (searchForm) {
        searchForm.addEventListener('submit', function (e) {
            e.preventDefault();
            performSearch();
        });
    }
});
