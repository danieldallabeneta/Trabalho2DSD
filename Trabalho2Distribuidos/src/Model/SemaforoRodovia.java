package Model;

import java.util.concurrent.TimeUnit;

public class SemaforoRodovia extends Road {

    public SemaforoRodovia(int x, int y, int direcao) {
        super(x, y, direcao);
    }

    @Override
    public boolean tryAcquire() {
        boolean acquired = false;
        try {
            acquired = super.semaphore.tryAcquire(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return acquired;
    }

    @Override
    public void release() {
        super.semaphore.release();
    }

    @Override
    public void addCar(Car car) {
        super.car = car;
        setImagemCarro();
    }

    @Override
    public void removeCar() {
        super.car = null;
        this.setImagemCelula();
    }
    
}
