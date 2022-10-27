package Controller;

import Model.Car;
import Model.Road;
import Model.ParametrosSimulacao;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ControllerSimulacao extends Thread {

    Queue<Car> filaCarros;
    private final ControllerMalha controller;
    private Road[][] matrix;
    private Boolean finalizandoSimulacao = false;
    private Boolean encerraSimulacao = false;
    private final ArrayList<Car> carrosAndando;

    public ControllerSimulacao(ControllerMalha controllerMalha) {
        this.controller = controllerMalha;
        this.filaCarros = new LinkedList<>();
        this.carrosAndando = new ArrayList<>();
    }

    /**
     * Retorna se está finalizando a simulação
     * @return 
     */
    public Boolean finalizando() {
        return this.finalizandoSimulacao == true;
    }
    /**
     * Retorna se a simulação está encerrada
     * @return 
     */
    public Boolean encerrado() {
        return this.encerraSimulacao == true;
    }
    
    public boolean getEncerrado(){
        return this.encerraSimulacao;
    }
    
    /**
     * Seta se deve finalizar a simulação
     * @param finalizaSimulacao 
     */
    public void setFinalizaSimulacao(boolean finalizaSimulacao) {
        this.finalizandoSimulacao = finalizaSimulacao;
    }
    
    /**
     * Seta se deve finalizar a simulação
     * @param encerraSimulacao
     */
    public void setEncerraSimulacao(boolean encerraSimulacao) {
        this.encerraSimulacao = encerraSimulacao;
//        int aux = this.filaCarros.size();
//        for (int i = 0; i < aux; i++) {
//            Car car = filaCarros.remove();
//            
//        }
       
        
        setCarrosEmMovimento();
    }
    
    /**
     * Adiciona carro na malha
     * @param car 
     */
    public void addCarroEmAndamento(Car car) {
        this.carrosAndando.add(car);
    }
    
    /**
     * Remove carro da malha
     * @param car 
     */
    public void removeCarroEmAndamento(Car car) {
        this.carrosAndando.remove(car);
        setCarrosEmMovimento();
    }

    @Override
    public void run() {
        validaQuantidades();
        this.filaCarros = this.criaCarros();
        setCarrosEmMovimento();

        this.matrix = this.controller.getMatrix();

        this.controller.updateRoadMesh();

        ParametrosSimulacao params = this.controller.getParametros();
        int qtdMaxFila = params.getQtdCarros();
        boolean permiteInserirCarros = this.carrosAndando.size() < qtdMaxFila;

        while (permiteInserirCarros || !this.finalizandoSimulacao || !this.encerraSimulacao) {
            for (int y = 0; y < this.controller.getLinha(); y++) {
                for (int x = 0; x < this.controller.getColuna(); x++) {

                    Road rodoviaInicial = matrix[x][y];
                    
                    if (permiteAdicionarCarroNaMalha(rodoviaInicial) && permiteInserirCarros) {
                        try {
                            sleep(params.getIntervaloCarros() * 1000);

                            Car car = filaCarros.remove();

                            car.defineRota(rodoviaInicial);
                            car.start();

                            car.setSimulation(this);

                            this.addCarroEmAndamento(car);
                            setCarrosEmMovimento();
                            filaCarros.add(new Car());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Valida se permite adiciona novo carro na malha
     * @param rodoviaInicial
     * @return 
     */
    private boolean permiteAdicionarCarroNaMalha(Road rodoviaInicial){
        boolean celulaEntrada             = rodoviaInicial.isCelulaEntrada();
        boolean celulaVazia               = rodoviaInicial.isDisponivel();
        boolean finalizandoSimulacao      = !this.finalizandoSimulacao;
        boolean encerraSimulacao          = !this.encerraSimulacao;
        boolean carrosEmAndamentoCompleto = this.carrosAndando.size() < this.controller.getParametros().getMaxCarros();
        return celulaEntrada && celulaVazia && finalizandoSimulacao && encerraSimulacao && carrosEmAndamentoCompleto;
    }

    /**
     * Valida a quantidade de carros informada nas configurações iniciais
     */
    private void validaQuantidades() {
        ParametrosSimulacao params = this.controller.getParametros();
        int maxCarrosMalha   = this.controller.getMaxCarrosMalha();
        int qtdCarrosInfo    = params.getQtdCarros();
        int qtdMaxCarrosInfo = params.getMaxCarros();
        
        /* Valida se a quantidade de carros informada é maior que a quantidade máxima que a malha comporta */
        if (qtdCarrosInfo > maxCarrosMalha) {
            params.setQtdCarros(maxCarrosMalha);
        } 
        
        /* valida se a quantidade máxima informada é maior que a quantidade que a malha comporta */
        if(qtdMaxCarrosInfo > maxCarrosMalha){
            params.setMaxCarros(maxCarrosMalha);
        }
        
        /* valida se a quantidade máxima de carros informada é maior que a quantidade de carros informada */
        if(qtdMaxCarrosInfo > qtdCarrosInfo){
            params.setMaxCarros(qtdCarrosInfo);
        }

    }
    
    /**
     * Cria os carros inicialmente para a malha conforme a quantidade de carros definidas
     * @return 
     */
    public Queue<Car> criaCarros() {
        Queue<Car> cars = new LinkedList<>();
        for (int i = 0; i < this.controller.getParametros().getQtdCarros(); i++) {
            cars.add(new Car());
        }
        return cars;
    }
        
    /**
     * Seta a quantidade de carros em movimento
     */
    public void setCarrosEmMovimento(){
        if(this.encerraSimulacao){
            this.controller.getContagemCarrosAndando().setText("Carros em Movimento: 0");
        } else {
            this.controller.getContagemCarrosAndando().setText("Carros em Movimento: " + this.carrosAndando.size());
        }
    }

}
