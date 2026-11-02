package br.com.fiap.clyvo_java.model.pet.saude;

import java.time.LocalDate;

public class LembreteVacina {

	private String nomeAnimal;
	private String nomeVacina;
	private int dosesAplicadas;
	private int dosesTotais;
	private LocalDate proximaData;
	private boolean atrasada;

	public LembreteVacina(String nomeAnimal, String nomeVacina, int dosesAplicadas, int dosesTotais,
			LocalDate proximaData, boolean atrasada) {
		this.nomeAnimal = nomeAnimal;
		this.nomeVacina = nomeVacina;
		this.dosesAplicadas = dosesAplicadas;
		this.dosesTotais = dosesTotais;
		this.proximaData = proximaData;
		this.atrasada = atrasada;
	}

	public int getDosesRestantes() {
		return dosesTotais - dosesAplicadas;
	}

	public String getNomeAnimal() {
		return nomeAnimal;
	}

	public String getNomeVacina() {
		return nomeVacina;
	}

	public int getDosesAplicadas() {
		return dosesAplicadas;
	}

	public int getDosesTotais() {
		return dosesTotais;
	}

	public LocalDate getProximaData() {
		return proximaData;
	}

	public boolean isAtrasada() {
		return atrasada;
	}

}