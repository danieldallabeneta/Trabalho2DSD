package Model;

import java.util.concurrent.Semaphore;

public abstract class Road {

    protected Semaphore semaphore;
    protected String imagem = "/image/road0.png";
    protected boolean isCelulaEntrada;
    protected boolean isCelulaSaida;
    protected int direcao;
    protected Car car;
    protected int x;
    protected int y;

    public Road(int x, int y, int direcao) {
        this.car       = null;
        this.direcao   = direcao;
        this.x         = x;
        this.y         = y;
        this.semaphore = new Semaphore(1);
        this.setImagemCelula();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagePath) {
        this.imagem = imagePath;
    }

    public int getDirecao() {
        return direcao;
    }

    public void setDirecao(int direcao) {
        this.direcao = direcao;
    }
    
    /**
     * Seta a imagem da celula
     */
    public void setImagemCelula() {
        if (this.isCelulaCruzamento()) {
            this.setImagemCruzamento();
        } else {
            this.setImagemRodovia();
        }
    }
    
    /**
     * Verifica se a celula pertence a um cruzamento
     * @return 
     */
    public boolean isCelulaCruzamento() {
        return this.direcao > 4;
    }

    /**
     * Seta a imagem do cruzamento
     */
    public void setImagemCruzamento() {
        this.imagem = "/image/crossroad.png";
    }

    /**
     * Seta a imagem da rodovia
     */
    public void setImagemRodovia() {
        this.imagem = "/image/road" + this.direcao + ".png";
    }

    /**
     * Seta a imagem do carro
     */
    public void setImagemCarro() {
        setImagem(car.getTipoCarro());
    }

    /**
     * Retorna se a celula está disponível
     * @return 
     */
    public boolean isDisponivel() {
        return this.car == null;
    }

    /**
     * retorna se a celula é de entrada
     * @return 
     */
    public boolean isCelulaEntrada() {
        return isCelulaEntrada;
    }

    /**
     * Seta se a celula é de entrada
     * @param isCelulaEntrada 
     */
    public void setIsCelulaEntrada(boolean isCelulaEntrada) {
        this.isCelulaEntrada = isCelulaEntrada;
    }
    
    /**
     * Retorna se a celula é de saída
     * @return 
     */
    public boolean isCelulaSaida() {
        return isCelulaSaida;
    }

    /**
     * Seta se a celula é de saída
     * @param isCelulaSaida 
     */
    public void setIsCelularSaida(boolean isCelulaSaida) {
        this.isCelulaSaida = isCelulaSaida;
    }

    public abstract boolean tryAcquire();

    public abstract void release();

    public abstract void addCar(Car car);

    public abstract void removeCar();


}
