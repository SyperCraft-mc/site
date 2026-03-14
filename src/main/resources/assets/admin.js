/* ═══════════════════════════════════════════════════════════
   obsidian-ui.js  v1.1
   UI helpers pour le panel admin Obsidian.
   Aucune logique métier — uniquement des composants UI.

   Tous les composants sont réutilisables :
   - Basés sur classes CSS + attributs data-*
   - Aucun ID hardcodé
   - Plusieurs instances par page supportées
   ═══════════════════════════════════════════════════════════ */

(function () {

  /* ── Sidebar burger / drawer ──────────────────────────── */
  (function () {
    const sidebar = document.querySelector('.sidebar');
    const burger  = document.querySelector('[data-sidebar-toggle]') || document.getElementById('sb-toggle');
    const overlay = document.querySelector('.sidebar-overlay');
    if (!sidebar || !burger) return;

    const open  = () => { sidebar.classList.add('open');    overlay?.classList.add('open');    };
    const close = () => { sidebar.classList.remove('open'); overlay?.classList.remove('open'); };

    burger.addEventListener('click', () => sidebar.classList.contains('open') ? close() : open());
    overlay?.addEventListener('click', close);
    document.addEventListener('keydown', e => { if (e.key === 'Escape') close(); });
    window.addEventListener('resize', () => { if (window.innerWidth > 768) close(); });

    function syncBurger() { burger.style.display = window.innerWidth <= 768 ? 'flex' : 'none'; }
    syncBurger();
    window.addEventListener('resize', syncBurger);
  })();

  /* ── User dropdown ────────────────────────────────────── */
  document.querySelectorAll('.sb-user-wrap').forEach(wrap => {
    const trigger  = wrap.querySelector('.sb-user');
    const dropdown = wrap.querySelector('.sb-dropdown');
    if (!trigger) return;
    trigger.addEventListener('click', e => { e.stopPropagation(); wrap.classList.toggle('open'); });
    dropdown?.addEventListener('click', e => e.stopPropagation());
  });
  document.addEventListener('click', () =>
    document.querySelectorAll('.sb-user-wrap.open').forEach(w => w.classList.remove('open'))
  );

  /* ── Chip groups ──────────────────────────────────────── */
  document.querySelectorAll('.chip-group').forEach(group => {
    group.querySelectorAll('.chip').forEach(chip => {
      chip.addEventListener('click', () => {
        group.querySelectorAll('.chip').forEach(c => c.classList.remove('active'));
        chip.classList.add('active');
      });
    });
  });

  /* ── Toggle switches ──────────────────────────────────── */
  document.querySelectorAll('.toggle').forEach(t =>
    t.addEventListener('click', () => t.classList.toggle('on'))
  );

  /* ── Panel nav ────────────────────────────────────────── */
  document.querySelectorAll('.panel-nav').forEach(nav => {
    nav.querySelectorAll('.panel-nav-item').forEach(item => {
      item.addEventListener('click', () => {
        nav.querySelectorAll('.panel-nav-item').forEach(i => i.classList.remove('active'));
        item.classList.add('active');
      });
    });
  });

  /* ── Animated counter [data-target] ──────────────────── */
  function animCount(el) {
    const target  = parseFloat(el.dataset.target);
    const isFloat = el.dataset.float === 'true';
    const dur = 1100, start = performance.now();
    (function step(now) {
      const p    = Math.min((now - start) / dur, 1);
      const ease = 1 - Math.pow(2, -10 * p);
      const val  = ease * target;
      el.textContent = isFloat ? val.toFixed(1) : Math.round(val).toLocaleString('fr');
      if (p < 1) requestAnimationFrame(step);
      else el.textContent = isFloat ? target.toFixed(1) : target.toLocaleString('fr');
    })(performance.now());
  }

  const countObserver = new IntersectionObserver(entries => {
    entries.forEach(e => {
      if (!e.isIntersecting) return;
      e.target.querySelectorAll('[data-target]').forEach(animCount);
      countObserver.unobserve(e.target);
    });
  }, { threshold: .2 });

  document.querySelectorAll('.stat-card, .stats-grid, .stat-mini').forEach(el => countObserver.observe(el));
  setTimeout(() => {
    document.querySelectorAll('[data-target]').forEach(el => {
      if (el.textContent === '0') animCount(el);
    });
  }, 200);

  /* ── Refresh spin [data-refresh] ─────────────────────── */
  document.querySelectorAll('[data-refresh]').forEach(btn => {
    btn.addEventListener('click', () => {
      btn.style.transition = 'transform .6s ease';
      btn.style.transform  = 'rotate(360deg)';
      setTimeout(() => { btn.style.transform = ''; btn.style.transition = ''; }, 650);
    });
  });

  /* ── Clipboard copy [data-copy] ──────────────────────── */
  document.querySelectorAll('[data-copy]').forEach(el => {
    el.style.cursor = 'pointer';
    el.title = 'Copier';
    el.addEventListener('click', () => {
      navigator.clipboard.writeText(el.dataset.copy).then(() => {
        const orig = el.textContent;
        el.textContent = '✓ copié';
        setTimeout(() => el.textContent = orig, 1200);
      });
    });
  });

  /* ── Collapsible [data-collapse="targetId"] ───────────── */
  document.querySelectorAll('[data-collapse]').forEach(trigger => {
    trigger.style.cursor = 'pointer';
    trigger.addEventListener('click', () => {
      const target = document.getElementById(trigger.dataset.collapse);
      if (!target) return;
      const isOpen = target.style.display !== 'none';
      target.style.display = isOpen ? 'none' : '';
      trigger.classList.toggle('collapsed', isOpen);
    });
  });

  /* ── Live clock [data-clock] ──────────────────────────── */
  function updateClock() {
    document.querySelectorAll('[data-clock]').forEach(el => {
      el.textContent = new Date().toLocaleTimeString('fr', {
        hour: '2-digit', minute: '2-digit', second: '2-digit'
      });
    });
  }
  updateClock();
  setInterval(updateClock, 1000);

  /* ── Sparkline [data-spark="v1,v2,…"] ────────────────── */
  document.querySelectorAll('.sparkline[data-spark]').forEach(el => {
    const vals = el.dataset.spark.split(',').map(Number);
    const max  = Math.max(...vals);
    el.style.cssText += 'display:flex;align-items:flex-end;';
    vals.forEach((v, i) => {
      const bar = document.createElement('div');
      bar.style.cssText = `flex:1;border-radius:1px 1px 0 0;height:${(v / max * 100)}%;
        background:linear-gradient(to top,#7C3AED,#8B5CF6);opacity:.65;
        animation:barfill-v .6s ease ${i * .04}s both;transform-origin:bottom;transition:opacity .2s;`;
      bar.title = v;
      bar.onmouseenter = () => bar.style.opacity = '1';
      bar.onmouseleave = () => bar.style.opacity = '.65';
      el.appendChild(bar);
    });
  });

  /* ── Donut [canvas.donut data-vals data-colors] ───────── */
  document.querySelectorAll('canvas.donut').forEach(canvas => {
    const vals   = canvas.dataset.vals.split(',').map(Number);
    const colors = canvas.dataset.colors.split(',');
    const ctx    = canvas.getContext('2d');
    const cx = canvas.width / 2, cy = canvas.height / 2;
    const r  = Math.min(cx, cy) - 8;
    const total = vals.reduce((a, b) => a + b, 0);
    let start = -Math.PI / 2;
    vals.forEach((v, i) => {
      const slice = (v / total) * Math.PI * 2;
      ctx.beginPath(); ctx.moveTo(cx, cy);
      ctx.arc(cx, cy, r, start, start + slice);
      ctx.closePath();
      ctx.fillStyle = colors[i] || '#8B5CF6';
      ctx.fill();
      start += slice;
    });
    ctx.beginPath();
    ctx.arc(cx, cy, r * .62, 0, Math.PI * 2);
    ctx.fillStyle = getComputedStyle(document.documentElement)
      .getPropertyValue('--surface').trim() || '#0d0d1a';
    ctx.fill();
  });

  /* ── Modal [data-modal-open="id"] ────────────────────── */
  function openModal(id)  {
    const b = document.getElementById(id);
    if (!b) return;
    b.classList.add('open');
    document.body.style.overflow = 'hidden';
  }
  function closeModal(id) {
    const b = document.getElementById(id);
    if (!b) return;
    b.classList.remove('open');
    document.body.style.overflow = '';
  }
  document.addEventListener('click', e => {
    const btn = e.target.closest('[data-modal-open]');
    if (btn) openModal(btn.dataset.modalOpen);
  });
  document.querySelectorAll('[data-modal-close]').forEach(btn =>
    btn.addEventListener('click', () => {
      const b = btn.closest('.modal-backdrop');
      if (b) closeModal(b.id);
    })
  );
  document.querySelectorAll('.modal-backdrop').forEach(b =>
    b.addEventListener('click', e => { if (e.target === b) closeModal(b.id); })
  );

  /* ── Offcanvas [data-oc-open="id"] ───────────────────── */
  function openOC(id) {
    const p = document.getElementById(id);
    if (!p) return;
    p.classList.add('open');
    document.querySelector(`.oc-backdrop[data-oc-for="${id}"]`)?.classList.add('open');
    document.body.style.overflow = 'hidden';
  }
  function closeOC(id) {
    const p = document.getElementById(id);
    if (!p) return;
    p.classList.remove('open');
    document.querySelector(`.oc-backdrop[data-oc-for="${id}"]`)?.classList.remove('open');
    document.body.style.overflow = '';
  }
  document.addEventListener('click', e => {
    const btn = e.target.closest('[data-oc-open]');
    if (btn) openOC(btn.dataset.ocOpen);
  });
  document.querySelectorAll('[data-oc-close]').forEach(btn =>
    btn.addEventListener('click', () => {
      const p = btn.closest('.offcanvas');
      if (p) closeOC(p.id);
    })
  );
  document.querySelectorAll('.oc-backdrop').forEach(b =>
    b.addEventListener('click', () => { if (b.dataset.ocFor) closeOC(b.dataset.ocFor); })
  );

  const restore = document.body.dataset.ocRestore;
  if (restore) openOC(restore);

  /* Esc ferme modals + offcanvas + sidebar */
  document.addEventListener('keydown', e => {
    if (e.key !== 'Escape') return;
    document.querySelectorAll('.modal-backdrop.open').forEach(b => closeModal(b.id));
    document.querySelectorAll('.oc-backdrop.open').forEach(b => {
      if (b.dataset.ocFor) closeOC(b.dataset.ocFor);
    });
  });

})();

/* ── Inject keyframe une seule fois ──────────────────────── */
if (!document.getElementById('oui-kf')) {
  const s = document.createElement('style');
  s.id = 'oui-kf';
  s.textContent = '@keyframes barfill-v{from{transform:scaleY(0)}to{transform:scaleY(1)}}';
  document.head.appendChild(s);
}

/* ═══════════════════════════════════════════════════════════
   Composants — tous réutilisables, aucun ID hardcodé
   ═══════════════════════════════════════════════════════════ */

/* ── Accordion ────────────────────────────────────────────
   <button class="acc-trigger" data-acc="panelId">
   <div class="acc-content" id="panelId">               */
document.querySelectorAll('.acc-trigger').forEach(t => {
  t.addEventListener('click', () => {
    const content = document.getElementById(t.dataset.acc);
    if (!content) return;
    const isOpen = content.classList.contains('open');
    t.closest('.accordion').querySelectorAll('.acc-content').forEach(c => c.classList.remove('open'));
    t.closest('.accordion').querySelectorAll('.acc-trigger').forEach(x => x.classList.remove('active'));
    if (!isOpen) { content.classList.add('open'); t.classList.add('active'); }
  });
});

/* ── Tabs ─────────────────────────────────────────────────
   <button class="tab-btn" data-tab="panelId">
   <div class="tab-panel" id="panelId">                 */
document.querySelectorAll('.tab-btn').forEach(btn => {
  btn.addEventListener('click', () => {
    btn.closest('.tab-list').querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    btn.closest('.tabs').querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
    btn.classList.add('active');
    document.getElementById(btn.dataset.tab)?.classList.add('active');
  });
});

/* ── Segmented control ────────────────────────────────────
   <button class="seg-btn" data-seg="groupName">        */
document.querySelectorAll('.seg-btn').forEach(btn => {
  btn.addEventListener('click', () => {
    document.querySelectorAll(`.seg-btn[data-seg="${btn.dataset.seg}"]`)
      .forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
  });
});

/* ── Custom select ────────────────────────────────────────
   <div class="select-wrap">
     <div class="select-btn"><span>Label</span>…</div>
     <div class="select-menu">
       <div class="select-option">…</div>              */
document.querySelectorAll('.select-wrap').forEach(wrap => {
  const btn = wrap.querySelector('.select-btn');
  if (!btn) return;
  btn.addEventListener('click', e => { e.stopPropagation(); wrap.classList.toggle('open'); });
  wrap.querySelectorAll('.select-option').forEach(opt => {
    opt.addEventListener('click', e => {
      e.stopPropagation();
      wrap.querySelectorAll('.select-option').forEach(o => o.classList.remove('selected'));
      opt.classList.add('selected');
      btn.querySelector('span:first-child').textContent = opt.textContent;
      wrap.classList.remove('open');
    });
  });
});
document.addEventListener('click', () =>
  document.querySelectorAll('.select-wrap.open').forEach(w => w.classList.remove('open'))
);

/* ── Range slider ─────────────────────────────────────────
   <input class="obsidian-range" type="range"
     data-range-for="labelId" data-suffix="%">
   <span id="labelId">50%</span>                        */
document.querySelectorAll('input[type=range].obsidian-range').forEach(input => {
  const label  = input.dataset.rangeFor ? document.getElementById(input.dataset.rangeFor) : null;
  const suffix = input.dataset.suffix ?? '';
  if (label) label.textContent = input.value + suffix;
  input.addEventListener('input', () => {
    if (label) label.textContent = input.value + suffix;
  });
});

/* ── Checkboxes ───────────────────────────────────────────
   <div class="form-check [checked]">
     <div class="form-check-box"></div>
     <span class="form-check-label">…</span>           */
document.querySelectorAll('.form-check').forEach(c => {
  const box = c.querySelector('.form-check-box');
  if (!box) return;
  if (c.classList.contains('checked')) box.textContent = '✓';
  c.addEventListener('click', () => {
    c.classList.toggle('checked');
    box.textContent = c.classList.contains('checked') ? '✓' : '';
  });
});

/* ── Pagination ───────────────────────────────────────────
   <div class="pagination">
     <button class="pg-btn">1</button>…               */
document.querySelectorAll('.pagination').forEach(pag => {
  pag.querySelectorAll('.pg-btn:not([disabled])').forEach(btn => {
    btn.addEventListener('click', () => {
      if (isNaN(btn.textContent.trim())) return;
      pag.querySelectorAll('.pg-btn').forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
    });
  });
});

/* ── Toast ────────────────────────────────────────────────
   spawnToast('✓', 'Titre', 'Message', 'toast-success')
   Le container .toast-stack est créé auto si absent.   */
function spawnToast(icon, label, msg, cls) {
  let container = document.querySelector('.toast-stack');
  if (!container) {
    container = document.createElement('div');
    container.className = 'toast-stack';
    document.body.appendChild(container);
  }
  container.classList.add('has-toasts');
  const t = document.createElement('div');
  t.className = `toast ${cls}`;
  t.innerHTML = `<span class="toast-icon">${icon}</span>
    <div class="toast-msg"><strong style="display:block;margin-bottom:2px;">${label}</strong>${msg}</div>
    <button class="toast-dismiss" onclick="this.closest('.toast').remove()">&#10005;</button>`;
  container.appendChild(t);
  setTimeout(() => {
    t.style.cssText = 'opacity:0;transform:translateX(20px);transition:all .3s';
    setTimeout(() => t.remove(), 300);
  }, 3500);
}

/* ── Heatmap ──────────────────────────────────────────────
   <div class="heatmap-root"
     data-weeks="12"
     data-months="Oct,Nov,Déc,Jan,Fév,Mar"
     data-max="312"
     data-unit="req">                                   */
document.querySelectorAll('.heatmap-root').forEach(root => {
  const weeks  = parseInt(root.dataset.weeks  ?? 12);
  const maxVal = parseInt(root.dataset.max    ?? 100);
  const unit   = root.dataset.unit ?? '';
  const months = (root.dataset.months ?? '').split(',').filter(Boolean);

  if (months.length) {
    const lr = document.createElement('div');
    lr.className = 'heatmap-labels';
    months.forEach(m => {
      const s = document.createElement('span');
      s.className = 'hm-month'; s.textContent = m; lr.appendChild(s);
    });
    root.appendChild(lr);
  }

  const grid = document.createElement('div');
  grid.style.cssText = 'display:flex;flex-direction:row;gap:3px;';
  for (let w = 0; w < weeks; w++) {
    const col = document.createElement('div');
    col.style.cssText = 'display:flex;flex-direction:column;gap:3px;';
    for (let d = 0; d < 7; d++) {
      const r    = Math.random();
      const lvl  = r < .35 ? 0 : r < .55 ? 1 : r < .75 ? 2 : r < .90 ? 3 : 4;
      const cell = document.createElement('div');
      cell.className = `hm-cell hm-l${lvl}`;
      cell.title = `${Math.floor(r * maxVal)} ${unit}`;
      col.appendChild(cell);
    }
    grid.appendChild(col);
  }
  root.appendChild(grid);

  const leg = document.createElement('div');
  leg.className = 'heatmap-legend';
  leg.innerHTML = '<span>Moins</span>';
  [0, 1, 2, 3, 4].forEach(l => {
    const c = document.createElement('div');
    c.className = `hm-cell hm-l${l}`; leg.appendChild(c);
  });
  leg.innerHTML += '<span>Plus</span>';
  root.appendChild(leg);
});

/* ── Gauges ───────────────────────────────────────────────
   <div class="gauge-root"
     data-size="80"
     data-gauges='[{"label":"CPU","val":34,"color":"#8B5CF6"}]'> */
document.querySelectorAll('.gauge-root').forEach(root => {
  const size = parseInt(root.dataset.size ?? 80);
  let gauges;
  try { gauges = JSON.parse(root.dataset.gauges); }
  catch {
    gauges = [
      { label: 'CPU',    val: 34, color: '#8B5CF6' },
      { label: 'RAM',    val: 62, color: '#7dd3fc'  },
      { label: 'Disque', val: 28, color: '#4ade80'  },
    ];
  }
  gauges.forEach(g => {
    const r    = (size / 2) - 8;
    const circ = 2 * Math.PI * r;
    const off  = circ * (1 - g.val / 100);
    const wrap = document.createElement('div');
    wrap.className = 'gauge-wrap';
    wrap.innerHTML = `
      <div class="gauge-container" style="width:${size}px;height:${size}px;">
        <svg class="gauge-svg" width="${size}" height="${size}">
          <circle class="gauge-track" cx="${size/2}" cy="${size/2}" r="${r}" stroke-width="6"/>
          <circle class="gauge-fill"  cx="${size/2}" cy="${size/2}" r="${r}" stroke-width="6"
            stroke="${g.color}" stroke-dasharray="${circ}" stroke-dashoffset="${circ}"/>
        </svg>
        <div class="gauge-center">
          <div class="gauge-val" style="color:${g.color};font-size:.9rem;">${g.val}%</div>
        </div>
      </div>
      <div class="gauge-label">${g.label}</div>`;
    root.appendChild(wrap);
    requestAnimationFrame(() => requestAnimationFrame(() => {
      wrap.querySelector('.gauge-fill').style.strokeDashoffset = off;
    }));
  });
});

/* ── Treemap ──────────────────────────────────────────────
   <div class="treemap-root"
     data-items='[{"label":"/api/users","val":43,"color":"#8B5CF6"}]'> */
document.querySelectorAll('.treemap-root').forEach(root => {
  let items;
  try { items = JSON.parse(root.dataset.items); }
  catch { return; }
  const total = items.reduce((s, d) => s + d.val, 0);
  items.forEach(d => {
    const col  = document.createElement('div');
    col.className = 'tm-col';
    col.style.flex = d.val;
    const cell = document.createElement('div');
    cell.className = 'tm-cell';
    cell.style.cssText = `background:${d.color}22;border-left:2px solid ${d.color}55;color:${d.color};`;
    cell.textContent = d.val >= 10 ? d.label : '';
    cell.title = `${d.label} — ${Math.round(d.val / total * 100)}%`;
    col.appendChild(cell);
    root.appendChild(col);
  });
});

/* ── Card expand ──────────────────────────────────────────
   <div class="card-expand-trigger [open]">
   <div class="card-expand-body [open]">
     <div class="card-expand-inner">…                  */
document.querySelectorAll('.card-expand-trigger').forEach(trigger => {
  trigger.addEventListener('click', () => {
    const body = trigger.nextElementSibling;
    if (!body?.classList.contains('card-expand-body')) return;
    const isOpen = body.classList.contains('open');
    body.classList.toggle('open', !isOpen);
    trigger.classList.toggle('open', !isOpen);
  });
});

/* ── Rating stars ─────────────────────────────────────────
   <div class="rating" data-rating="3">
     <span class="rating-star">★</span> × 5
   </div>
   <span class="rating-val">3/5</span>                 */
document.querySelectorAll('.rating').forEach(rating => {
  const stars  = rating.querySelectorAll('.rating-star');
  const valEl  = rating.nextElementSibling?.classList.contains('rating-val')
    ? rating.nextElementSibling : null;
  let current  = parseInt(rating.dataset.rating ?? 0);

  const render = n => stars.forEach((s, i) => s.classList.toggle('lit', i < n));
  render(current);

  stars.forEach((star, i) => {
    star.addEventListener('mouseenter', () => render(i + 1));
    star.addEventListener('mouseleave', () => render(current));
    star.addEventListener('click', () => {
      current = i + 1;
      rating.dataset.rating = current;
      render(current);
      if (valEl) valEl.textContent = `${current}/5`;
    });
  });
});

/* ── Number stepper ───────────────────────────────────────
   <div class="num-stepper">
     <button class="num-btn dec">−</button>
     <input class="num-input" type="number" min="1" max="64" value="8">
     <button class="num-btn inc">+</button>            */
document.querySelectorAll('.num-stepper').forEach(stepper => {
  const input = stepper.querySelector('.num-input');
  if (!input) return;
  const min = parseFloat(input.min ?? -Infinity);
  const max = parseFloat(input.max ??  Infinity);
  stepper.querySelector('.num-btn.dec')?.addEventListener('click', () => {
    input.value = Math.max(min, parseFloat(input.value) - 1);
    input.dispatchEvent(new Event('change'));
  });
  stepper.querySelector('.num-btn.inc')?.addEventListener('click', () => {
    input.value = Math.min(max, parseFloat(input.value) + 1);
    input.dispatchEvent(new Event('change'));
  });
});

/* ── Tag input ────────────────────────────────────────────
   <div class="tag-input-wrap">
     <span class="tag-item">tag<button class="tag-rm">✕</button></span>
     <input class="tag-field" type="text" placeholder="…"> */
document.querySelectorAll('.tag-input-wrap').forEach(wrap => {
  const field = wrap.querySelector('.tag-field');
  if (!field) return;
  wrap.addEventListener('click', () => field.focus());
  wrap.querySelectorAll('.tag-rm').forEach(btn =>
    btn.addEventListener('click', e => { e.stopPropagation(); btn.closest('.tag-item').remove(); })
  );
  function addTag(val) {
    val = val.trim().replace(/,/g, '');
    if (!val) return;
    const tag = document.createElement('span');
    tag.className = 'tag-item';
    tag.innerHTML = `${val}<button class="tag-rm">&#10005;</button>`;
    tag.querySelector('.tag-rm').addEventListener('click', e => { e.stopPropagation(); tag.remove(); });
    wrap.insertBefore(tag, field);
    field.value = '';
  }
  field.addEventListener('keydown', e => {
    if ((e.key === 'Enter' || e.key === ',') && field.value.trim()) {
      e.preventDefault(); addTag(field.value);
    }
    if (e.key === 'Backspace' && !field.value)
      wrap.querySelector('.tag-item:last-of-type')?.remove();
  });
});

/* ── Color swatches ───────────────────────────────────────
   <div class="color-picker-wrap">
     <div class="swatch-row">
       <div class="swatch [active]" data-color="#8B5CF6"
            style="background:#8B5CF6"></div>…
     </div>
     <div class="swatch-preview">
       <div class="swatch-dot"></div>
       <span class="swatch-hex"></span>               */
document.querySelectorAll('.color-picker-wrap').forEach(picker => {
  const preview = picker.querySelector('.swatch-preview');
  picker.querySelectorAll('.swatch-row').forEach(row => {
    row.querySelectorAll('.swatch').forEach(sw => {
      sw.addEventListener('click', () => {
        row.querySelectorAll('.swatch').forEach(s => s.classList.remove('active'));
        sw.classList.add('active');
        if (preview) {
          preview.querySelector('.swatch-dot').style.background = sw.dataset.color;
          preview.querySelector('.swatch-hex').textContent = sw.dataset.color;
        }
      });
    });
  });
});

/* ── Dropzone ─────────────────────────────────────────────
   <div class="dropzone">
     <input type="file" multiple style="display:none">…
   </div>
   <div class="dz-file-list"></div>                     */
document.querySelectorAll('.dropzone').forEach(dz => {
  const fileInput = dz.querySelector('input[type=file]');
  const list = dz.parentElement?.querySelector('.dz-file-list');
  dz.addEventListener('click', () => fileInput?.click());
  ['dragover', 'dragenter'].forEach(ev =>
    dz.addEventListener(ev, e => { e.preventDefault(); dz.classList.add('drag-over'); })
  );
  ['dragleave', 'drop'].forEach(ev =>
    dz.addEventListener(ev, e => { e.preventDefault(); dz.classList.remove('drag-over'); })
  );
  dz.addEventListener('drop', e => handleFiles(e.dataTransfer.files));
  fileInput?.addEventListener('change', e => handleFiles(e.target.files));
  function handleFiles(files) {
    if (!list) return;
    [...files].forEach(f => {
      const row = document.createElement('div');
      row.className = 'dz-file';
      row.innerHTML = `<span class="dz-file-name">${f.name}</span>
        <span class="dz-file-size">${(f.size / 1024).toFixed(1)} KB</span>
        <button class="dz-file-rm">&#10005;</button>`;
      row.querySelector('.dz-file-rm').addEventListener('click', () => row.remove());
      list.appendChild(row);
    });
  }
});

/* ── Sortable table ───────────────────────────────────────
   <table class="sortable">
     <thead><tr><th class="sortable">Col</th>…        */
document.querySelectorAll('table.sortable').forEach(table => {
  table.querySelectorAll('th.sortable').forEach((th, colIdx) => {
    th.addEventListener('click', () => {
      const asc = th.classList.contains('sort-asc');
      table.querySelectorAll('th').forEach(h => h.classList.remove('sort-asc', 'sort-desc'));
      th.classList.add(asc ? 'sort-desc' : 'sort-asc');
      const tbody = table.querySelector('tbody');
      [...tbody.querySelectorAll('tr')]
        .sort((a, b) => {
          const aT  = a.cells[colIdx]?.textContent.trim() ?? '';
          const bT  = b.cells[colIdx]?.textContent.trim() ?? '';
          const n   = parseFloat(aT) - parseFloat(bT);
          const cmp = isNaN(n) ? aT.localeCompare(bT, 'fr') : n;
          return asc ? -cmp : cmp;
        })
        .forEach(r => tbody.appendChild(r));
    });
  });
});
