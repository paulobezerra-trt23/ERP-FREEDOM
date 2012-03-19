/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Inform�tica Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.std <BR>
 * Classe:
 * @(#)FOrcamento.java <BR>
 * 
 * Este programa � licenciado de acordo com a LPG-PC (Licen�a P�blica Geral para Programas de Computador), <BR>
 * vers�o 2.1.0 ou qualquer vers�o posterior. <BR>
 * A LPG-PC deve acompanhar todas PUBLICA��ES, DISTRIBUI��ES e REPRODU��ES deste Programa. <BR>
 * Caso uma c�pia da LPG-PC n�o esteja dispon�vel junto com este Programa, voc� pode contatar <BR>
 * o LICENCIADOR ou ent�o pegar uma c�pia em: <BR>
 * Licen�a: http://www.lpg.adv.br/licencas/lpgpc.rtf <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa � preciso estar <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Tela de or�amento, tela respons�vel pela inser��o e edi��o de or�amentos por cliente <BR>
 * diferente da tela de or�amento do atendimento que � por conveniado. <BR>
 * 
 */

package org.freedom.modulos.std;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.DeleteEvent;
import org.freedom.acao.DeleteListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.componentes.GuardaCampo;
import org.freedom.componentes.ImprimeOS;
import org.freedom.componentes.JLabelPad;
import org.freedom.componentes.JPanelPad;
import org.freedom.componentes.JTextAreaPad;
import org.freedom.componentes.JTextFieldFK;
import org.freedom.componentes.JTextFieldPad;
import org.freedom.componentes.ListaCampos;
import org.freedom.funcoes.EmailBean;
import org.freedom.funcoes.Funcoes;
import org.freedom.layout.componentes.LeiauteGR;
import org.freedom.telas.Aplicativo;
import org.freedom.telas.FPrinterJob;

public class FOrcamento extends FVD implements PostListener, CarregaListener, FocusListener, ActionListener, 
   InsertListener, DeleteListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JPanelPad pinTot = new JPanelPad( 200, 200 );

	private JPanelPad pnTot = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad pnCenter = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JButton btObs = new JButton( Icone.novo( "btObs.gif" ) );

	private JButton btOrc = new JButton( Icone.novo( "btImprimeOrc.gif" ) );

	private JButton btOrcTst = new JButton( Icone.novo( "btFisio.gif" ) );

	private JButton btOrcTst2 = new JButton( Icone.novo( "btEmprestimo.gif" ) );
	
	private JButton btFechaOrc = new JButton( Icone.novo( "btOk.gif" ) );

	private JButton btExp = new JButton( Icone.novo( "btExportar.gif" ) );

	private JTextFieldPad txtCodOrc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDtOrc = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtVencOrc = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodConv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCodTpConv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescTipoConv = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldFK txtDescConv = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtCodEnc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtEstCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtCodItOrc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtQtdItOrc = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 15, 2 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodBarras = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCLoteProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtPrecoItOrc = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 15, 2 );

	private JTextFieldPad txtPercDescItOrc = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 6, 2 );

	private JTextFieldPad txtVlrDescItOrc = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 15, 2 );

	private JTextFieldPad txtVlrLiqItOrc = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 15, 2 );

	private JTextFieldPad txtVlrEdDescOrc = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 15, 2 );

	private JTextFieldPad txtVlrEdAdicOrc = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 15, 2 );

	private JTextFieldPad txtPercDescOrc = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 6, 2 );

	private JTextFieldPad txtVlrDescOrc = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 15, 2 );

	private JTextFieldPad txtPercAdicOrc = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 6, 2 );

	private JTextFieldPad txtVlrAdicOrc = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 15, 2 );

	private JTextFieldPad txtVlrLiqOrc = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 15, 2 );

	private JTextFieldPad txtVlrProdItOrc = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 15, 2 );

	private JTextFieldPad txtStrDescItOrc = new JTextFieldPad( JTextFieldPad.TP_STRING, 500, 0 );

	private JTextFieldPad txtVlrProdOrc = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 15, 2 );

	private JTextFieldPad txtStatusOrc = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtCodTpCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodClComiss = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtPrazoEntOrc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodAlmoxItOrc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtCodEmpLG = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodFilialLG = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodLog = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtTxt01 = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodLote = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldFK txtNomeEnc = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtNomeCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescTipoCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescAlmoxItOrc = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescClComiss = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtSldLiqProd = new JTextFieldFK( JTextFieldPad.TP_NUMERIC, 15, casasDec );

	private JTextFieldFK txtDescLote = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescAtend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextAreaPad txaObsItOrc = new JTextAreaPad( 500 );

	private ListaCampos lcCli = new ListaCampos( this, "CL" );
	
	private ListaCampos lcEnc = new ListaCampos( this, "EC" );

	private ListaCampos lcProd = new ListaCampos( this, "PD" );

	private ListaCampos lcProd2 = new ListaCampos( this, "PD" );

	private ListaCampos lcOrc2 = new ListaCampos( this );

	private ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );

	private ListaCampos lcVend = new ListaCampos( this, "VD" );
	
	private ListaCampos lcConv = new ListaCampos( this, "CV" );
	
	private ListaCampos lcTipoConv = new ListaCampos( this, "TC" );

	private ListaCampos lcTipoCli = new ListaCampos( this, "TC" );

	private ListaCampos lcAlmox = new ListaCampos( this, "AX" );

	private ListaCampos lcClComiss = new ListaCampos( this, "CM" );

	private ListaCampos lcLote = new ListaCampos( this, "LE" );

	private ListaCampos lcAtend = new ListaCampos( this, "AE" );

	private Vector<Object> vParamOrc = new Vector<Object>();

	private String sOrdNota = "";

	private String sModoNota = "";

	private String oldStatusOrc = null;

	private BigDecimal bdVlrDescItAnt;

	private FPrinterJob dl = null;

	private Object[] oPrefs = null;

	private boolean bCtrl = false;

	private int iCodCliAnt = 0;

	public FOrcamento() {
				
		setTitulo( "Or�amento" );
		setAtribos( 50, 50, 769, 460 );

		txtDescProd.setToolTipText( "Clique aqui duas vezes para alterar a descri��o." );
		txtDescProd.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getClickCount() == 2 )
					mostraTelaDecricao( txaObsItOrc, txtCodProd.getVlrInteger().intValue(), txtDescProd.getVlrString() );
			}
		} );
		setImprimir( true );
	}

	private void montaOrcamento() {

		oPrefs = prefs(); // Carrega as prefer�ncias

		pnMaster.remove( 2 ); // Remove o JPanelPad pr�definido da class FDados
		pnGImp.removeAll(); // Remove os bot�es de impress�o para adicionar logo
		// embaixo
		pnGImp.setLayout( new GridLayout( 1, 5 ) ); // redimensiona o painel de
		// impress�o
		pnGImp.setPreferredSize( new Dimension( 210, 26 ) );
		pnGImp.add( btPrevimp );
		pnGImp.add( btImp );
		pnGImp.add( btFechaOrc );
		pnGImp.add( btObs );// Agora o painel est� maior
		pnGImp.add( btOrc );// Bot�o provis�rio para emiss�o de or�amento padr�o
		
		if(Aplicativo.sNomeModulo.equals( "Atendimento" )){
			pnGImp.add( btOrcTst );// Bot�o para teste de laudo fisioterapia
			pnGImp.add( btOrcTst2 );// Outro bot�o de teste para contrato
		}

		pnTot.setPreferredSize( new Dimension( 120, 200 ) ); // JPanelPad de Totais
		pnTot.add( pinTot );
		pnCenter.add( pnTot, BorderLayout.EAST );
		pnCenter.add( spTab, BorderLayout.CENTER );

		JPanelPad pnLab = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );
		pnLab.add( new JLabelPad( " Totais:" ) ); // Label do painel de totais

		pnMaster.add( pnCenter, BorderLayout.CENTER );

		// FK Tipo de cliente
		lcTipoCli.add( new GuardaCampo( txtCodTpCli, "CodTipoCli", "C�d.cli.", ListaCampos.DB_PK, false ) );
		lcTipoCli.add( new GuardaCampo( txtDescTipoCli, "DescTipoCli", "Raz�o social do cliente", ListaCampos.DB_SI, false ) );
		txtCodTpCli.setTabelaExterna( lcTipoCli );
		txtDescTipoCli.setListaCampos( lcTipoCli );
		lcTipoCli.montaSql( false, "TIPOCli", "VD" );
		lcTipoCli.setQueryCommit( false );
		lcTipoCli.setReadOnly( true );

		// FK Plano de pagamento
		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "C�d.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descri��o do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.setWhereAdic( "ATIVOPLANOPAG='S' AND CVPLANOPAG IN ('V','A')" );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag );
		txtDescPlanoPag.setListaCampos( lcPlanoPag );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );

		// FK Vendedor
		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "C�d.comiss.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		txtCodVend.setTabelaExterna( lcVend );
		txtNomeVend.setListaCampos( lcVend );
		lcVend.montaSql( false, "VENDEDOR", "VD" );
		lcVend.setQueryCommit( false );
		lcVend.setReadOnly( true );

		// FK Classifica��o de comiss�o
		lcClComiss.add( new GuardaCampo( txtCodClComiss, "CodClComis", "C�d.cl.comiss.", ListaCampos.DB_PK, false ) );
		lcClComiss.add( new GuardaCampo( txtDescClComiss, "DescClComis", "Descri��o da class. da comiss�o", ListaCampos.DB_SI, false ) );
		lcClComiss.montaSql( false, "CLCOMIS", "VD" );
		lcClComiss.setQueryCommit( false );
		lcClComiss.setReadOnly( true );
		txtCodClComiss.setTabelaExterna( lcClComiss );
		
		// FK Encaminhador
		lcEnc.add( new GuardaCampo( txtCodEnc, "CodEnc", "C�d.enc.", ListaCampos.DB_PK, false ) );
		lcEnc.add( new GuardaCampo( txtNomeEnc, "NomeEnc", "Descri��o do encaminhador", ListaCampos.DB_SI, false ) );
		txtCodEnc.setTabelaExterna( lcEnc );
		txtNomeEnc.setListaCampos( lcEnc );
		txtCodEnc.setNomeCampo( "CodEnc" );
		lcEnc.montaSql( false, "ENCAMINHADOR", "AT" );
		lcEnc.setQueryCommit( false );
		lcEnc.setReadOnly( true );
				
		// FK Conveniado
		lcConv.add( new GuardaCampo( txtCodConv, "CodConv", "C�d.conv.", ListaCampos.DB_PK, false ) );
		lcConv.add( new GuardaCampo( txtDescConv, "NomeConv", "Nome do coveniado", ListaCampos.DB_SI, false ) );
		lcConv.add( new GuardaCampo( txtCodTpConv, "CodTpConv", "Tipo de Conveniado", ListaCampos.DB_SI, false ) );
		lcConv.add( new GuardaCampo( txtCodEnc, "CodEnc", "Encaminhador", ListaCampos.DB_FK, false ) );
		lcConv.montaSql( false, "CONVENIADO", "AT" );
		lcConv.setQueryCommit( false );
		lcConv.setReadOnly( true );
		txtCodConv.setTabelaExterna( lcConv );

		//FK Tipo de conveniado
		
		lcTipoConv.add( new GuardaCampo( txtCodTpConv, "CodTpConv", "C�d.tp.conv.", ListaCampos.DB_PK, false ) );
		lcTipoConv.add( new GuardaCampo( txtDescTipoConv, "DescTpConv", "Descri��o do tipo de conveniado", ListaCampos.DB_SI, false ) );
		txtCodTpConv.setTabelaExterna( lcTipoConv );
		txtDescTipoConv.setListaCampos( lcTipoConv );
		lcTipoConv.montaSql( false, "TIPOCONV", "AT" );
		lcTipoConv.setQueryCommit( false );
		lcTipoConv.setReadOnly( true );	
		


		// FK Lote
		lcLote.add( new GuardaCampo( txtCodLote, "CodLote", "Lote", ListaCampos.DB_PK, txtDescLote, false ) );
		lcLote.add( new GuardaCampo( txtDescLote, "VenctoLote", "Dt.vencto.", ListaCampos.DB_SI, false ) );
		lcLote.add( new GuardaCampo( txtSldLiqProd, "SldLiqLote", "Saldo", ListaCampos.DB_SI, false ) );
		lcLote.setDinWhereAdic( "CODPROD=#N ", txtCodProd );
		lcLote.montaSql( false, "LOTE", "EQ" );
		lcLote.setQueryCommit( false );
		lcLote.setReadOnly( true );
		txtCodLote.setTabelaExterna( lcLote );
		txtDescLote.setListaCampos( lcLote );
		txtDescLote.setNomeCampo( "VenctoLote" );
		txtDescLote.setLabel( "Vencimento" );

		// FK Cliente
		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "C�d.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Raz�o social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtNomeCli, "NomeCli", "Nome do cliente", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "C�d.p.pag.", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCodVend, "CodVend", "C�d.comiss.", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtEstCli, "UfCli", "UF", ListaCampos.DB_SI, false ) );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setQueryCommit( false );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli );
		txtNomeCli.setSize( 250, 20 );

		// FK de atendente
		
		lcAtend.add( new GuardaCampo( txtCodAtend, "CodAtend", "C�d.atend.", ListaCampos.DB_PK, false ) );
		lcAtend.add( new GuardaCampo( txtDescAtend, "NomeAtend", "Nome do atendente", ListaCampos.DB_SI, false ) );
		txtCodAtend.setTabelaExterna( lcAtend );
		txtDescAtend.setListaCampos( lcAtend );
		lcAtend.montaSql( false, "ATENDENTE", "AT" );
		lcAtend.setQueryCommit( false );
		lcAtend.setReadOnly( true );

		// FK de Almoxarifado

		lcAlmox.add( new GuardaCampo( txtCodAlmoxItOrc, "codalmox", "Cod.Almox.", ListaCampos.DB_PK, false ) );
		lcAlmox.add( new GuardaCampo( txtDescAlmoxItOrc, "DescAlmox", "Descri��o do almoxarifado", ListaCampos.DB_SI, false ) );
		lcAlmox.montaSql( false, "ALMOX", "EQ" );
		lcAlmox.setQueryCommit( false );
		lcAlmox.setReadOnly( true );
		txtCodAlmoxItOrc.setTabelaExterna( lcAlmox );

		// FK Produto
		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "C�d.prod.", ListaCampos.DB_PK, txtDescProd, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descri��o do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Refer�ncia do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodBarras, "CodBarProd", "C�digo de barras", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtSldLiqProd, "SldLiqProd", "Saldo", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodAlmoxItOrc, "CodAlmox", "C�d.almox.", ListaCampos.DB_SI, txtDescAlmoxItOrc, false ) );
		lcProd.add( new GuardaCampo( txtCLoteProd, "CLoteProd", "C/Lote", ListaCampos.DB_SI, false ) );
		lcProd.setWhereAdic( "ATIVOPROD='S'" );
		lcProd.montaSql( false, "PRODUTO", "EQ" );
		lcProd.setQueryCommit( false );
		lcProd.setReadOnly( true );
		txtCodProd.setTabelaExterna( lcProd );

		// FK do produto (*Somente em caso de refer�ncias este listaCampos
		// Trabalha como gatilho para o listaCampos de produtos, assim
		// carregando o c�digo do produto que ser� armazenado no Banco)
		lcProd2.add( new GuardaCampo( txtRefProd, "RefProd", "Ref.prod.", ListaCampos.DB_PK, txtDescProd, false ) );
		lcProd2.add( new GuardaCampo( txtDescProd, "DescProd", "Descri��o do produto", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodProd, "CodProd", "C�d.prod.", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtSldLiqProd, "SldLiqProd", "Saldo", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodAlmoxItOrc, "CodAlmox", "C�d.almox.", ListaCampos.DB_SI, txtDescAlmoxItOrc, false ) );
		lcProd2.add( new GuardaCampo( txtCLoteProd, "CLoteProd", "C/Lote", ListaCampos.DB_SI, false ) );
		txtRefProd.setNomeCampo( "RefProd" );
		txtRefProd.setListaCampos( lcDet );
		lcProd2.setWhereAdic( "ATIVOPROD='S'" );
		lcProd2.montaSql( false, "PRODUTO", "EQ" );
		lcProd2.setQueryCommit( false );
		lcProd2.setReadOnly( true );
		txtRefProd.setTabelaExterna( lcProd2 );

		// ListaCampos de Totais (� acionada pelo listaCampos de Orcamento)

		lcOrc2.add( new GuardaCampo( txtCodOrc, "CodOrc", "C�d.Or�.", ListaCampos.DB_PK, false ) );
		lcOrc2.add( new GuardaCampo( txtPercDescOrc, "PercDescOrc", "% desc.", ListaCampos.DB_SI, false ) );
		lcOrc2.add( new GuardaCampo( txtVlrDescOrc, "VlrDescOrc", "Vlr.desc.", ListaCampos.DB_SI, false ) );
		lcOrc2.add( new GuardaCampo( txtPercAdicOrc, "PercAdicOrc", "% adic.", ListaCampos.DB_SI, false ) );
		lcOrc2.add( new GuardaCampo( txtVlrAdicOrc, "VlrAdicOrc", "Vlr.adic.", ListaCampos.DB_SI, false ) );
		lcOrc2.add( new GuardaCampo( txtVlrLiqOrc, "VlrLiqOrc", "Vlr.total", ListaCampos.DB_SI, false ) );
		lcOrc2.add( new GuardaCampo( txtVlrProdOrc, "VlrProdOrc", "Vlr.parcial", ListaCampos.DB_SI, false ) );
		lcOrc2.montaSql( false, "ORCAMENTO", "VD" );
		lcOrc2.setQueryCommit( false );
		lcOrc2.setReadOnly( true );

		// Coloca os coment�rio nos bot�es

		btFechaOrc.setToolTipText( "Completar o Or�amento (F4)" );
		btObs.setToolTipText( "Observa��es (Ctrl + O)" );
		btOrc.setToolTipText( "Imprime or�amento padr�o" );
		btOrcTst.setToolTipText( "Imprime or�amento assinado" );
		btOrcTst2.setToolTipText( "Imprime contrato de loca��o" );

		// Desativa as os TextFields para que os usu�rios n�o fussem

		txtVlrDescOrc.setAtivo( false );
		txtVlrAdicOrc.setAtivo( false );
		txtVlrLiqOrc.setAtivo( false );

		// Adiciona os componentes na tela e no ListaCompos da orcamento
		pinCab = new JPanelPad( 740, 180 );
		setListaCampos( lcCampos );
		setPainel( pinCab, pnCliCab );
		adicCampo( txtCodOrc, 7, 20, 90, 20, "CodOrc", "N� or�amento", ListaCampos.DB_PK, true );
		adicCampo( txtDtOrc, 440, 20, 107, 20, "DtOrc", "Data", ListaCampos.DB_SI, true );

		if(Aplicativo.sNomeModulo.equals( "Atendimento" )){
			setAltCab( 170 );
			adicCampoInvisivel( txtCodCli, "CodCli", "C�d.cli.", ListaCampos.DB_FK, txtRazCli, false );			
			adicDescFK( txtRazCli, 7, 100, 345, 20, "RazCli", "Raz�o social do cliente" );
			adicCampo( txtCodConv, 100, 20, 87, 20, "CodConv", "C�d.conv.", ListaCampos.DB_FK, txtDescConv, true );
			adicDescFK( txtDescConv, 190, 20, 247, 20, "NomeConv", "Nome do conveniado" );
			adicCampo( txtCodVend, 7, 60, 90, 20, "CodVend", "C�d.comiss.", ListaCampos.DB_FK, txtNomeVend, true );
			adicDescFK( txtNomeVend, 100, 60, 250, 20, "NomeVend", "Nome do comissionado" );
			adicDescFK( txtDescTipoConv, 456, 60, 283, 20, "DescTpConv", "Tipo de conveniado" );
			adicDescFK( txtNomeEnc, 355, 100, 383, 20, "NomeEnc", "Org.Encaminhador" );			
			if ( !oPrefs[ PrefOrc.TITORCTXT01.ordinal() ].equals( "" ) )
				adicCampo( txtTxt01, 353, 60, 100, 20, "Txt01", oPrefs[ PrefOrc.TITORCTXT01.ordinal() ].toString().trim(), ListaCampos.DB_SI, false );
			adicCampoInvisivel( txtCodTpConv, "CodTpConv", "C�d.tp.conv.", ListaCampos.DB_FK, txtDescTipoConv, false );
			adicCampoInvisivel( txtCodPlanoPag, "CodPlanoPag", "C�d.p.pg.", ListaCampos.DB_FK, txtDescPlanoPag, true );
			adicCampoInvisivel( txtCodAtend, "CodAtend", "Plano atendente.", ListaCampos.DB_FK, txtDescAtend, false );

		}
		else {
			setAltCab( 130 );
			adicCampo( txtCodCli, 100, 20, 87, 20, "CodCli", "C�d.cli.", ListaCampos.DB_FK, txtRazCli, true );
			adicDescFK( txtRazCli, 190, 20, 247, 20, "RazCli", "Raz�o social do cliente" );
			adicDescFK( txtDescTipoCli, 270, 60, 147, 20, "DescTipoCli", "Desc. do tipo de cliente" );
			adicCampo( txtCodPlanoPag, 420, 60, 77, 20, "CodPlanoPag", "C�d.p.pg.", ListaCampos.DB_FK, txtDescPlanoPag, true );
			adicDescFK( txtDescPlanoPag, 500, 60, 240, 20, "DescPlanoPag", "Descri��o do plano de pagamento" );
			adicCampo( txtCodVend, 7, 60, 90, 20, "CodVend", "C�d.comiss.", ListaCampos.DB_FK, txtNomeVend, true );
			adicDescFK( txtNomeVend, 100, 60, 167, 20, "NomeVend", "Nome do comissionado" );	
			adicCampoInvisivel( txtCodClComiss, "CodClComis", "C�d.cl.comiss.", ListaCampos.DB_FK, txtDescClComiss, false );
		}
		
		adicCampo( txtDtVencOrc, 550, 20, 87, 20, "DtVencOrc", "Dt.valid.", ListaCampos.DB_SI, true );
		adicCampo( txtPrazoEntOrc, 640, 20, 100, 20, "PrazoEntOrc", "Dias p/ entrega", ListaCampos.DB_SI, false );
		
		adicCampoInvisivel( txtPercDescOrc, "PercDescOrc", "% desc.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtVlrDescOrc, "VlrDescOrc", "Vlr.desc.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtPercAdicOrc, "PercAdicOrc", "% adic.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtVlrAdicOrc, "VlrAdicOrc", "Vlr.adic.", ListaCampos.DB_SI, false );

		/*
		 * porque usar estes campos? adicCampoInvisivel(txtVlrEdDescOrc, "VlrDescOrc", "Vlr.desc.",ListaCampos.DB_SI, false); adicCampoInvisivel(txtVlrEdAdicOrc, "VlrAdicOrc", "Vlr.adic.",ListaCampos.DB_SI, false);
		 */

		adicCampoInvisivel( txtStatusOrc, "StatusOrc", "Status", ListaCampos.DB_SI, false );

		setListaCampos( true, "ORCAMENTO", "VD" );

		// pnRodape.add(btExp);
		btExp.setPreferredSize( new Dimension( 30, 30 ) );
		btExp.setToolTipText( "Copia or�amento." );
		pnNavCab.add( btExp, BorderLayout.EAST );

		// adic(btExp, 633, 50, 30, 30);

		txtVlrLiqItOrc.setAtivo( false );

		// Adiciona os Listeners
		btFechaOrc.addActionListener( this );
		btObs.addActionListener( this );
		btOrc.addActionListener( this );
		btOrcTst.addActionListener( this );
		btOrcTst2.addActionListener( this );
		
		btExp.addActionListener( this );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );

		txtRefProd.addKeyListener( this );

		txtPercDescItOrc.addFocusListener( this );
		txtVlrDescItOrc.addFocusListener( this );
		txtQtdItOrc.addFocusListener( this );
		txtPrecoItOrc.addFocusListener( this );

		lcCampos.addCarregaListener( this );
		lcProd2.addCarregaListener( this );
		lcCli.addCarregaListener( this );
		lcProd.addCarregaListener( this );
		lcProd2.addCarregaListener( this );
		lcDet.addCarregaListener( this );
		lcPlanoPag.addCarregaListener( this );
		lcCli.addCarregaListener( this );

		lcCampos.addInsertListener( this );
		lcDet.addInsertListener( this );

		lcDet.addPostListener( this );
		lcCampos.addPostListener( this );

		lcDet.addDeleteListener( this );
	
	}

	// Fun��o criada para montar a tela conforme a prefer�ncia do usu�rio:
	// com ou sem Refer�ncia sendo PK;
	private void montaDetalhe() {

		setAltDet( 100 );
		pinDet = new JPanelPad( 740, 100 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );
		adicCampo( txtCodItOrc, 7, 20, 30, 20, "CodItOrc", "Item", ListaCampos.DB_PK, true );
		
		if ( ( (Boolean) oPrefs[ PrefOrc.USAREFPROD.ordinal() ] ).booleanValue() ) {
			adicCampoInvisivel( txtCodProd, "CodProd", "C�d.prod.", ListaCampos.DB_FK, txtDescProd, false );
			adicCampoInvisivel( txtRefProd, "RefProd", "Ref.prod.", ListaCampos.DB_FK, false );
			adic( new JLabelPad( "Refer�ncia" ), 40, 0, 67, 20 );
			adic( txtRefProd, 40, 20, 67, 20 );
			txtRefProd.setFK( true );
			txtRefProd.setBuscaAdic( new DLBuscaProd( con, "REFPROD", lcProd2.getWhereAdic() ) );
		}
		else {
			adicCampo( txtCodProd, 40, 20, 67, 20, "CodProd", "C�d.prod.", ListaCampos.DB_FK, txtDescProd, true );
			txtCodProd.setBuscaAdic( new DLBuscaProd( con, "CODPROD", lcProd.getWhereAdic() ) );
		}

		txtQtdItOrc.setBuscaAdic( new DLBuscaEstoq( lcDet, lcAlmox, lcProd, con, "qtditvenda" ) );
		txtCodAlmoxItOrc.setAtivo( false );

		adicDescFK( txtDescProd, 110, 20, ( ( (Boolean) oPrefs[ PrefOrc.USALOTEORC.ordinal() ] ).booleanValue() ? 
				187 : 277 ), 20, "DescProd", "Descri��o do produto" );
		if ( ( (Boolean) oPrefs[ PrefOrc.USALOTEORC.ordinal() ] ).booleanValue() )
			adicCampo( txtCodLote, 300, 20, 88, 20, "CodLote", "Lote", ListaCampos.DB_FK, txtDescLote, false );
		adicCampo( txtQtdItOrc, 391, 20, 60, 20, "QtdItOrc", "Qtd.", ListaCampos.DB_SI, true );
		adicCampo( txtPrecoItOrc, 454, 20, 80, 20, "PrecoItOrc", "Pre�o", ListaCampos.DB_SI, true );
		adicCampo( txtPercDescItOrc, 537, 20, 47, 20, "PercDescItOrc", "% desc.", ListaCampos.DB_SI, false );
		adicCampo( txtVlrDescItOrc, 587, 20, 70, 20, "VlrDescItOrc", "Valor desc.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtVlrProdItOrc, "VlrProdItOrc", "Valor bruto", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtStrDescItOrc, "StrDescItOrc", "Descontos", ListaCampos.DB_SI, false );
		adicCampo( txtVlrLiqItOrc, 660, 20, 80, 20, "VlrLiqItOrc", "Valor item", ListaCampos.DB_SI, false );

		adicCampo( txtCodAlmoxItOrc, 7, 60, 65, 20, "CodAlmox", "C�d.ax.", ListaCampos.DB_FK, txtDescAlmoxItOrc, false );
		adicDescFK( txtDescAlmoxItOrc, 75, 60, 222, 20, "DescAlmox", "Descri��o do almoxarifado" );
		adicDescFK( txtSldLiqProd, 300, 60, 88, 20, "SldLiqProd", "Saldo" );
		adicDBLiv( txaObsItOrc, "ObsItOrc", "Observa��o", false );
		adicCampoInvisivel( txtCodEmpLG, "CodEmpLG", "Emp.log.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtCodFilialLG, "CodFilialLG", "Filial log.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtCodLog, "CodLog", "C�d.log.", ListaCampos.DB_SI, false );
		pinTot.adic( new JLabelPad( "Total desc." ), 7, 0, 90, 20 );
		pinTot.adic( txtVlrDescOrc, 7, 20, 100, 20 );
		pinTot.adic( new JLabelPad( "Total adic." ), 7, 40, 90, 20 );
		pinTot.adic( txtVlrAdicOrc, 7, 60, 100, 20 );
		pinTot.adic( new JLabelPad( "Total geral" ), 7, 80, 90, 20 );
		pinTot.adic( txtVlrLiqOrc, 7, 100, 100, 20 );

		setListaCampos( true, "ITORCAMENTO", "VD" );
		montaTab();

		tab.setAutoRol( true );

		tab.setTamColuna( 30, 0 );
		tab.setTamColuna( 70, 1 );
		tab.setTamColuna( 230, 2 );
		tab.setTamColuna( 60, 3 );
		tab.setTamColuna( 70, 4 );
		tab.setTamColuna( 60, 5 );
		tab.setTamColuna( 70, 6 );
		tab.setTamColuna( 90, 7 );
	}

	public void setLog( String[] args ) {

		if ( args != null ) {
			txtCodEmpLG.setVlrString( args[ 0 ] );
			txtCodFilialLG.setVlrString( args[ 1 ] );
			txtCodLog.setVlrString( args[ 2 ] );
		}
	}

	public void setParansPreco( BigDecimal bdPreco ) {

		txtPrecoItOrc.setVlrBigDecimal( bdPreco );
	}

	public Vector<JTextFieldPad> getParansDesconto() { 

		Vector<JTextFieldPad> param = new Vector<JTextFieldPad>();
		param.addElement( txtStrDescItOrc );
		param.addElement( txtPrecoItOrc );
		param.addElement( txtVlrDescItOrc );
		param.addElement( txtQtdItOrc );
		return param;
	}

	public String[] getParansPass() {

		return new String[] { "or�amento", txtCodOrc.getVlrString().trim(), txtCodItOrc.getVlrString().trim(), 
				txtCodProd.getVlrString().trim(), txtVlrProdItOrc.getVlrString().trim() };
	}

	private int getClComiss( int iCodVend ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		int iRet = 0;
		try {
			sSQL = "SELECT CODCLCOMIS FROM VDVENDEDOR " + "WHERE CODEMP=? AND CODFILIAL=? AND CODVEND=?";
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			ps.setInt( 3, iCodVend );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				iRet = rs.getInt( "CODCLCOMIS" );
				return iRet;
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar a class. da comiss�o." + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return iRet;
	}

	private int getCodCli() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		int iRet = 0;
		try {
			sSQL = "SELECT CODCLI FROM SGPREFERE4 WHERE " + "CODEMP=? AND CODFILIAL=?";
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				iRet = rs.getInt( "CODCLI" );
			}
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o c�digo do cliente.\n" + "Provavelmente n�o foram gravadas corretamente as prefer�ncias!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return iRet;
	}

	private int getCodTipoCli() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		int iRet = 0;
		
		try {
			
			sSQL = "SELECT CODTIPOCLI FROM VDCLIENTE WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=?";
			
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			ps.setInt( 3, txtCodCli.getVlrInteger().intValue() );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				iRet = rs.getInt( "CODTIPOCLI" );
			}
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o c�digo do tipo de cliente.\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return iRet;
	}

	private void getLote() {

		txtCodLote.setVlrString( getLote( txtCodProd.getVlrInteger().intValue(), 
				( (Boolean) oPrefs[ PrefOrc.CONTESTOQ.ordinal() ] ).booleanValue() ) );
		lcLote.carregaDados();
	}

	public int[] getParansPreco() {

		int[] iRetorno = { txtCodProd.getVlrInteger().intValue(), txtCodCli.getVlrInteger().intValue(), Aplicativo.iCodEmp, 
				ListaCampos.getMasterFilial( "VDCLIENTE" ),	 txtCodPlanoPag.getVlrInteger().intValue(), Aplicativo.iCodEmp, 
				ListaCampos.getMasterFilial( "FNPLANOPAG" ), ( (Integer) oPrefs[ PrefOrc.CODTIPOMOV2.ordinal() ] ).intValue(), 
				Aplicativo.iCodEmp,	ListaCampos.getMasterFilial( "EQTIPOMOV" ), Aplicativo.iCodEmp, Aplicativo.iCodFilial, 
				txtCodOrc.getVlrInteger().intValue(), Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDORCAMENTO" ) };
		return iRetorno;

	}

	private int getPlanoPag() {

		int iRet = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		try {
			sSQL = "SELECT CodPlanoPag FROM SGPREFERE4 WHERE " + "CODEMP=? AND CODFILIAL=?";
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				iRet = rs.getInt( "CodPlanoPag" );
			}
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o plano de pagamento.\n" + "Provavelmente n�o foram gravadas corretamente as prefer�ncias!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return iRet;
	}

	private int getPrazo() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		int iRet = 0;
		String sSQL = null;
		try {
			sSQL = "SELECT Prazo FROM SGPREFERE4 WHERE " + "CODEMP=? AND CODFILIAL=?";
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				iRet = rs.getInt( "Prazo" );
			}
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o prazo.\n" + "Provavelmente n�o foram gravadas corretamente as prefer�ncias!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return iRet;
	}

	private Date getVencimento() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		GregorianCalendar clVenc = new GregorianCalendar();
		Date dtRet = null;
		String sSQL = null;
		int diasVenc = 0;
		try {
			sSQL = "SELECT DIASVENCORC FROM SGPREFERE4 WHERE " + "CODEMP=? AND CODFILIAL=?";
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				diasVenc = rs.getInt( "DIASVENCORC" );
				clVenc.add( Calendar.DATE, diasVenc );
				dtRet = clVenc.getTime();
			}
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar a data de vencimento.\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return dtRet;
	}

	private int getVendedor() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		int iRet = 0;
		try {
			sSQL = "SELECT CODVEND FROM VDCLIENTE " + "WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=?";
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			ps.setInt( 3, txtCodCli.getVlrInteger().intValue() );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				iRet = rs.getInt( "CodVend" );
				return iRet;
			}

			sSQL = "SELECT CODVEND FROM ATATENDENTE WHERE " + "IDUSU=? AND CODEMPUS=? AND CODFILIALUS=?";
			ps = con.prepareStatement( sSQL );
			ps.setString( 1, Aplicativo.strUsuario );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, Aplicativo.iCodFilialPad );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				iRet = rs.getInt( "CodVend" );
			}

			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o comissionado.\n" + "O usu�rio '" + Aplicativo.strUsuario + "' � um comissionado?\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return iRet;
	}

	private void calcDescIt() {

		if ( txtPercDescItOrc.floatValue() > 0 ) {
			txtVlrDescItOrc.setVlrBigDecimal( new BigDecimal( Funcoes.arredFloat( txtVlrProdItOrc.floatValue() * txtPercDescItOrc.floatValue() / 100, casasDecFin ) ) );
			bdVlrDescItAnt = txtVlrDescItOrc.getVlrBigDecimal();
		}
		else if ( txtVlrDescItOrc.floatValue() == 0 ) {
			txtPercDescItOrc.setVlrString( "" );
			bdVlrDescItAnt = txtVlrDescItOrc.getVlrBigDecimal();
		}
	}

	private void calcTot() {

		txtVlrLiqItOrc.setVlrBigDecimal( calcVlrTotalProd( txtVlrProdItOrc.getVlrBigDecimal(), txtVlrDescItOrc.getVlrBigDecimal() ) );
	}

	private void calcVlrProd() {

		txtVlrProdItOrc.setVlrBigDecimal( calcVlrProd( txtPrecoItOrc.getVlrBigDecimal(), txtQtdItOrc.getVlrBigDecimal() ) );
	}

	private boolean testaLucro() {

		return super.testaLucro( new Object[] { txtCodProd.getVlrInteger(), txtCodAlmoxItOrc.getVlrInteger(), txtPrecoItOrc.getVlrBigDecimal(), } );
	}

	private boolean testaCodLote() {

		boolean bValido = false;
		String sSQL = "SELECT SLDLIQLOTE FROM EQLOTE " + "WHERE CODLOTE=? AND CODPROD=? AND CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setString( 1, txtCodLote.getVlrString().trim() );
			ps.setInt( 2, txtCodProd.getVlrInteger().intValue() );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "EQLOTE" ) );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				if ( rs.getFloat( 1 ) > 0.0f )
					bValido = true;
				else
					Funcoes.mensagemInforma( this, "LOTE SEM SALDO!" );
			}
			else
				Funcoes.mensagemErro( this, "C�d.lote � requerido." );

			rs.close();
			ps.close();
			if ( !con.getAutoCommit() )
				con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela EQLOTE!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}

		return bValido;
	}

	private void mostraTelaDescont() {

		if ( ( lcDet.getStatus() == ListaCampos.LCS_INSERT ) || ( lcDet.getStatus() == ListaCampos.LCS_EDIT ) ) {
			txtPercDescItOrc.setVlrString( "" );
			txtVlrDescItOrc.setVlrString( "" );
			calcVlrProd();
			calcTot();
			mostraTelaDesconto();
			calcVlrProd();
			calcTot();
			txtVlrDescItOrc.requestFocus( true );
		}
	}

	private void fechaOrc() {

		Object[] oValores = null;

		DLCompOrc dl = new DLCompOrc( this, ( txtVlrDescOrc.floatValue() > 0 ), 
				txtVlrProdOrc.getVlrBigDecimal(), txtPercDescOrc.getVlrBigDecimal(), 
				txtVlrDescOrc.getVlrBigDecimal(), txtPercAdicOrc.getVlrBigDecimal(), 
				txtVlrAdicOrc.getVlrBigDecimal(), txtCodPlanoPag.getVlrInteger() );
		try {
			/*
			 * Verifica se o or�amento foi gerado por um atendimento e adiciona a PK para ser preenchida na tela de complemento.
			 */
			if ( txtStatusOrc.getVlrString().equals( "OA" ) || txtCodAtend.getVlrInteger().intValue() > 0 )
				dl.setFKAtend( txtCodAtend.getVlrInteger().intValue() );
			dl.setConexao( con );
			dl.setVisible( true );
			if ( dl.OK ) {
				oValores = dl.getValores();
				dl.dispose();
			}
			else {
				dl.dispose();
			}
			if ( oValores != null ) {
				lcCampos.edit();

				txtPercDescOrc.setVlrBigDecimal( (BigDecimal) oValores[ 0 ] );
				txtVlrDescOrc.setVlrBigDecimal( (BigDecimal) oValores[ 1 ] );
				txtPercAdicOrc.setVlrBigDecimal( (BigDecimal) oValores[ 2 ] );
				txtVlrAdicOrc.setVlrBigDecimal( (BigDecimal) oValores[ 3 ] );

				if ( oValores[ 3 ] != txtCodPlanoPag.getVlrInteger() )
					txtCodPlanoPag.setVlrInteger( (Integer) ( oValores[ 4 ] ) );

				// pega o status antigo do or�amento;
				oldStatusOrc = txtStatusOrc.getVlrString().trim();
				// Ajusta o status para OC - or�amento completo.
				if ("OC-OL-OV".indexOf(oldStatusOrc)==-1) { 
					txtStatusOrc.setVlrString( "OC" );
				}
				if ( ( oValores[7]!= null ) && ( ((Integer) oValores[7] )).intValue()>0 ) {
					txtCodAtend.setVlrInteger( (Integer) oValores[7] );
				}
				lcCampos.post();
				lcCampos.carregaDados();
				if ( oValores[ 5 ].equals( "S" ) )
					aprovar();
				if ( oValores[ 6 ].equals( "S" ) )
					imprimir( true );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			oValores = null;
			dl = null;
		}
	}

	private void exportar() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		DLCopiaOrc dl = null;

		try {
			if ( txtCodOrc.getVlrInteger().intValue() == 0 || lcCampos.getStatus() != ListaCampos.LCS_SELECT ) {
				Funcoes.mensagemInforma( this, "Selecione um or�amento cadastrado antes!" );
				return;
			}
			dl = new DLCopiaOrc( this );
			dl.setConexao( con );
			dl.setVisible( true );
			if ( !dl.OK ) {
				dl.dispose();
				return;
			}
			int[] iVals = dl.getValores();
			dl.dispose();

			sSQL = "SELECT IRET FROM VDCOPIAORCSP(?,?,?,?,?)";

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodOrc.getVlrInteger().intValue() );
			ps.setInt( 4, iVals[ 1 ] );
			ps.setInt( 5, iVals[ 0 ] );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				if ( Funcoes.mensagemConfirma( this, "Or�amento '" + rs.getInt( 1 ) + "' criado com sucesso!\n" + 
						"Gostaria de edita-lo agora?" ) == JOptionPane.OK_OPTION ) {
					txtCodOrc.setVlrInteger( new Integer( rs.getInt( 1 ) ) );
					lcCampos.carregaDados();
				}
			}
			rs.close();
			ps.close();
			if ( !con.getAutoCommit() )
				con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao copiar o or�amento!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		dl.dispose();
	}

	public void aprovar() {

		PreparedStatement ps = null;
		String sSQL = null;
		String status = "OC";
		try {

			if ( tab.getRowCount() <= 0 ) {
				Funcoes.mensagemInforma( this, "N�o ha nenhum �tem para ser aprovado" );
				return;
			}
			/* isso aqui � pra n�o deixar aprovar mais de uma vez... */
			if ( oldStatusOrc.equals( "OL" ) || oldStatusOrc.equals( "OV" ) ) {
				if ( oldStatusOrc.equals( "OV" ) )
					Funcoes.mensagemInforma( this, "Or�amento j� foi faturado." );
				lcCampos.edit();
				txtStatusOrc.setVlrString( oldStatusOrc );
				lcCampos.post();
				lcCampos.carregaDados();
				return;
			}

			/* TIRADO O UPDATE EM EMITITORC PARA N�O PERMITIR A EMIS�O DO MESMO ITEM MAIS DE UMA VEZ */
			/*sSQL = "UPDATE VDITORCAMENTO SET ACEITEITORC='S', APROVITORC='S', VENCAUTORIZORC=?, " +
					"EMITITORC='N' " + "WHERE CODEMP=? AND CODFILIAL=? AND CODITORC=? AND CODORC=?";*/
			
			sSQL = "UPDATE VDITORCAMENTO SET ACEITEITORC='S', APROVITORC='S', VENCAUTORIZORC=? " +
					"WHERE CODEMP=? AND CODFILIAL=? AND CODITORC=? AND CODORC=?";

			try {
				ps = con.prepareStatement( sSQL );
				for ( int iLin = 0; iLin < tab.getRowCount(); iLin++ ) {
					ps.setDate( 1, Funcoes.dateToSQLDate( txtDtVencOrc.getVlrDate() ) );
					ps.setInt( 2, Aplicativo.iCodEmp );
					ps.setInt( 3, ListaCampos.getMasterFilial( "VDITORCAMENTO" ) );
					ps.setInt( 4, Integer.parseInt( tab.getValor( iLin, 0 ).toString() ) );
					ps.setInt( 5, txtCodOrc.getVlrInteger().intValue() );

					ps.execute();
				}
				if ( !con.getAutoCommit() )
					con.commit();
				status = "OL";
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao atualizar a tabela ITORCAMENTO!\n" + err.getMessage(), true, con, err );
			}

			try {
				sSQL = "UPDATE VDORCAMENTO SET STATUSORC=? WHERE " + "CODEMP=? AND CODFILIAL=? AND CODORC=?";

				ps = con.prepareStatement( sSQL );

				ps.setString( 1, status );
				ps.setInt( 2, Aplicativo.iCodEmp );
				ps.setInt( 3, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
				ps.setInt( 4, txtCodOrc.getVlrInteger().intValue() );
				ps.execute();
				if ( !con.getAutoCommit() )
					con.commit();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao atualizar a tabela ORCAMENTO!\n" + err.getMessage(), true, con, err );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			ps = null;
			sSQL = null;
		}
	}

	private synchronized void iniOrc() {

		txtCodCli.setVlrInteger( new Integer( getCodCli() ) );
		lcCli.carregaDados();
		txtCodTpCli.setVlrInteger( new Integer( getCodTipoCli() ) );
		lcTipoCli.carregaDados();
		txtCodPlanoPag.setVlrInteger( new Integer( getPlanoPag() ) );
		lcPlanoPag.carregaDados();
		txtCodVend.setVlrInteger( new Integer( getVendedor() ) );
		lcVend.carregaDados();
		lcProd.limpaCampos( true );
		lcProd2.limpaCampos( true );
		txtVlrAdicOrc.setVlrString( "" );
		txtVlrEdAdicOrc.setVlrString( "" );
		txtVlrEdDescOrc.setVlrString( "" );
		txtVlrLiqOrc.setVlrString( "" );
		txtVlrProdOrc.setVlrString( "" );
		txtDtOrc.setVlrDate( new Date() );
		txtDtVencOrc.setVlrDate( getVencimento() );
		txtPrazoEntOrc.setVlrInteger( new Integer( getPrazo() ) );
		tab.limpa();
		txtCodOrc.requestFocus();
	}

	private synchronized void iniItem() {

		lcDet.insert( true );
		txtCodItOrc.setVlrInteger( new Integer( 1 ) );
		if ( ( (Boolean) oPrefs[ PrefOrc.USAREFPROD.ordinal() ] ).booleanValue() )
			txtRefProd.requestFocus();
		else
			txtCodProd.requestFocus();
	}

	public void exec( int iCodOrc ) {

		txtCodOrc.setVlrString( String.valueOf( iCodOrc ) );
		lcCampos.carregaDados();
	}

	public void show() {

		super.show();
		lcCampos.insert( true );
		iniOrc();
	}

	private void focusCodprod() {

		if ( ( (Boolean) oPrefs[ PrefOrc.USAREFPROD.ordinal() ] ).booleanValue() )
			txtRefProd.requestFocus();
		else
			txtCodProd.requestFocus();
	}

	private void imprimir( boolean bVisualizar ) {

		String sOrdem = "";

		DLROrcamento dlo = new DLROrcamento( sOrdNota, sModoNota );
		dlo.setVisible( true );
		if ( dlo.OK == false ) {
			dlo.dispose();
			return;
		}
		if ( dlo.getModo().equals( "G" ) ) {
			imprimeGrafico( bVisualizar );
		}
		else if ( dlo.getModo().equals( "T" ) ) {
			sOrdem = dlo.getOrdem();
			imprimeTexto( bVisualizar, sOrdem );
		}
	}

	public void imprimeGrafico( boolean bVisualizar ) {

		String sSql = "SELECT CLASSTPCONV FROM ATTIPOCONV WHERE CODEMP=? AND CODFILIAL=? AND CODTPCONV=?";
		String sClassOrc = "";
		
		LeiauteGR leiOrc = null;
		
		try {
			
			PreparedStatement ps = con.prepareStatement( sSql );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "ATTIPOCONV" ) );
			ps.setInt( 3, txtCodTpConv.getVlrInteger().intValue() );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {
				if ( rs.getString( "CLASSTPCONV" ) != null ) {
					sClassOrc = rs.getString( "CLASSTPCONV" ).trim();
				}
			}
			else {
				sSql = "SELECT CLASSORC FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
				sClassOrc = "";
				PreparedStatement ps2 = null;
				ResultSet rs2 = null;
				leiOrc = null;
				try {
					ps2 = con.prepareStatement( sSql );
					ps2.setInt( 1, Aplicativo.iCodEmp );
					ps2.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
					rs2 = ps2.executeQuery();

					if ( rs2.next() ) {
						if ( rs2.getString( "CLASSORC" ) != null ) {
							sClassOrc = rs2.getString( "CLASSORC" ).trim();
							rs2.close();
							ps2.close();
						}
					}
				} catch ( SQLException err ) {
					Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
					err.printStackTrace();
				}
			}
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela ATTPCONV!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
		try {
			if ( ("".equals( sClassOrc.trim() )) || ( sClassOrc.indexOf( "jasper" ) > -1 )) {
				HashMap<String, Object> hParam = new HashMap<String, Object>();
				hParam.put( "CODORC", txtCodOrc.getVlrInteger() );
				hParam.put( "CODEMP", Aplicativo.iCodEmp );					
				hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
				hParam.put( "CODFILIALPF", ListaCampos.getMasterFilial( "SGPREFERE1" ) );
								
				hParam.put( "SUBREPORT_DIR", "org/freedom/layout/orc/"); 
				
				if ("".equals( sClassOrc.trim() ) ) {
					sClassOrc = "ORC_PD.jasper";
				}
				
				EmailBean mail = Aplicativo.getEmailBean();				
				mail.setPara( EmailBean.getEmailCli( txtCodCli.getVlrInteger(), con ) );
				
				FPrinterJob dlGr = new FPrinterJob( "layout/orc/" + sClassOrc , null, null, this, hParam, con, mail );

				if ( bVisualizar ) {
					dlGr.setVisible( true );
				}
				else {
					JasperPrintManager.printReport( dlGr.getRelatorio(), true );
				}
			} else {
				leiOrc = (LeiauteGR) Class.forName( "org.freedom.layout.orc." + sClassOrc ).newInstance();
				leiOrc.setConexao( con );
				vParamOrc.clear();
				vParamOrc.addElement( txtCodOrc.getText() );
				vParamOrc.addElement( txtCodCli.getText() );
				leiOrc.setParam( vParamOrc );
				if ( bVisualizar ) {
					dl = new FPrinterJob( leiOrc, this );
					dl.setVisible( true );
				}
				else {
					leiOrc.imprimir( true );
				}
			}
		} catch ( Exception err ) {
			Funcoes.mensagemInforma( this, "N�o foi poss�vel carregar o layout de Or�amento!\n" + err.getMessage() );
			err.printStackTrace();
		}
	}

	public void imprimeTexto( boolean bVisualizar, String sOrdem ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		String linhaFina = Funcoes.replicate( "-", 135 );
		Vector<?> vDesc = null;
		Vector<?> vObs = null;
		ImprimeOS imp = new ImprimeOS( "", con );
		int iCodOrc = txtCodOrc.getVlrInteger().intValue();
		int linPag = imp.verifLinPag() - 1;

		try {
			sSQL = "SELECT O.CODORC, O.CODPLANOPAG, O.CODCLI, O.OBSORC, O.VLRLIQORC, O.PRAZOENTORC, C.RAZCLI," + 
			" C.CONTCLI, C.CNPJCLI, C.CPFCLI, C.RGCLI, C.INSCCLI, C.SITECLI, C.EMAILCLI, C.ENDCLI, C.NUMCLI,"	+ 
			" C.BAIRCLI, C.CIDCLI, C.UFCLI, C.CEPCLI,C.DDDCLI, C.FONECLI, C.FAXCLI, I.CODITORC, I.CODPROD," + 
			" I.QTDITORC, I.PRECOITORC, I.VLRPRODITORC, I.VLRLIQITORC, I.VLRDESCITORC, P.REFPROD, P.DESCPROD, P.CODUNID," + 
			" PG.DESCPLANOPAG, I.OBSITORC, VEND.NOMEVEND, VEND.EMAILVEND,"	+ 
			" (SELECT FN.DESCFUNC FROM RHFUNCAO FN WHERE FN.CODEMP=VEND.CODEMPFU" + 
			" AND FN.CODFILIAL=VEND.CODFILIALFU AND FN.CODFUNC=VEND.CODFUNC)" + 
			" FROM VDORCAMENTO O, VDITORCAMENTO I, VDCLIENTE C, EQPRODUTO P, FNPLANOPAG PG, VDVENDEDOR VEND" + 
			" WHERE O.CODEMP=? AND O.CODFILIAL=? AND O.CODORC=?" + 
			" AND C.CODEMP=O.CODEMPCL AND C.CODFILIAL=O.CODFILIALCL AND C.CODCLI=O.CODCLI" + 
			" AND I.CODEMP=O.CODEMP AND I.CODFILIAL=O.CODFILIAL AND I.CODORC=O.CODORC AND I.TIPOORC=O.TIPOORC"	+ 
			" AND P.CODEMP=I.CODEMPPD AND P.CODFILIAL=I.CODFILIALPD AND P.CODPROD=I.CODPROD" + 
			" AND PG.CODEMP=O.CODEMPPG AND PG.CODFILIAL=O.CODFILIALPG AND PG.CODPLANOPAG=O.CODPLANOPAG" + 
			" AND VEND.CODEMP=O.CODEMPVD AND VEND.CODFILIAL=O.CODFILIALVD AND VEND.CODVEND=O.CODVEND" + 
			" ORDER BY P." + sOrdem + ",P.DESCPROD";

			imp.montaCab();
			imp.setTitulo( "OR�AMENTO" );
			imp.limpaPags();
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ps.setInt( 3, iCodOrc );
			rs = ps.executeQuery();
			while ( rs.next() ) {

				vDesc = new Vector<Object>();
				if ( ( (Boolean) oPrefs[ PrefOrc.DESCCOMPPED.ordinal() ] ).booleanValue() )
					vDesc = Funcoes.quebraLinha( Funcoes.stringToVector( rs.getString( "ObsItOrc" ) == null ? 
							rs.getString( "DescProd" ).trim() : rs.getString( "ObsItOrc" ).trim() ), 50 );
				else
					vDesc = Funcoes.quebraLinha( Funcoes.stringToVector( rs.getString( "DescProd" ).trim() ), 50 );

				for ( int i = 0; i < vDesc.size(); i++ ) {
					if ( imp.pRow() == 0 ) {
						imp.impCab( 136, false );
						imp.say( 0, imp.comprimido() );
						imp.say( 1, "CLIENTE" );
						imp.say( 70, "OR�AMENTO: " + ( rs.getString( "CodOrc" ) != null ? rs.getString( "CodOrc" ).trim() : "" ) );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 1, ( rs.getString( "RazCli" ) != null ? rs.getString( "RazCli" ).trim() : "" ) + " - " + ( rs.getString( "CodCli" ) != null ? rs.getString( "CodCli" ).trim() : "" ) );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 1, rs.getString( "CpfCli" ) != null ? "CPF    : " + Funcoes.setMascara( rs.getString( "CpfCli" ), "###.###.###-##" ) : "CNPJ   : " + Funcoes.setMascara( rs.getString( "CnpjCli" ), "##.###.###/####-##" ) );
						imp.say( 70, "CONTATO: " + ( rs.getString( "ContCli" ) != null ? rs.getString( "ContCli" ).trim() : "" ) );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 1, rs.getString( "RgCli" ) != null ? "R.G.   : " + rs.getString( "RgCli" ) : "I.E.   : " + rs.getString( "InscCli" ) );// IE cliente
						imp.say( 70, ( rs.getString( "EndCli" ) != null ? rs.getString( "EndCli" ).trim() : "" ) + ( rs.getString( "NumCli" ) != null ? "  N�: " + rs.getString( "NumCli" ).trim() : "" ) );// rua e n�mero do cliente
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 1, "SITE   : " + ( rs.getString( "SiteCli" ) != null ? rs.getString( "SiteCli" ).trim() : "" ) );
						imp.say( 70, ( rs.getString( "BairCli" ) != null ? rs.getString( "BairCli" ).trim() : "" ) + ( rs.getString( "CidCli" ) != null ? " - " + rs.getString( "CidCli" ).trim() : "" ) + ( rs.getString( "UFCli" ) != null ? " - " + rs.getString( "UFCli" ).trim() : "" )
								+ ( rs.getString( "CepCli" ) != null ? " - " + rs.getString( "CepCli" ).trim() : "" ) );// complemento do endere�o do cliente
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 1, "E-MAIl : " + ( rs.getString( "EmailCli" ) != null ? rs.getString( "EmailCli" ).trim() : "" ) );
						imp.say( 70, "TEL: " + ( rs.getString( "DDDCli" ) != null ? "(" + rs.getString( "DDDCli" ) + ")" : "" ) + ( rs.getString( "FoneCli" ) != null ? Funcoes.setMascara( rs.getString( "FoneCli" ).trim(), "####-####" ) : "" ) + " - FAX:"
								+ ( rs.getString( "FaxCli" ) != null ? Funcoes.setMascara( rs.getString( "FaxCli" ), "####-####" ) : "" ) );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, linhaFina );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 55, "DADO(S) DO(S) PRODUTO(S)" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, linhaFina );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 1, "IT" );
						imp.say( 6, "|   C�DIGO" );
						imp.say( 19, "|                     DESCRI��O" );
						imp.say( 72, "| UN" );
						imp.say( 77, "|    QUANT." );
						imp.say( 91, "|   V.UNIT." );
						imp.say( 105, "|   V.DESC." );
						imp.say( 119, "|     V.TOTAL" );
					}

					imp.pulaLinha( 1, imp.comprimido() );

					if ( i == 0 ) {
						imp.say( 1, rs.getString( "CodItOrc" ).trim() );

						if ( ( (Boolean) oPrefs[ PrefOrc.USAREFPROD.ordinal() ] ).booleanValue() )
							imp.say( 7, rs.getString( "RefProd" ).trim() );
						else
							imp.say( 7, rs.getString( "CodProd" ).trim() );
					}

					imp.say( 20, (String) vDesc.elementAt( i ).toString() );

					if ( i == 0 ) {
						imp.say( 74, Funcoes.copy( rs.getString( "CodUnid" ).trim(), 2 ) );
						imp.say( 78, Funcoes.strDecimalToStrCurrency( 12, 2, rs.getString( "QtdItOrc" ) ) );
						imp.say( 92, Funcoes.strDecimalToStrCurrency( 12, 2, rs.getString( "PrecoItOrc" ) ) );
						imp.say( 106, Funcoes.strDecimalToStrCurrency( 12, 2, rs.getString( "VlrDescItOrc" ) ) );
						imp.say( 120, Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( "VlrLiqItOrc" ) ) );
					}
				}
			}
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, linhaFina );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 86, "|    TOTAL PRODUTOS: " + rs.getString( "VlrLiqOrc" ) );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, linhaFina );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 55, "INFORMA��ES COMPLEMENTARES" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, linhaFina );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "PAGAMENTO.........:    " + rs.getString( "CODPLANOPAG" ) + " - " + rs.getString( "DESCPLANOPAG" ) );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "PRAZO DE ENTREGA..:    " + rs.getString( "PrazoEntOrc" ) );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, linhaFina );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 62, "OBSERVAC�O" );
			imp.pulaLinha( 1, imp.comprimido() );

			vObs = Funcoes.quebraLinha( Funcoes.stringToVector( rs.getString( "ObsOrc" ) ), 115 );

			for ( int i = 0; i < vObs.size(); i++ ) {
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 20, vObs.elementAt( i ).toString() );
				if ( imp.pRow() >= linPag ) {
					imp.incPags();
					imp.eject();
				}
			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, linhaFina );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 5, Funcoes.replicate( "-", 40 ) );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 5, rs.getString( "NomeVend" ) );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 5, rs.getString( 37 ) );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 5, rs.getString( "EmailVend" ) );

			imp.eject();
			imp.fechaGravacao();

			if ( !con.getAutoCommit() )
				con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela de Venda!" + err.getMessage(), true, con, err );
		} finally {
			vDesc = null;
			vObs = null;
			ps = null;
			rs = null;
			sSQL = null;
		}

		if ( bVisualizar ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	private enum PrefOrc {USAREFPROD, USALIQREL, TIPOPRECOCUSTO, CODTIPOMOV2, DESCCOMPPED, USAORCSEQ, 
		OBSCLIVEND, RECALCPCORC, USABUSCAGENPROD, USALOTEORC, CONTESTOQ, TITORCTXT01  } ;
	
	private Object[] prefs() {

		Object[] oRetorno = new Object[ PrefOrc.values().length ];
		String sSQL = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			sSQL = "SELECT P.USAREFPROD,P.USALIQREL,P.TIPOPRECOCUSTO,P.CODTIPOMOV2,P4.USALOTEORC,P.CONTESTOQ," + 
			  "P.ORDNOTA,P.DESCCOMPPED,P.USAORCSEQ,P.OBSCLIVEND,P.RECALCPCORC,P4.USABUSCAGENPROD,P.TITORCTXT01 " +
			  "FROM SGPREFERE1 P, SGPREFERE4 P4 " + "WHERE P.CODEMP=? AND P.CODFILIAL=? " + 
			  "AND P4.CODEMP=P.CODEMP AND P4.CODFILIAL=P.CODFILIAL";
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				oRetorno[ PrefOrc.USAREFPROD.ordinal() ] = new Boolean( rs.getString( "UsaRefProd" ).trim().equals( "S" ) );
				if ( rs.getString( "UsaLiqRel" ) == null ) {
					oRetorno[ PrefOrc.USALIQREL.ordinal() ] = new Boolean( false );
					Funcoes.mensagemInforma( this, "Preencha op��o de desconto em prefer�ncias!" );
				}
				else
					oRetorno[ PrefOrc.USALIQREL.ordinal() ] = new Boolean( rs.getString( "UsaLiqRel" ).trim().equals( "S" ) );
				oRetorno[ PrefOrc.TIPOPRECOCUSTO.ordinal() ] = new Boolean( rs.getString( "TipoPrecoCusto" ).equals( "M" ) );
				if ( rs.getString( "CODTIPOMOV2" ) != null )
					oRetorno[ PrefOrc.CODTIPOMOV2.ordinal() ] = new Integer( rs.getInt( "CODTIPOMOV2" ) );
				else
					oRetorno[ PrefOrc.CODTIPOMOV2.ordinal() ] = new Integer( 0 );
				oRetorno[ PrefOrc.DESCCOMPPED.ordinal() ] = new Boolean( rs.getString( "DescCompPed" ).equals( "S" ) );
				oRetorno[ PrefOrc.USAORCSEQ.ordinal() ] = new Boolean( rs.getString( "UsaOrcSeq" ).equals( "S" ) );
				oRetorno[ PrefOrc.OBSCLIVEND.ordinal() ] = new Boolean( rs.getString( "ObsCliVend" ).equals( "S" ) );
				oRetorno[ PrefOrc.RECALCPCORC.ordinal() ] = new Boolean( rs.getString( "ReCalcPCOrc" ).equals( "S" ) );
				oRetorno[ PrefOrc.USABUSCAGENPROD.ordinal() ] = new Boolean( rs.getString( "USABUSCAGENPROD" ).equals( "S" ) );
				oRetorno[ PrefOrc.USALOTEORC.ordinal() ] = new Boolean( rs.getString( "USALOTEORC" ).equals( "S" ) );
				oRetorno[ PrefOrc.CONTESTOQ.ordinal() ] = new Boolean( rs.getString( "CONTESTOQ" ).equals( "S" ) );								
				oRetorno[ PrefOrc.TITORCTXT01.ordinal() ] = rs.getString( "TitOrcTxt01" );
				if ( oRetorno[ PrefOrc.TITORCTXT01.ordinal() ] == null )
					oRetorno[ PrefOrc.TITORCTXT01.ordinal() ] = "";
				
				sOrdNota = rs.getString( "OrdNota" );

			}
			rs.close();
			ps.close();

			sSQL = "SELECT IMPGRAFICA FROM SGESTACAOIMP WHERE CODEMP=? AND CODFILIAL=? AND IMPPAD='S' AND CODEST=?";
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGESTACAOIMP" ) );
			ps.setInt( 3, Aplicativo.iNumEst );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				sModoNota = "G";
				if ( ( rs.getString( "IMPGRAFICA" ) != null ) && ( !rs.getString( "IMPGRAFICA" ).equals( "S" ) ) ) {
					sModoNota = "T";
				}
			}
			rs.close();
			ps.close();
			if ( !con.getAutoCommit() )
				con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela SGPREFERE1!\n" + err.getMessage(), true, con, err );
		} finally {
			sSQL = null;
			ps = null;
			rs = null;
		}

		return oRetorno;
	}

	public void focusGained( FocusEvent fevt ) {

	}

	public void focusLost( FocusEvent fevt ) {

		if ( fevt.getSource() == txtPercDescItOrc ) {
			if ( txtPercDescItOrc.getText().trim().length() > 1 ) {
				calcDescIt();
				calcVlrProd();
				calcTot();
			}
		}
		else if ( fevt.getSource() == txtVlrDescItOrc ) {
			if ( bdVlrDescItAnt != txtVlrDescItOrc.getVlrBigDecimal() )
				if ( txtPercDescItOrc.getText().trim().length() < 1 )
					txtPercDescItOrc.setVlrString( "" );
			if ( txtVlrDescItOrc.getVlrBigDecimal().floatValue() >= 0 ) {
				calcDescIt();
				calcVlrProd();
				calcTot();
			}

			if ( lcDet.getStatus() == ListaCampos.LCS_INSERT ) {
				lcDet.post();
				lcDet.limpaCampos( true );
				lcDet.setState( ListaCampos.LCS_NONE );
				lcDet.edit();
				focusCodprod();
			}
			else if ( lcDet.getStatus() == ListaCampos.LCS_EDIT ) {
				lcDet.post();
				txtCodItOrc.requestFocus();
			}
		}
		else if ( ( fevt.getSource() == txtQtdItOrc ) || ( fevt.getSource() == txtPrecoItOrc ) ) {
			calcVlrProd();
			calcTot();
		}
	}

	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getKeyCode() == KeyEvent.VK_CONTROL ) {
			bCtrl = true;
		}
		else if ( kevt.getKeyCode() == KeyEvent.VK_O ) {
			if ( bCtrl ) {
				btObs.doClick();
			}
		}
		else if ( kevt.getKeyCode() == KeyEvent.VK_F4 ) {
			btFechaOrc.doClick();
		}
		else if ( kevt.getKeyCode() == KeyEvent.VK_F3 ) {
			if ( kevt.getSource() == txtPercDescItOrc || kevt.getSource() == txtVlrDescItOrc )
				mostraTelaDescont();
		}
		else if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
			if ( kevt.getSource() == txtCodPlanoPag )
				if ( lcCampos.getStatus() == ListaCampos.LCS_INSERT )
					lcCampos.post();
		}
		if ( kevt.getSource() == txtRefProd )
			lcDet.edit();

		super.keyPressed( kevt );
	}

	public void keyTyped( KeyEvent kevt ) {

		super.keyTyped( kevt );
	}

	public void keyReleased( KeyEvent kevt ) {

		if ( kevt.getKeyCode() == KeyEvent.VK_CONTROL )
			bCtrl = false;
		super.keyReleased( kevt );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btFechaOrc ) {
			fechaOrc();
		}
		else if ( evt.getSource() == btPrevimp )
			imprimir( true );
		else if ( evt.getSource() == btImp )
			imprimir( false );
		else if ( evt.getSource() == btOrc ) {
			ImprimeOrc imp = new ImprimeOrc( txtCodOrc.getVlrInteger().intValue() );
			imp.setConexao( con );
			dl = new FPrinterJob( imp, this );
			dl.setVisible( true );
		}
		else if ( evt.getSource() == btOrcTst ) {
			LeiauteGR leiOrc = null;
			try {
				leiOrc = (LeiauteGR) Class.forName( "org.freedom.layout.orc." + "LaudoAprSusFisio" ).newInstance();
				leiOrc.setConexao( con );
				vParamOrc.clear();
				vParamOrc.addElement( txtCodOrc.getText() );
				vParamOrc.addElement( txtCodConv.getText() );
				leiOrc.setParam( vParamOrc );

				dl = new FPrinterJob( leiOrc, this );
				dl.setVisible( true );
			} catch ( Exception err ) {
				Funcoes.mensagemInforma( this, "N�o foi poss�vel carregar o leiaute de Or�amento Fisio.!\n" + err.getMessage() );
				err.printStackTrace();
			}
		}
		else if ( evt.getSource() == btOrcTst2 ) {
			LeiauteGR leiOrc = null;
			try {
				leiOrc = (LeiauteGR) Class.forName( "org.freedom.layout.orc." + "ContratoAluguelApr" ).newInstance();
				leiOrc.setConexao( con );
				vParamOrc.clear();
				vParamOrc.addElement( txtCodOrc.getText() );
				vParamOrc.addElement( txtCodConv.getText() );
				leiOrc.setParam( vParamOrc );

				dl = new FPrinterJob( leiOrc, this );
				dl.setVisible( true );
			} catch ( Exception err ) {
				Funcoes.mensagemInforma( this, "N�o foi poss�vel carregar o layout de Contrato de loca��o!\n" + err.getMessage() );
				err.printStackTrace();
			}
		}		
		else if ( evt.getSource() == btObs ) {
			mostraObs( "VDORCAMENTO", txtCodOrc.getVlrInteger().intValue() );
		}
		else if ( evt.getSource() == btExp )
			exportar();
		super.actionPerformed( evt );
	}

	public void beforeCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcProd2 ) {
			lcProd.edit();
		}
	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcDet ) {
			lcOrc2.carregaDados();// Carrega os Totais
		}
		else if ( ( cevt.getListaCampos() == lcProd ) || ( cevt.getListaCampos() == lcProd2 ) ) {
			if ( lcDet.getStatus() == ListaCampos.LCS_INSERT ) {

				if ( ( (Boolean) oPrefs[ PrefOrc.USALOTEORC.ordinal() ] ).booleanValue() && 
						txtCLoteProd.getVlrString().equals( "S" ) ) {
					getLote();
					txtCodLote.setAtivo( true );
				}
				else {
					txtCodLote.setAtivo( false );
				}
				calcVlrItem( null, false );
			}
			lcAlmox.carregaDados();
		}
		else if ( cevt.getListaCampos() == lcCampos ) {
			String s = txtCodOrc.getVlrString();
			lcOrc2.carregaDados();// Carrega os Totais
			txtCodOrc.setVlrString( s );
			s = null;
		}
		else if ( cevt.getListaCampos() == lcCli ) {
			if ( ( (Boolean) oPrefs[ PrefOrc.OBSCLIVEND.ordinal() ] ).booleanValue() ) {
				if ( iCodCliAnt != txtCodCli.getVlrInteger().intValue() ) {
					iCodCliAnt = txtCodCli.getVlrInteger().intValue();
					mostraObsCli( iCodCliAnt, new Point( this.getX(), this.getY() + pinCab.getHeight() + 
							pnCab.getHeight() + 10 ), new Dimension( spTab.getWidth(), 150 ) );
				}
			}
			if ( ( (Boolean) oPrefs[ PrefOrc.RECALCPCORC.ordinal() ] ).booleanValue() ) {
				setReCalcPreco( true );
			}
			txtCodTpCli.setVlrInteger( new Integer( getCodTipoCli() ) );
			lcTipoCli.carregaDados();
		}
		else if ( cevt.getListaCampos() == lcPlanoPag ) {
			if ( ( (Boolean) oPrefs[ PrefOrc.RECALCPCORC.ordinal() ] ).booleanValue() )
				setReCalcPreco( true );
		}
	}

	public void beforePost( PostEvent evt ) {
		if ( evt.getListaCampos() == lcCampos ) {
			if ( lcCampos.getStatus() == ListaCampos.LCS_INSERT ) {
				if ( ( (Boolean) oPrefs[ PrefOrc.USAORCSEQ.ordinal() ] ).booleanValue() )
					txtCodOrc.setVlrInteger( testaCodPK( "VDORCAMENTO" ) );
				txtStatusOrc.setVlrString( "*" );
			}
			if ( podeReCalcPreco() )
				calcVlrItem( "VDORCAMENTO", true );
			txtCodClComiss.setVlrInteger( new Integer( getClComiss( txtCodVend.getVlrInteger().intValue() ) ) );
		}
		else if ( evt.getListaCampos() == lcDet ) {
			if ( ( lcDet.getStatus() == ListaCampos.LCS_INSERT ) || ( lcDet.getStatus() == ListaCampos.LCS_EDIT ) ) {
				if ( txtQtdItOrc.getVlrBigDecimal().floatValue() <= 0 ) {
					Funcoes.mensagemInforma( this, "Quantidade invalida!" );
					evt.cancela();
					return;
				}
				if ( txtPrecoItOrc.getVlrBigDecimal().floatValue() <= 0 ) {
					Funcoes.mensagemInforma( this, "Pre�o invalido!" );
					evt.cancela();
					return;
				}
				if ( txtCLoteProd.getVlrString().equals( "S" ) && 
						( (Boolean) oPrefs[ PrefOrc.USALOTEORC.ordinal() ] ).booleanValue() ) {
					if ( !testaCodLote() )
						evt.cancela();
				}
				if ( !testaLucro() ) {
					Funcoes.mensagemInforma( this, "N�o � permitido a venda deste produto abaixo do custo!!!" );
					evt.cancela();
				}
			}
		}
		setReCalcPreco( false );
	}

	public void afterPost( PostEvent pevt ) {

		lcOrc2.carregaDados(); // Carrega os Totais
		if ( pevt.getListaCampos() == lcCampos ) {
			if ( lcDet.getStatus() == ListaCampos.LCS_NONE ) {
				iniItem();
			}
		}
	}

	public void beforeDelete( DeleteEvent devt ) {

	}

	public void afterDelete( DeleteEvent devt ) {

		if ( devt.getListaCampos() == lcDet )
			lcOrc2.carregaDados();
	}

	public void beforeInsert( InsertEvent ievt ) {

	}

	public void afterInsert( InsertEvent ievt ) {

		if ( ievt.getListaCampos() == lcCampos )
			iniOrc();
		else if ( ievt.getListaCampos() == lcDet )
			focusCodprod();
	}

	public void setConexao( Connection cn ) {

		super.setConexao( cn );
		montaOrcamento();
		montaDetalhe();
		lcProd.setConexao( cn );
		lcProd2.setConexao( cn );
		lcLote.setConexao( cn );
		lcOrc2.setConexao( cn );
		lcCli.setConexao( cn );
		lcPlanoPag.setConexao( cn );
		lcVend.setConexao( cn );
		lcTipoCli.setConexao( cn );
		lcAlmox.setConexao( cn );
		lcClComiss.setConexao( cn );
		lcConv.setConexao( cn );
		lcTipoConv.setConexao( cn );
		lcEnc.setConexao( cn );
		lcAtend.setConexao( cn );

		if ( ( (Boolean) oPrefs[ PrefOrc.USABUSCAGENPROD.ordinal() ] ).booleanValue() ) {
			if ( ( (Boolean) oPrefs[ PrefOrc.USAREFPROD.ordinal() ] ).booleanValue() )
				txtRefProd.setBuscaGenProd( new DLCodProd( cn, null ) );
			else
				txtCodProd.setBuscaGenProd( new DLCodProd( cn, null ) );
		}
	}
}