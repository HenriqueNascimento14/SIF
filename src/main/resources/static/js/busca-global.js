(() => {
  const raiz = document.querySelector("[data-global-search]");
  if (!raiz) return;

  const campo = raiz.querySelector("[data-global-search-input]");
  const painel = raiz.querySelector("[data-global-search-panel]");
  const atalho = raiz.querySelector("[data-global-search-kbd]");
  const isMac = /Mac|iPhone|iPad|iPod/.test(navigator.platform || navigator.userAgent);

  if (atalho) {
    atalho.textContent = isMac ? "⌘K" : "Ctrl K";
  }

  let espera;
  let requisicao;
  let indiceAtivo = -1;
  let sugestoes = [];

  function abrirPainel() {
    painel.hidden = false;
    campo.setAttribute("aria-expanded", "true");
  }

  function fecharPainel() {
    painel.hidden = true;
    campo.setAttribute("aria-expanded", "false");
    indiceAtivo = -1;
  }

  function escapar(texto) {
    return String(texto ?? "")
      .replaceAll("&", "&amp;")
      .replaceAll("<", "&lt;")
      .replaceAll(">", "&gt;")
      .replaceAll('"', "&quot;");
  }

  function renderizar() {
    if (!campo.value.trim()) {
      painel.innerHTML = `<p class="global-search__hint">Digite para buscar alunos, cursos, disciplinas, professores e produtos.</p>`;
      abrirPainel();
      return;
    }

    if (!sugestoes.length) {
      painel.innerHTML = `<p class="global-search__empty">Nenhum resultado para “${escapar(campo.value.trim())}”.</p>`;
      abrirPainel();
      return;
    }

    painel.innerHTML = sugestoes
      .map(
        (item, indice) => `
        <a
          href="${escapar(item.url)}"
          class="global-search__item${indice === indiceAtivo ? " is-active" : ""}"
          role="option"
          data-index="${indice}"
          aria-selected="${indice === indiceAtivo}"
        >
          <span class="global-search__text">
            <span class="global-search__title">${escapar(item.titulo)}</span>
            ${item.subtitulo ? `<span class="global-search__subtitle">${escapar(item.subtitulo)}</span>` : ""}
          </span>
          <span class="global-search__type">${escapar(item.tipo)}</span>
        </a>`
      )
      .join("");
    abrirPainel();
  }

  async function buscar() {
    const q = campo.value.trim();
    if (!q) {
      sugestoes = [];
      renderizar();
      return;
    }

    requisicao?.abort();
    const atual = new AbortController();
    requisicao = atual;
    painel.setAttribute("aria-busy", "true");

    try {
      const resposta = await fetch(`/api/busca?q=${encodeURIComponent(q)}`, {
        signal: atual.signal,
        headers: { Accept: "application/json" },
      });
      if (!resposta.ok) throw new Error("Falha na busca");
      sugestoes = await resposta.json();
      if (atual.signal.aborted) return;
      indiceAtivo = sugestoes.length ? 0 : -1;
      renderizar();
    } catch (erro) {
      if (erro.name === "AbortError") return;
      painel.innerHTML = `<p class="global-search__empty">Não foi possível buscar agora. Tente de novo.</p>`;
      abrirPainel();
    } finally {
      if (requisicao === atual) painel.removeAttribute("aria-busy");
    }
  }

  function agendarBusca() {
    clearTimeout(espera);
    espera = setTimeout(buscar, 220);
  }

  function irParaAtivo() {
    if (indiceAtivo < 0 || !sugestoes[indiceAtivo]) return;
    window.location.assign(sugestoes[indiceAtivo].url);
  }

  campo.addEventListener("focus", () => {
    if (campo.value.trim() || !painel.hidden) renderizar();
    else {
      sugestoes = [];
      renderizar();
    }
  });

  campo.addEventListener("input", (evento) => {
    if (!evento.isComposing) agendarBusca();
  });

  campo.addEventListener("keydown", (evento) => {
    if (evento.key === "ArrowDown") {
      evento.preventDefault();
      if (!sugestoes.length) return;
      indiceAtivo = (indiceAtivo + 1) % sugestoes.length;
      renderizar();
    } else if (evento.key === "ArrowUp") {
      evento.preventDefault();
      if (!sugestoes.length) return;
      indiceAtivo = (indiceAtivo - 1 + sugestoes.length) % sugestoes.length;
      renderizar();
    } else if (evento.key === "Enter") {
      if (indiceAtivo >= 0) {
        evento.preventDefault();
        irParaAtivo();
      }
    } else if (evento.key === "Escape") {
      fecharPainel();
      campo.blur();
    }
  });

  painel.addEventListener("mousemove", (evento) => {
    const item = evento.target.closest("[data-index]");
    if (!item) return;
    const indice = Number(item.dataset.index);
    if (Number.isNaN(indice) || indice === indiceAtivo) return;
    indiceAtivo = indice;
    painel.querySelectorAll(".global-search__item").forEach((el, i) => {
      el.classList.toggle("is-active", i === indiceAtivo);
      el.setAttribute("aria-selected", String(i === indiceAtivo));
    });
  });

  document.addEventListener("keydown", (evento) => {
    const meta = isMac ? evento.metaKey : evento.ctrlKey;
    if (meta && evento.key.toLowerCase() === "k") {
      evento.preventDefault();
      campo.focus();
      campo.select();
    }
  });

  document.addEventListener("pointerdown", (evento) => {
    if (!raiz.contains(evento.target)) fecharPainel();
  });
})();
