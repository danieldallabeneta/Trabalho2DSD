package Controller;

import View.ViewSimulacao;
import Model.Road;
import Model.SemaforoRodovia;
import Model.ParametrosSimulacao;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ControllerMalha {

    private static ControllerMalha instance;
    private ViewSimulacao telaSimulacao;
    private Road matrix[][];
    private File file = null;
    private ControllerSimulacao simulation;
    private int linha;
    private int coluna;
    private int maxCarrosMalha;
    private ParametrosSimulacao parametros;
    private JLabel contagemCarrosAndando;

    private ControllerMalha() {
    }

    public static synchronized ControllerMalha getInstance() {
        if (instance == null) {
            instance = new ControllerMalha();
        }
        return instance;
    }

    /**
     * Retorna a view de simulação
     * @return 
     */
    public ViewSimulacao getTelaSimulacao() {
        return this.telaSimulacao;
    }

    /**
     * Seta a tela de simulação
     * @param telaSimulacao 
     */
    public void setTelaSimulacao(ViewSimulacao telaSimulacao) {
        this.telaSimulacao = telaSimulacao;
    }
    
    /**
     * retorna o modelo de simulação
     * @return 
     */
    public ControllerSimulacao getSimulation() {
        return this.simulation;
    }

    /**
     * Retorna a quantidade de carros em movimento
     * @return 
     */
    public JLabel getContagemCarrosAndando() {
        return contagemCarrosAndando;
    }

    /**
     * Seta a quantidade de carros em movimento na malha
     * @param contagemCarrosAndando 
     */
    public void setContagemCarrosAndando(JLabel contagemCarrosAndando) {
        this.contagemCarrosAndando = contagemCarrosAndando;
    }

    /**
     * Cria a matriz conforme malha selecionada
     * @param malha 
     */
    public void setMatrizFromMalha(File malha) {
        Scanner meshScanner = null;
        try {
            meshScanner = new Scanner(malha);
            while (meshScanner.hasNextInt()) {
                this.linha  = meshScanner.nextInt();
                this.coluna = meshScanner.nextInt();
                this.matrix = new Road[this.coluna][this.linha];

                for (int y = 0; y < this.linha; y++) {
                    for (int x = 0; x < this.coluna; x++) {
                        int direcao = meshScanner.nextInt();
                        SemaforoRodovia cell = new SemaforoRodovia(x, y, direcao);
                        if (this.isRoad(cell)) {
                            this.defineEntradaAndSaida(cell);
                        }
                        this.matrix[x][y] = cell;
                        if (direcao > 0 && direcao < 5) {
                            maxCarrosMalha++;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            meshScanner.close();
        }
    }

    /**
     * Retorna se é rodovia
     * @param cell
     * @return 
     */
    public boolean isRoad(SemaforoRodovia cell) {
        return cell.getDirecao() > 0;
    }

    /**
     * Finaliza a simulação
     */
    public void finalizaSimulacao() {
        this.simulation.setFinalizaSimulacao(true);
    }

    /**
     * Encerra a simulação
     */
    public void encerraSimulacao() {
        this.simulation.setEncerraSimulacao(true);
    }

    /**
     * Inicia a simulação da malha
     */
    public void iniciaSimulacao() {
        this.simulation = new ControllerSimulacao(this);
        this.simulation.start();
    }

    /**
     * Define se a celula é de entrada e de saída
     * @param cell 
     */
    public void defineEntradaAndSaida(Road cell) {
        boolean isCelulaEntrada = defineEntrada(cell);
        boolean isExitCell = defineSaida(cell);
        cell.setIsCelulaEntrada(isCelulaEntrada);
        cell.setIsCelularSaida(isExitCell);
    }
    
    /**
     * Retorna se a célula é de entrada
     * @param cell
     * @return 
     */
    private boolean defineEntrada(Road cell){
        boolean entradaCima     = this.isEntradaSuperior(cell);
        boolean entradaBaixo    = this.isEntradaInferior(cell);
        boolean entradaEsquerda = this.isEntradaEsquerda(cell);
        boolean entradaDireita  = this.isEntradaDireita(cell);
        return entradaCima || entradaBaixo || entradaEsquerda || entradaDireita;
    }
    
    /**
     * Define se a célula é de saída
     * @param cell
     * @return 
     */
    private boolean defineSaida(Road cell){
        boolean saidaCima     = this.isSaidaSuperior(cell);
        boolean saidaBaixo    = this.isSaidaInferior(cell);
        boolean saidaEsquerda = this.isSaidaEsquerda(cell);
        boolean saidaDireita  = this.isSaidaDireita(cell);
        return saidaCima || saidaBaixo || saidaEsquerda || saidaDireita;
    }
    
    /**
     * Retorna se é entrada superior
     * @param cell
     * @return 
     */
    public boolean isEntradaSuperior(Road cell) {
        return cell.getY() - 1 < 0 && cell.getDirecao() == 3;
    }

    /**
     * Retorna se é entrada Inferior
     * @param cell
     * @return 
     */
    public boolean isEntradaInferior(Road cell) {
        return cell.getY() + 1 >= this.linha && cell.getDirecao() == 1;
    }

    /**
     * Retorna se é entrada Esquerda
     * @param cell
     * @return 
     */
    public boolean isEntradaEsquerda(Road cell) {
        return cell.getX() - 1 < 0 && cell.getDirecao() == 2;
    }

    /**
     * Retorna se é entrada Direita
     * @param cell
     * @return 
     */
    public boolean isEntradaDireita(Road cell) {
        return cell.getX() + 1 >= this.coluna && cell.getDirecao() == 4;
    }

    /**
     * Retorna se é saída Superior
     * @param cell
     * @return 
     */
    public boolean isSaidaSuperior(Road cell) {
        return cell.getY() - 1 < 0 && cell.getDirecao() == 1;
    }

    /**
     * Retorna se é saída Inferior
     * @param cell
     * @return 
     */
    public boolean isSaidaInferior(Road cell) {
        return cell.getY() + 1 >= this.linha && cell.getDirecao() == 3;
    }

    /**
     * Retorna se é saída Esquerda
     * @param cell
     * @return 
     */
    public boolean isSaidaEsquerda(Road cell) {
        return cell.getX() - 1 < 0 && cell.getDirecao() == 4;
    }

    /**
     * Retorna se é saída Direita
     * @param cell
     * @return 
     */
    public boolean isSaidaDireita(Road cell) {
        return cell.getX() + 1 >= this.coluna && cell.getDirecao() == 2;
    }

    /**
     * Retorna String da imagem conforme linha e coluna informada por parâmetro
     * @param linha
     * @param coluna
     * @return 
     */
    public String getImagemFromPosicao(int linha, int coluna) {
        return matrix[coluna][linha].getImagem();
    }
    
    /**
     * Atualizar a tela de simulação conforme malha atual
     */
    public void updateRoadMesh() {
        this.telaSimulacao.getPainelPrincipal().updateUI();
    }
    
    /**
     * Retorna o arquivo de malha
     * @return 
     */
    public File getFile() {
        return file;
    }

    /**
     * Retorna o valor da linha
     * @return 
     */
    public int getLinha() {
        return linha;
    }

    /**
     * Retorna o valor da coluna
     * @return 
     */
    public int getColuna() {
        return coluna;
    }
    
    /**
     * seta o arquivo de malha
     * @param file 
     */
    public void setPathName(File file) {
        this.file = file;
    }

    /**
     * Retorna os parametros da simulação
     * @return 
     */
    public ParametrosSimulacao getParametros() {
        return this.parametros;
    }

    /**
     * Seta os parametros da simulação
     * @param parametros 
     */
    public void setParametros(ParametrosSimulacao parametros) {
        this.parametros = parametros;
    }

    /**
     * Retorna matrix
     * @return 
     */
    public Road[][] getMatrix() {
        return this.matrix;
    }

    /**
     * Retorna a quantidade máxima de carros na malha de acordo com a quantidade de espaços disponíveis na malha
     * @return 
     */
    public int getMaxCarrosMalha() {
        return maxCarrosMalha;
    }

    
}
