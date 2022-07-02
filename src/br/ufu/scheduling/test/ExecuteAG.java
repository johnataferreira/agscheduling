package br.ufu.scheduling.test;

import br.ufu.scheduling.ag.AGScheduling;

public class ExecuteAG {

	public static void main(String[] args) {
		long initialTime = System.currentTimeMillis();

		try {
			AGScheduling scheduling = new AGScheduling();
			scheduling.execute(initialTime);
		} catch (Exception e) {
			e.printStackTrace();

			System.out.println("Erro: " + e);
			System.out.println("Tempo de execução com erro: " + ((double) (System.currentTimeMillis() - initialTime) / 1000) + " segundos.");
		}
	}
}