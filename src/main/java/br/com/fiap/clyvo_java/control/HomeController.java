package br.com.fiap.clyvo_java.control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.fiap.clyvo_java.model.pet.saude.LembreteVacina;
import br.com.fiap.clyvo_java.model.pet.saude.VacinaAnimal;
import br.com.fiap.clyvo_java.model.usuario.Usuario;
import br.com.fiap.clyvo_java.repository.pet.consultas.ConsultaRepository;
import br.com.fiap.clyvo_java.repository.pet.saude.VacinaAnimalRepository;
import br.com.fiap.clyvo_java.repository.usuario.UsuarioRepository;

@Controller
public class HomeController {

	@Autowired
	private UsuarioRepository repU;

	@Autowired
	private ConsultaRepository repC;

	@Autowired
	private VacinaAnimalRepository repVA;

	@GetMapping("/home")
	public ModelAndView popularHomePage() {
		ModelAndView mv = new ModelAndView("home/index");

		Authentication autenticado = SecurityContextHolder.getContext().getAuthentication();

		Optional<Usuario> op = repU.findByUsername(autenticado.getName());

		if (op.isPresent()) {
			mv.addObject("usuario", op.get());
		}

		LocalDate hoje = LocalDate.now();
		mv.addObject("consultas_proximas", repC.buscarConsultasPorPeriodo(hoje, hoje.plusDays(7)));
		mv.addObject("lembretes_vacinas", montarLembretesVacinas(hoje));

		return mv;
	}

	private List<LembreteVacina> montarLembretesVacinas(LocalDate hoje) {
		List<LembreteVacina> lembretes = new ArrayList<>();

		for (List<VacinaAnimal> aplicacoes : agruparPorAnimalEVacina().values()) {
			VacinaAnimal ultima = aplicacoes.get(0);
			int aplicadas = aplicacoes.size();

			if (aplicadas < ultima.getNum_doses()) {
				LocalDate proximaData = ultima.getDt_receita().plusDays(ultima.getFrequencia_aplicacao());
				lembretes.add(new LembreteVacina(ultima.getAnimal().getNm_animal(),
						ultima.getVacina().getNm_vacina(), aplicadas, ultima.getNum_doses(),
						proximaData, proximaData.isBefore(hoje)));
			}
		}

		lembretes.sort(Comparator.comparing(LembreteVacina::getProximaData));
		return lembretes;
	}

	private Map<String, List<VacinaAnimal>> agruparPorAnimalEVacina() {
		Map<String, List<VacinaAnimal>> grupos = new LinkedHashMap<>();

		for (VacinaAnimal aplicacao : repVA.buscarTodasOrdenadasPorData()) {
			if (aplicacao.getAnimal() == null || aplicacao.getVacina() == null) {
				continue;
			}

			String chave = aplicacao.getAnimal().getId_animal() + "-" + aplicacao.getVacina().getId_vacina();

			if (!grupos.containsKey(chave)) {
				grupos.put(chave, new ArrayList<>());
			}
			grupos.get(chave).add(aplicacao);
		}

		return grupos;
	}

}