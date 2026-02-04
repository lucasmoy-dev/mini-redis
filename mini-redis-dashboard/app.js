const CONFIG = {
    API_URL: 'http://localhost:8080/api'
};

const elements = {
    input: document.getElementById('console-input'),
    output: document.getElementById('console-output'),
    sendBtn: document.getElementById('send-btn'),
    status: document.getElementById('server-status'),
    statKeys: document.getElementById('stat-keys'),
    quickBtns: document.querySelectorAll('.action-btn'),
    clearBtn: document.getElementById('clear-console')
};

async function executeCommand(command) {
    if (!command.trim()) return;

    appendLine(`> ${command}`, 'user');

    try {
        const response = await fetch(`${CONFIG.API_URL}/?cmd=${encodeURIComponent(command)}`);
        const text = await response.text();
        appendLine(text, 'response');
        updateStats();
        updateServerStatus(true);
    } catch (error) {
        appendLine(`Error: ${error.message}`, 'error');
        updateServerStatus(false);
    }
}

function appendLine(text, type) {
    const div = document.createElement('div');
    div.className = `line ${type}`;
    div.textContent = text;
    elements.output.appendChild(div);
    elements.output.scrollTop = elements.output.scrollHeight;
}

async function updateStats() {
    try {
        const response = await fetch(`${CONFIG.API_URL}/?cmd=DBSIZE`);
        const size = await response.text();
        elements.statKeys.textContent = size;
    } catch (e) {
        elements.statKeys.textContent = '--';
    }
}

async function updateServerStatus(isOnline) {
    if (isOnline) {
        elements.status.classList.add('online');
        elements.status.querySelector('.text').textContent = 'Server Online';
    } else {
        elements.status.classList.remove('online');
        elements.status.querySelector('.text').textContent = 'Server Offline';
    }
}

async function checkHealth() {
    try {
        const res = await fetch(`${CONFIG.API_URL}/swagger-ui.html`, { mode: 'no-cors' });
        updateServerStatus(true);
        updateStats();
    } catch (e) {
        updateServerStatus(false);
    }
}

// Event Listeners
elements.sendBtn.addEventListener('click', () => {
    const cmd = elements.input.value;
    executeCommand(cmd);
    elements.input.value = '';
});

elements.input.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        elements.sendBtn.click();
    }
});

elements.quickBtns.forEach(btn => {
    btn.addEventListener('click', () => {
        const cmd = btn.getAttribute('data-cmd');
        elements.input.value = cmd;
        elements.input.focus();
    });
});

elements.clearBtn.addEventListener('click', () => {
    elements.output.innerHTML = '<div class="line system">Console cleared.</div>';
});

// Super Realistic Explosion Logic
document.getElementById('start-discovery').addEventListener('click', function () {
    const overlay = document.getElementById('intro-overlay');

    // 1. Initial Shockwave
    createShockwave();

    // 2. Powerful Flash
    const flash = document.createElement('div');
    flash.className = 'flash-overlay';
    document.body.appendChild(flash);

    // 3. Screen Shake
    document.body.classList.add('shake');

    // 4. Multi-layered Particles (Embers & Debris)
    const centerX = window.innerWidth / 2;
    const centerY = window.innerHeight / 2;

    for (let i = 0; i < 300; i++) {
        setTimeout(() => {
            spawnPhysicalParticle(centerX, centerY);
        }, Math.random() * 200);
    }

    // 5. Subsequent Shockwaves
    setTimeout(createShockwave, 200);
    setTimeout(createShockwave, 450);

    // Fade out overlay
    overlay.style.opacity = '0';
    setTimeout(() => {
        overlay.style.display = 'none';
        document.body.classList.remove('shake');
        flash.remove();
    }, 1200);
});

function createShockwave() {
    const sw = document.createElement('div');
    sw.className = 'shockwave';
    document.body.appendChild(sw);
    setTimeout(() => sw.remove(), 1000);
}

function spawnPhysicalParticle(x, y) {
    const p = document.createElement('div');
    const isEmber = Math.random() > 0.3;
    p.className = isEmber ? 'particle ember' : 'particle';

    const size = Math.random() * (isEmber ? 6 : 15) + 2;
    p.style.width = `${size}px`;
    p.style.height = `${size}px`;

    const colors = isEmber
        ? ['#ff4500', '#ff8c00', '#ffd700', '#ffffff']
        : ['#6366f1', '#ec4899', '#3b82f6', '#1e1b4b'];

    p.style.background = colors[Math.floor(Math.random() * colors.length)];
    if (isEmber) p.style.boxShadow = `0 0 ${size * 2}px ${p.style.background}`;

    document.body.appendChild(p);

    const angle = Math.random() * Math.PI * 2;
    const force = Math.random() * 25 + 10;
    let vx = Math.cos(angle) * force;
    let vy = Math.sin(angle) * force;
    let posX = x;
    let posY = y;
    let opacity = 1;
    const gravity = 0.25;
    const friction = 0.98;

    const anim = setInterval(() => {
        posX += vx;
        posY += vy;
        vy += gravity; // Gravity effect
        vx *= friction;
        vy *= friction;
        opacity -= 0.015;

        p.style.left = `${posX}px`;
        p.style.top = `${posY}px`;
        p.style.opacity = opacity;
        p.style.transform = `scale(${opacity})`;

        if (opacity <= 0 || posY > window.innerHeight + 50) {
            clearInterval(anim);
            p.remove();
        }
    }, 16);
}

// Initial Load
checkHealth();
setInterval(checkHealth, 5000);
