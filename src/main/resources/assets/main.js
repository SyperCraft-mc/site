/* ── CUSTOM CURSOR ── */
(function initCursor() {
  const cur  = document.getElementById('cur');
  const ring = document.getElementById('ring');
  if (!cur || !ring) return;

  let mx=0, my=0, rx=0, ry=0;
  document.addEventListener('mousemove', e => { mx = e.clientX; my = e.clientY; });

  (function tick() {
    rx += (mx - rx) * .12;
    ry += (my - ry) * .12;
    cur.style.left  = mx + 'px';
    cur.style.top   = my + 'px';
    ring.style.left = rx + 'px';
    ring.style.top  = ry + 'px';
    requestAnimationFrame(tick);
  })();

  document.querySelectorAll('a, button, .fc, .rc, .lb-row, .wc, .post-row').forEach(el => {
    el.addEventListener('mouseenter', () => ring.classList.add('big'));
    el.addEventListener('mouseleave', () => ring.classList.remove('big'));
  });
})();

/* ── EMBER PARTICLES ── */
(function initEmbers() {
  const cv = document.getElementById('ec');
  if (!cv) return;
  const cx = cv.getContext('2d');
  let W, H;

  function rsz() { W = cv.width = innerWidth; H = cv.height = innerHeight; }
  rsz();
  addEventListener('resize', rsz);

  class Ember {
    constructor() { this.reset(); this.y = Math.random() * H; }
    reset() {
      this.x    = Math.random() * W;
      this.y    = H + 8;
      this.vx   = (Math.random() - .5) * .45;
      this.vy   = -(Math.random() * .8 + .25);
      this.r    = Math.random() * 2.2 + .3;
      this.life = 1;
      this.d    = Math.random() * .0025 + .0012;
    }
    draw() {
      cx.globalAlpha  = this.life * .6;
      cx.shadowBlur   = this.r > 1.5 ? 7 : 3;
      cx.shadowColor  = this.r > 1.5 ? 'rgba(249,115,22,.5)' : 'rgba(160,72,24,.35)';
      cx.fillStyle    = this.r > 1.5 ? '#f97316' : '#a04818';
      cx.beginPath();
      cx.arc(this.x, this.y, this.r, 0, Math.PI * 2);
      cx.fill();
      cx.shadowBlur = 0;
    }
    update() {
      this.x   += this.vx;
      this.y   += this.vy;
      this.vx  += (Math.random() - .5) * .03;
      this.life -= this.d;
      if (this.life <= 0 || this.y < -10) this.reset();
    }
  }

  const embers = Array.from({length: 60}, () => new Ember());
  (function loop() {
    cx.clearRect(0, 0, W, H);
    embers.forEach(e => { e.update(); e.draw(); });
    cx.globalAlpha = 1;
    requestAnimationFrame(loop);
  })();
})();

/* ── SCROLL REVEAL ── */
(function initReveal() {
  document.querySelectorAll('.reveal').forEach(el =>
      new IntersectionObserver(([e]) => { if (e.isIntersecting) el.classList.add('on'); }, { threshold: .06 }).observe(el)
  );
})();

/* ── NAV SCROLL STATE ── */
(function initNavScroll() {
  const nav = document.getElementById('nav');
  if (!nav) return;
  addEventListener('scroll', () => nav.classList.toggle('scrolled', scrollY > 30));
})();

/* ── COPY IP ── */
function copyIP() {
  navigator.clipboard.writeText('play.sypercraft.fr').catch(() => {});
  const t = document.getElementById('toast');
  if (t) { t.classList.add('show'); setTimeout(() => t.classList.remove('show'), 2200); }
}

/* ── TOAST ── */
function showToast(msg) {
  const t = document.getElementById('toast');
  if (!t) return;
  t.textContent = msg;
  t.classList.add('show');
  setTimeout(() => t.classList.remove('show'), 2200);
}

/* ── GENERIC TABS ── */
function switchTab(id, btn, prefix = 't-') {
  document.querySelectorAll('[id^="' + prefix + '"]').forEach(el => el.classList.add('hidden'));
  document.getElementById(prefix + id).classList.remove('hidden');
  document.querySelectorAll('.tbtn').forEach(b => b.classList.remove('on'));
  btn.classList.add('on');
}

/* ── USER DROPDOWN ── */
function toggleUserMenu() {
  const u = document.getElementById('navUser');
  if (!u) return;
  u.classList.toggle('open');
}

document.addEventListener('click', function(e) {
  const u = document.getElementById('navUser');
  if (u && !u.contains(e.target)) u.classList.remove('open');
});


/* AUTO ACTIVE NAV LINK */
(function setActiveNav() {
  const path = location.pathname;
  document.querySelectorAll('.nav-links a').forEach(a => {
    const href = a.getAttribute('href');
    if (!href) return;
    if (href === path || (path === '/' && href === '/') || (href !== '/' && path.startsWith(href))) {
      a.classList.add('active');
    }
  });
})();


/* Cookie banner */
(function() {
  const banner = document.getElementById('cookie-banner');
  const consent = localStorage.getItem('cookie-consent');
  if (!consent) banner.style.display = 'block';

  document.getElementById('cookie-accept').addEventListener('click', function() {
    localStorage.setItem('cookie-consent', 'accepted');
    banner.style.opacity = '0';
    banner.style.transition = 'opacity .2s';
    setTimeout(() => banner.style.display = 'none', 200);
  });

  document.getElementById('cookie-refuse').addEventListener('click', function() {
    localStorage.setItem('cookie-consent', 'refused');
    banner.style.opacity = '0';
    banner.style.transition = 'opacity .2s';
    setTimeout(() => banner.style.display = 'none', 200);
  });
})();
