const formulario = document.querySelector("[data-search-form]");
const campoBusca = formulario.querySelector("input[type=search]");
const statusBusca = document.querySelector("[data-search-status]");
let resultados = document.querySelector("[data-search-results]");
let espera;
let requisicao;

function agendarBusca(atraso) {
  clearTimeout(espera);
  requisicao?.abort();
  espera = setTimeout(atualizarResultados, atraso);
}

async function atualizarResultados() {
  const destino = new URL(formulario.action);
  for (const [chave, valor] of new FormData(formulario)) {
    if (valor.trim()) destino.searchParams.append(chave, valor);
  }

  const atual = new AbortController();
  requisicao = atual;
  resultados.setAttribute("aria-busy", "true");
  try {
    const resposta = await fetch(destino, { signal: atual.signal });
    if (resposta.redirected) {
      window.location.assign(resposta.url);
      return;
    }
    if (!resposta.ok) throw new Error("Falha na busca");
    const documento = new DOMParser().parseFromString(await resposta.text(), "text/html");
    if (atual.signal.aborted) return;
    const novaTabela = documento.querySelector("[data-search-results]");
    const novoStatus = documento.querySelector("[data-search-status]");
    if (!novaTabela || !novoStatus) throw new Error("Resposta sem resultados");
    resultados.replaceWith(novaTabela);
    resultados = novaTabela;
    statusBusca.textContent = novoStatus.textContent;
    history.replaceState(null, "", destino);
  } catch (erro) {
    if (erro.name !== "AbortError") {
      statusBusca.textContent = "Não foi possível atualizar os resultados. Selecione Buscar para tentar de novo.";
    }
  } finally {
    if (requisicao === atual) resultados.removeAttribute("aria-busy");
  }
}

campoBusca.addEventListener("input", (evento) => {
  if (!evento.isComposing) agendarBusca(300);
});
formulario.querySelectorAll("select").forEach((filtro) => {
  filtro.addEventListener("change", () => agendarBusca(0));
});
