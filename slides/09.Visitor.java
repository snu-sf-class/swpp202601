// Car - Wheel, Engine, Body
// User - (1) Wheel -> ...
//        (2) Engine -> ...
// Visitor
interface Wheel {
    String getName();
}
interface Engine {
    void start();
    void stop();
}
interface Visitor {
    void visit(Wheel wheel);
    void visit(Engine engine);
}
// Element or Elements
interface Elements {
    void accept(Visitor visitor); // CarElements have to provide accept().
}
class WheelImpl implements Wheel, Elements {
    private String name;
    public WheelImpl(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
class EngineImpl implements Engine, Elements {
    public void start() {
        System.out.println("Engine started");
    }
    public void stop() {
        System.out.println("Engine stopped");
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
class BodyImpl implements Elements {
    public void accept(Visitor visitor) {
        /* do nothing */
    }
}
class Car implements Elements {
    private WheelImpl[] wheels;
    private EngineImpl engine;
    private BodyImpl body;
    public Car() {
        wheels = new WheelImpl[] {
            new WheelImpl("front left"), new WheelImpl("front right"),
            new WheelImpl("back left") , new WheelImpl("back right") };
        engine = new EngineImpl();
        body = new BodyImpl();
    }
    public void accept(Visitor visitor) {
        engine.accept(visitor);
        body.accept(visitor);
        for (WheelImpl element : this.wheels) {
            element.accept(visitor);
        }
    }
}
class DumbVisitor1 implements Visitor {
    public void visit(Wheel wheel) {
        System.out.println("Visiting " + wheel.getName() + " wheel");
    }
    public void visit(Engine engine) {
        engine.start();
    }
}
class DumbVisitor2 implements Visitor {
    public void visit(Wheel wheel) {
        System.out.println("Kicking my " + wheel.getName() + " wheel");
    }
    public void visit(Engine engine) {
        engine.stop();
    }
}
class Main {
    static public void main(String[] args) {
        Car car = new Car();
        car.accept(new DumbVisitor1());
        car.accept(new DumbVisitor2());
    }
}
