package Model;

import Controller.ControllerMalha;
import Controller.ControllerSimulacao;

import java.util.*;

public class Car extends Thread {

    private ArrayList<Road> route;
    private Road matrix[][];
    private Random random;
    private Road currentRoad;
    private ControllerMalha meshController;
    private int speed;
    private String tipoCarro;
    protected String[] carros = new String[4];

    private ControllerSimulacao simulation;

    public Car() {
        this.route          = new ArrayList<>();
        this.meshController = ControllerMalha.getInstance();
        this.matrix         = meshController.getMatrix();
        this.random         = new Random();
        this.speed          = random.nextInt(100) + 500;
        this.currentRoad    = null;
        setCarrosOnList();
        setCarImage();
    }

    public void setSimulation(ControllerSimulacao simulation) {
        this.simulation = simulation;
    }

    public String getTipoCarro() {
        return tipoCarro;
    }

    public void setCarrosOnList(){
        carros[0] = "/image/police.png";
        carros[1] = "/image/antigo.png";
        carros[2] = "/image/laranja.png";
        carros[3] = "/image/taxi.png";
    }
    
    public void setCarImage() {
        this.random    = new Random();
        int carro      = random.nextInt(3);
        this.tipoCarro = carros[carro];
    }
    
    /**
     * Trata cruzamento da rodovia
     */
    private void trataCruzamento() {
        try {
            sleep(this.speed);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Road> cruzamentoAAdquirir = new ArrayList<>();
        ArrayList<Road> cruzamentoAdquirido = new ArrayList<>();

        for (int i = 0; i < this.route.size(); i++) {
            Road road = this.route.get(i);
            cruzamentoAAdquirir.add(road);
            if (!road.isCelulaCruzamento()) {
                break;
            }
        }

        for (Road crossroadToAcquire : cruzamentoAAdquirir) {
            if (crossroadToAcquire.tryAcquire()) {
                cruzamentoAdquirido.add(crossroadToAcquire);
            } else {
                for (Road acquiredRoad : cruzamentoAdquirido) {
                    acquiredRoad.release();
                }
                break;
            }
        }

        boolean validaCruzamentos = cruzamentoAdquirido.size() == cruzamentoAAdquirir.size();

        if (validaCruzamentos) {
            for (Road acquiredCrossroad : cruzamentoAdquirido) {
                this.route.remove(acquiredCrossroad);
                this.move(acquiredCrossroad, false);
            }
        }
    }

    /**
     * Método move o veículo
     * @param nextRoad
     * @param adquirido 
     */
    private void move(Road nextRoad, boolean adquirido) {
        if (nextRoad.isDisponivel()) {

            boolean acquired = false;

            if (adquirido) {
                do {
                    if (nextRoad.tryAcquire()) {
                        acquired = true;
                    }
                } while (!acquired);
            }

            nextRoad.addCar(this);
            Road previousRoad = this.getRodoviaAtual();

            if (previousRoad != null) {
                previousRoad.removeCar();
                previousRoad.release();
            }

            this.setRodoviaAtual(nextRoad);
            this.meshController.updateRoadMesh();

            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run() {
        
        try {
            while (!route.isEmpty() && !this.simulation.getEncerrado()) {
                int nextRoadIndex = 0;

                if (route.get(nextRoadIndex).isCelulaCruzamento()) {
                    trataCruzamento();
                } else {
                    Road road = this.route.remove(nextRoadIndex);

                    this.move(road, true);
                }
                if(this.simulation.getEncerrado()){
                    this.getRodoviaAtual().removeCar();
                    this.getRodoviaAtual().release();
                    this.simulation.removeCarroEmAndamento(this);
                    this.meshController.updateRoadMesh();
                    return;
                }

            }
            this.getRodoviaAtual().removeCar();
            this.getRodoviaAtual().release();
            this.simulation.removeCarroEmAndamento(this);
            this.meshController.updateRoadMesh();
            
        } catch (Exception e) {
            this.simulation.setCarrosEmMovimento();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Define a rota 
     * @param rodovia
     * @throws Exception 
     */
    public void defineRota(Road rodovia) throws Exception {
        boolean permiteSairCruzamento = false;

        Road proximaRotaRodovia = rodovia;

        route.add(proximaRotaRodovia);

        int qtdCelulaCruzamentoAtual = 0;

        while (!permiteSairCruzamento) {
            int direcao = proximaRotaRodovia.getDirecao();
            int linha   = proximaRotaRodovia.getX();
            int coluna  = proximaRotaRodovia.getY();

            boolean isRodovia = direcao <= 4;

            if (isRodovia) {
                proximaRotaRodovia = this.defineRodovia(direcao, linha, coluna);
            } else {
                proximaRotaRodovia = this.defineCruzamento(direcao,linha,coluna,qtdCelulaCruzamentoAtual);
                if (proximaRotaRodovia.isCelulaCruzamento()) {
                    qtdCelulaCruzamentoAtual++;
                } else {
                    qtdCelulaCruzamentoAtual = 0;
                }
            }
            route.add(proximaRotaRodovia);
            permiteSairCruzamento = proximaRotaRodovia.isCelulaSaida();
        }
    }

    /**
     * Define a rota do veículo
     * @param direcao
     * @param linha
     * @param coluna
     * @return
     * @throws Exception 
     */
    public Road defineRodovia(int direcao, int linha, int coluna) throws Exception {
        switch (direcao) {
            case 1:
                return this.matrix[linha][coluna - 1];
            case 2:
                return this.matrix[linha + 1][coluna];
            case 3:
                return this.matrix[linha][coluna + 1];
            case 4:
                return this.matrix[linha - 1][coluna];
            default:
                throw new Exception("Direção inválida");
        }
    }

    /**
     * Define a rota do veículo no cruzamento
     * @param direcao
     * @param linha
     * @param coluna
     * @param tempoNoCruzamento
     * @return
     * @throws Exception 
     */
    private Road defineCruzamento(int direcao,int linha,int coluna,int tempoNoCruzamento) throws Exception {
        switch (direcao) {
            case 5: {
                return this.matrix[linha][coluna - 1];
            }
            case 6: {
                return this.matrix[linha + 1][coluna];
            }
            case 7: {
                return this.matrix[linha][coluna + 1];
            }
            case 8: {
                return this.matrix[linha - 1][coluna];
            }
            case 9: {
                if (tempoNoCruzamento == 3) {
                    return this.matrix[linha + 1][coluna];
                } else {
                    int escolha = random.nextInt(2);
                    if (escolha == 0) {
                        return this.matrix[linha][coluna - 1];
                    } else {
                        return this.matrix[linha + 1][coluna];
                    }
                }
            }
            case 10: {
                if (tempoNoCruzamento == 3) {
                    return this.matrix[linha][coluna - 1];
                } else {
                    int escolha = random.nextInt(2);
                    if (escolha == 0) {
                        return this.matrix[linha][coluna - 1];
                    } else {
                        return this.matrix[linha - 1][coluna];
                    }
                }
            }
            case 11: {
                if (tempoNoCruzamento == 3) {
                    return this.matrix[linha][coluna + 1];
                } else {
                    int escolha = random.nextInt(2);
                    if (escolha == 0) {
                        return this.matrix[linha + 1][coluna];
                    } else {
                        return this.matrix[linha][coluna + 1];
                    }
                }
            }
            case 12: {
                if (tempoNoCruzamento == 3) {
                    return this.matrix[linha - 1][coluna];
                } else {
                    int escolha = random.nextInt(2);
                    if (escolha == 0) {
                        return this.matrix[linha][coluna + 1];
                    } else {
                        return this.matrix[linha - 1][coluna];
                    }
                }
            }
            default: {
                throw new Exception("Direção Inválida");
            }
        }
    }

    /**
     * Retorna rodovia Atual
     * @return 
     */
    public Road getRodoviaAtual() {
        return currentRoad;
    }

    /**
     * Seta a rodovia atual
     * @param rodoviaAtual 
     */
    public void setRodoviaAtual(Road rodoviaAtual) {
        this.currentRoad = rodoviaAtual;
    }

}
