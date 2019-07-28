package functions;

public abstract class Function {

    public abstract double getValueAt(double x);

    public double getDerivativeAt(double x, double dx){
        double df = getValueAt(x+dx/2)-getValueAt(x-dx/2);
        return df/dx;
    }

    public double getIntegralAt(double x, double dx, IntegralStyle style){
        switch (style){
            case SQUAD:
                return getValueAt(x)*dx;
            case TRAPEZE:
                return (getValueAt(x)+getValueAt(x-dx))/2 * dx;
        }
        return 0;
    }

    public enum IntegralStyle{
        SQUAD, TRAPEZE
    }

}
