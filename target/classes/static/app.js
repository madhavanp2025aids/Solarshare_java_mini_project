const API_BASE = 'http://localhost:8080/api';

async function handleRequest(url, options) {
    try {
        const response = await fetch(url, options);
        if (!response.ok) {
            const errData = await response.json().catch(() => ({}));
            throw new Error(errData.error || errData.message || JSON.stringify(errData) || 'Request failed');
        }
        return await response.json();
    } catch (err) {
        throw err;
    }
}

function showFeedback(elementId, message, isError = false) {
    const el = document.getElementById(elementId);
    el.textContent = message;
    el.className = 'feedback ' + (isError ? 'error' : 'success');
    setTimeout(() => { el.textContent = ''; el.className = 'feedback'; }, 5000);
}

// 1. Create Installation
document.getElementById('installationForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = document.getElementById('instName').value;
    try {
        const data = await handleRequest(`${API_BASE}/installations`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name })
        });
        showFeedback('instResult', `Success! Installation created with ID: ${data.id}`);
        e.target.reset();
    } catch (err) {
        showFeedback('instResult', err.message, true);
    }
});

// 2. Add Household
document.getElementById('householdForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const instId = document.getElementById('hhInstId').value;
    const payload = {
        name: document.getElementById('hhName').value,
        allocationRatio: parseFloat(document.getElementById('hhRatio').value)
    };
    try {
        const data = await handleRequest(`${API_BASE}/installations/${instId}/households`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        showFeedback('hhResult', `Success! Household added with ID: ${data.id}`);
        e.target.reset();
    } catch (err) {
        showFeedback('hhResult', err.message, true);
    }
});

// 3. Log Generation
document.getElementById('genForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const instId = document.getElementById('genInstId').value;
    const payload = {
        date: document.getElementById('genDate').value,
        unitsGenerated: parseFloat(document.getElementById('genUnits').value)
    };
    try {
        const data = await handleRequest(`${API_BASE}/installations/${instId}/generation`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        showFeedback('genResult', `Generation logged for ${data.date}`);
        e.target.reset();
    } catch (err) {
        showFeedback('genResult', err.message, true);
    }
});

// 4. Log Consumption
document.getElementById('consForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const hhId = document.getElementById('consHhId').value;
    const payload = {
        date: document.getElementById('consDate').value,
        unitsConsumed: parseFloat(document.getElementById('consUnits').value)
    };
    try {
        const data = await handleRequest(`${API_BASE}/households/${hhId}/consumption`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        showFeedback('consResult', `Consumption logged for ${data.date}`);
        e.target.reset();
    } catch (err) {
        showFeedback('consResult', err.message, true);
    }
});

// 5. Generate Summary
document.getElementById('summaryForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const hhId = document.getElementById('sumHhId').value;
    const year = document.getElementById('sumYear').value;
    const month = document.getElementById('sumMonth').value;
    const resultDiv = document.getElementById('summaryResult');
    
    resultDiv.innerHTML = '<p>Loading...</p>';
    try {
        const data = await handleRequest(`${API_BASE}/households/${hhId}/summary?year=${year}&month=${month}`, {
            method: 'GET'
        });
        
        if (!data.dailyUsages || data.dailyUsages.length === 0) {
            resultDiv.innerHTML = '<p class="subtitle">No records found for this period.</p>';
            return;
        }

        let html = `
            <table class="summary-table">
                <thead>
                    <tr>
                        <th>Day</th>
                        <th>Generated Share (kWh)</th>
                        <th>Consumed (kWh)</th>
                        <th>Net Exported (kWh)</th>
                    </tr>
                </thead>
                <tbody>
        `;
        
        data.dailyUsages.forEach(usage => {
            html += `
                <tr>
                    <td>${usage.dayOfMonth}</td>
                    <td>${usage.generatedShare.toFixed(2)}</td>
                    <td>${usage.consumed.toFixed(2)}</td>
                    <td style="color: ${usage.netExported > 0 ? 'var(--success)' : 'inherit'}">${usage.netExported.toFixed(2)}</td>
                </tr>
            `;
        });
        
        html += `
                </tbody>
            </table>
            
            <div class="totals">
                <div class="total-item">
                    <span class="total-label">Total Share</span>
                    <span class="total-value">${data.totalGeneratedShare.toFixed(2)}</span>
                </div>
                <div class="total-item">
                    <span class="total-label">Total Consumed</span>
                    <span class="total-value">${data.totalConsumed.toFixed(2)}</span>
                </div>
                <div class="total-item">
                    <span class="total-label">Total Exported</span>
                    <span class="total-value">${data.totalNetExported.toFixed(2)}</span>
                </div>
            </div>
        `;
        resultDiv.innerHTML = html;
    } catch (err) {
        resultDiv.innerHTML = `<p class="feedback error">${err.message}</p>`;
    }
});
