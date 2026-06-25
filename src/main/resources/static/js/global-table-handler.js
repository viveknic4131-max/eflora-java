(function () {
    let debounceTimer;

    // Core AJAX Fetch implementation function 
    async function updateTableFragment(url, wrapper) {
        try {
            const response = await fetch(url, {
                headers: { 'X-Requested-With': 'XMLHttpRequest' }
            });
            if (!response.ok) throw new Error('Fragment retrieval failed');
            
            const htmlStream = await response.text();
            
            // Replaces old DOM tree with freshly rendered backend markup structure seamlessly
            wrapper.outerHTML = htmlStream;
        } catch (err) {
            console.error('AJAX Table Swap Error:', err);
        }
    }

    // Comprehensive URL construction helper extracting all runtime tracking options
    function dispatchTableState(wrapper, forcedPage = 0) {
        const baseUrl = wrapper.dataset.baseUrl;
        const currentSort = wrapper.dataset.currentSort || 'name';
        const currentDir = wrapper.dataset.currentDir || 'asc';
        
        const searchInput = wrapper.querySelector('.js-table-search');
        const sizeSelect = wrapper.querySelector('.js-table-size');
        
        const query = searchInput ? encodeURIComponent(searchInput.value) : '';
        const size = sizeSelect ? sizeSelect.value : 10;

        // Formats final request context query parameters matching backend patterns
        const targetUrl = `${baseUrl}?page=${forcedPage}&size=${size}&search=${query}&sort=${currentSort},${currentDir}`;
        updateTableFragment(targetUrl, wrapper);
    }

    // 1. Intercept Pagination Action Clicks (Global Listener Event Delegation)
    document.body.addEventListener('click', function (e) {
        const btn = e.target.closest('.js-page-link');
        if (!btn || btn.disabled || btn.classList.contains('is-active')) return;

        const wrapper = btn.closest('#table-card-wrapper');
        if (wrapper) {
            const targetPage = btn.dataset.page;
            dispatchTableState(wrapper, targetPage);
        }
    });

    // 2. Intercept Header Sorting Action Click Elements
    document.body.addEventListener('click', function (e) {
        const th = e.target.closest('#dt-table th[data-sort]');
        if (!th) return;

        const wrapper = th.closest('#table-card-wrapper');
        if (wrapper) {
            const requestedSortField = th.dataset.sort;
            let currentSortField = wrapper.dataset.currentSort;
            let currentDir = wrapper.dataset.currentDir;

            if (currentSortField === requestedSortField) {
                // Toggles direction value smoothly matching standard JS specification parameters
                wrapper.dataset.currentDir = currentDir === 'asc' ? 'desc' : 'asc';
            } else {
                wrapper.dataset.currentSort = requestedSortField;
                wrapper.dataset.currentDir = 'asc';
            }
            // Sorting change defaults layout view tracking state window context reset to page 0
            dispatchTableState(wrapper, 0);
        }
    });

    // 3. Intercept Row-Per-Page Configuration Option Value Modification
    document.body.addEventListener('change', function (e) {
        if (!e.target.classList.contains('js-table-size')) return;

        const wrapper = e.target.closest('#table-card-wrapper');
        if (wrapper) {
            dispatchTableState(wrapper, 0);
        }
    });

    // 4. Intercept Filtering Search Bar Inputs equipped with Debounce parameters
    document.body.addEventListener('input', function (e) {
        if (!e.target.classList.contains('js-table-search')) return;

        const wrapper = e.target.closest('#table-card-wrapper');
        if (wrapper) {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(() => {
                dispatchTableState(wrapper, 0);
            }, 400); // 400ms debounce protects backend infrastructure from heavy query execution loops
        }
    });

    // 5. Clear Search button integration handle
    document.body.addEventListener('click', function (e) {
        const clearBtn = e.target.closest('#dt-clear-search');
        if (!clearBtn) return;

        const wrapper = clearBtn.closest('#table-card-wrapper');
        const searchInput = wrapper ? wrapper.querySelector('.js-table-search') : null;
        if (wrapper && searchInput) {
            searchInput.value = '';
            dispatchTableState(wrapper, 0);
        }
    });
})();