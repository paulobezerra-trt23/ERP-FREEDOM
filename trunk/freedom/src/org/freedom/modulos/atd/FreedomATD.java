/**
 * @version 07/05/2003 <BR>
 * @author Setpoint Inform�tica Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.atd <BR>
 * Classe:
 * @(#)FreedomATD.java <BR>
 * 
 * Este programa � licenciado de acordo com a LPG-PC (Licen�a P�blica Geral para
 * Programas de Computador), <BR>
 * vers�o 2.1.0 ou qualquer vers�o posterior. <BR>
 * A LPG-PC deve acompanhar todas PUBLICA��ES, DISTRIBUI��ES e REPRODU��ES deste
 * Programa. <BR>
 * Caso uma c�pia da LPG-PC n�o esteja dispon�vel junto com este Programa, voc�
 * pode contatar <BR>
 * o LICENCIADOR ou ent�o pegar uma c�pia em: <BR>
 * Licen�a: http://www.lpg.adv.br/licencas/lpgpc.rtf <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa �
 * preciso estar <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Tela principal do m�dulo de atendimento.
 *  
 */

package org.freedom.modulos.atd;

import java.awt.event.ActionListener;

import org.freedom.funcoes.Funcoes;
import org.freedom.modulos.std.FCliente;
import org.freedom.telas.Aplicativo;

public class FreedomATD extends Aplicativo implements ActionListener {
	public FreedomATD() {
		super("iconAtendimento32.gif", "splashATD.jpg",
				"FreedomATD - M�dulo de atendimento", 1, 4, "freedom.ini", null);

		//Arquivo:
		addOpcao(-1, TP_OPCAO_MENU, "Arquivo", "", 'A', 100000000, 0, false,
				null);

		addOpcao(100000000, TP_OPCAO_MENU, "Tabelas", "", 'T', 100100000, 1,
				false, null);

		addOpcao(100100000, TP_OPCAO_MENU, "Conveniado", "", 'C', 100101000, 2,
				false, null);
		addOpcao(100101000, TP_OPCAO_ITEM, "Tipo de Conveniado",
				"Tipo de conveniados", 'T', 100101010, 3, true, FTipoConv.class);
		addOpcao(100101000, TP_OPCAO_ITEM, "Atribui��es", "Abribui��es", 'A',
				100101020, 3, true, FAtribuicao.class);
		addOpcao(100101000, TP_OPCAO_ITEM, "Encaminhador", "Encaminhador", 'E',
				100101030, 3, true, FEncaminhador.class);
		addOpcao(100101000, TP_OPCAO_ITEM, "Conveniados", "Conveniados", 'C',
				100101040, 3, true, FConveniado.class);

		addOpcao(101000000, TP_OPCAO_MENU, "Atendente", "", 'A', 100102000, 2,
				false, null);
		addOpcao(100102000, TP_OPCAO_ITEM, "Tipo de Atendente",
				"Tipo de atendentes", 'T', 100102010, 3, true, FTipoAtend.class);
		addOpcao(100102000, TP_OPCAO_ITEM, "Atendentes", "Atendentes", 'A',
				100102020, 3, true, FAtendente.class);

		addOpcao(100100000, TP_OPCAO_MENU, "Atendimento", "", 't', 100103000,
				2, false, null);
		addOpcao(100103000, TP_OPCAO_ITEM, "Setor de Atendimento",
				"Setor de Atendimento", 'S', 100103010, 3, true,
				FSetorAtend.class);
		addSeparador(100103000);
		addOpcao(100103000, TP_OPCAO_ITEM, "Atendente", "Atendente", 'e',
				100103020, 1, true, FAtendente.class);
		addOpcao(100103000, TP_OPCAO_ITEM, "Tipo de Atendente",
				"Tipo de Atendente", 'i', 100103030, 1, true, FTipoAtend.class);

		addSeparador(100100000);
		addOpcao(100100000, TP_OPCAO_ITEM, "Clientes", "Clientes", 'l',
				100104000, 2, true, FCliente.class);
		addSeparador(100100000);

		addOpcao(100000000, TP_OPCAO_MENU, "Prefer�ncias", "", 'P', 100200000,
				1, false, null);

		addOpcao(100200000, TP_OPCAO_ITEM, "Prefer�ncias Gerais...",
				"Pref. Gerais", 'P', 100201000, 2, true, FPrefereAtend.class);

		//Atendimento:
		addOpcao(-1, TP_OPCAO_MENU, "Atendimento", "", 't', 110000000, 0,
				false, null);
		addOpcao(110000000, TP_OPCAO_ITEM, "Atendimento", "Atendimento", 'A',
				110100000, 1, true, FAtendimento.class);
		addSeparador(110000000);
		addOpcao(110000000, TP_OPCAO_ITEM, "Tipos de Atendimento",
				"Tipo de atendimento", 'T', 110200000, 1, true,
				FTipoAtendo.class);

		//Or�amento:
		addOpcao(-1, TP_OPCAO_MENU, "Or�amento", "", 'O', 1200000000, 0, false,
				null);
		addOpcao(1200000000, TP_OPCAO_ITEM, "Or�amento", "Orcamento", 'O',
				120100000, 1, true, FOrcamento.class);
		addSeparador(1200000000);
		addOpcao(1200000000, TP_OPCAO_ITEM, "Pesquisa Or�amentos",
				"Pesquisa Orcamentos", 'P', 120200000, 1, true, FConsOrc.class);
		addOpcao(1200000000, TP_OPCAO_ITEM, "Aprova Or�amentos",
				"Aprova Orcamento", 'A', 120300000, 1, true, FAprovaOrc.class);
		addOpcao(1200000000, TP_OPCAO_ITEM, "Pesquisa Autoriza��es",
				"Pesquisa Autoriza��o", 'e', 120400000, 1, true,
				FConsAutoriz.class);

		addBotao("btAtendimento.gif", "Atendimento", "Atendimento", 110100000,
				FAtendimento.class);
		addBotao("btOrcamento.gif", "Or�amento", "Orcamento", 120100000,
				FOrcamento.class);
		addBotao("btConsOrcamento.gif", "Pesquisa Or�amento",
				"Pesquisa Orcamentos", 120200000, FConsOrc.class);
		addBotao("barraConveniados.gif", "Conveniados", "Conveniados",
				100101040, FConveniado.class);
		addBotao("btAprovaOrc.gif", "Aprova��es de Or�amantos",
				"Aprova Orcamento", 120300000, FAprovaOrc.class);

		ajustaMenu();

	}

	public static void main(String sParams[]) {
		try {
			FreedomATD freedomatd = new FreedomATD();
			freedomatd.show();
		} catch (Throwable e) {
			Funcoes.criaTelaErro("Erro de execu��o");
			e.printStackTrace();
		}
	}
}